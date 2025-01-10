package org.rayedb.Schema.Adapters.Secondaries;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.WriteModel;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.bson.Document;
import org.rayedb.Schema.Domain.Entity.Schema;
import org.rayedb.Schema.Domain.Interface.SchemaRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Date;
import java.time.ZoneId;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
class MongoSchemaRepository implements SchemaRepository {
    private final MongoClient mongoClient;
    private final String databaseName;
    private final ObjectMapper objectMapper;
    private static final String COLLECTION_NAME = "schemas";

    @Inject
    public MongoSchemaRepository(
            MongoClient mongoClient,
            ObjectMapper objectMapper,
            @ConfigProperty(name = "quarkus.mongodb.database") String databaseName) {
        this.mongoClient = mongoClient;
        this.objectMapper = objectMapper;
        this.databaseName = databaseName;
    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase(databaseName).getCollection(COLLECTION_NAME);
    }

    public Schema create(Schema schema) {
        Document doc = new Document()
            .append("slug", schema.getSlug())
            .append("createdAt", Date.from(schema.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()))
            .append("version", schema.getVersion())
            .append("latest", schema.isLatest())
            .append("schemaDefinition", objectMapper.convertValue(schema.getSchemaDefinition(), Document.class));
        
        getCollection().insertOne(doc);
        return schema;
    }

    public Schema get(String slug) {
        Document doc = getCollection().find(new Document("slug", slug)).first();
        if (doc == null) return null;
        
        return new Schema(
            doc.getString("slug"),
            ((Date) doc.get("createdAt")).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            doc.getString("version"),
            doc.getBoolean("latest"),
            objectMapper.convertValue(doc.get("schemaDefinition"), JsonNode.class)
        );
    }

    public Schema update(Schema schema) {
        String slug = schema.getSlug();
        List<WriteModel<Document>> operations = Arrays.asList(
            new UpdateOneModel<>(
                Filters.and(Filters.eq("slug", slug), Filters.eq("latest", true)), 
                Updates.set("latest", false)
            ),
            new InsertOneModel<>(
                new Document("slug", slug)
                    .append("createdAt", schema.getCreatedAt())
                    .append("version", schema.getVersion())
                    .append("latest", true)
                    .append("schemaDefinition", schema.getSchemaDefinition())
            )
        );

        try {
            getCollection().bulkWrite(operations);
        } catch (MongoException e) {
            throw new RuntimeException("Failed to update schema", e);
        }
        return schema;
    }

    public void delete(String slug) {
        getCollection().deleteOne(new Document("slug", slug));
    }

    public List<Schema> getAll() {
        System.out.println("doc");
        List<Schema> schemas = new ArrayList<>();
        for (Document doc : getCollection().find()) {  
            System.out.println(doc);
            schemas.add(new Schema(
                doc.getString("slug"),
                ((Date) doc.get("createdAt")).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                doc.getString("version"),
                doc.getBoolean("latest"),
                objectMapper.convertValue(doc.get("schemaDefinition"), JsonNode.class)
            ));
        }
        System.out.println(schemas);
        return schemas;
    }
}
