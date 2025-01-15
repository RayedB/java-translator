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


// CREATE NEW SQLSCHEMAREPOSITORY

@ApplicationScoped
class MongoSchemaRepository implements SchemaRepository <Schema, String> {
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

    @Override
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

    @Override
    public Schema get(String slug) {
        Document doc = getCollection().find(new Document("slug", slug).append("latest", true)).first();
        if (doc == null) return null;
        return new Schema(
            doc.getString("slug"),
            ((Date) doc.get("createdAt")).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            doc.getString("version"),
            doc.getBoolean("latest"),
            objectMapper.convertValue(doc.get("schemaDefinition"), JsonNode.class)
        );
    }

    // USE JAVA STREAM API
    // BULK OF XML ITEMS AND STREAM THROUGH THAT
    // MANUAL VALIDATION AS WELL
    // (Streaming and lambda)

    @Override
    public Schema update(Schema schema) {
        String slug = schema.getSlug();
        List<WriteModel<Document>> operations = Arrays.asList(
            new UpdateOneModel<>(
                Filters.and(Filters.eq("slug", slug), Filters.eq("latest", true)), 
                Updates.set("latest", false)
            ),
            new InsertOneModel<>(
                new Document("slug", slug)
                    .append("createdAt", Date.from(schema.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()))
                    .append("version", schema.getVersion())
                    .append("latest", true)
                    .append("schemaDefinition", objectMapper.convertValue(schema.getSchemaDefinition(), Document.class))
            )
        );
        try {
            getCollection().bulkWrite(operations);
        } catch (MongoException e) {
            throw new RuntimeException("Failed to update schema", e);
        }
        return schema;
    }

    @Override
    public void delete(String slug) {
        getCollection().deleteOne(new Document("slug", slug));
    }

    @Override
    public List<Schema> getAll() {
        List<Schema> schemas = new ArrayList<>();
        for (Document doc : getCollection().find()) {  
            schemas.add(new Schema(
                doc.getString("slug"),
                ((Date) doc.get("createdAt")).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                doc.getString("version"),
                doc.getBoolean("latest"),
                objectMapper.convertValue(doc.get("schemaDefinition"), JsonNode.class)
            ));
        }
        return schemas;
    }
}
