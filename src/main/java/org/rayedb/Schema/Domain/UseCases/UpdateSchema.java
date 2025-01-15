package org.rayedb.Schema.Domain.UseCases;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import org.rayedb.Schema.Domain.Entity.Schema;
import org.rayedb.Schema.Domain.Interface.SchemaRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.lang.Nullable;

@ApplicationScoped
public class UpdateSchema {
    private final SchemaRepository<Schema, String> schemaRepository;
    @Inject
    public UpdateSchema(SchemaRepository<Schema, String> schemaRepository) {
        this.schemaRepository = schemaRepository;
    }   

    public Schema handle(String slug, JsonNode schemaDefinition) {
        Schema schema = schemaRepository.get(slug);

        String updatedVersion = generateVersion(schema.getVersion(), false);
        schema.setVersion(updatedVersion);
        schema.setSchemaDefinition(schemaDefinition);
        return schemaRepository.update(schema);
    }

    private String generateVersion(@Nullable String version, @Nullable Boolean major) {
        if (version == null) {
            return "1.0.0";
        }
        if (major) {
            return String.format("%d.%d.%d", 
                Integer.parseInt(version.split("\\.")[0]) + 1, 
                Integer.parseInt(version.split("\\.")[1]) , 
                Integer.parseInt(version.split("\\.")[2]) );
        }
        return String.format("%d.%d.%d", 
            Integer.parseInt(version.split("\\.")[0]), 
            Integer.parseInt(version.split("\\.")[1]) + 1, 
            Integer.parseInt(version.split("\\.")[2]) );
    }
}