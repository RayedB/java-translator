package org.rayedb.Schema.Domain.UseCases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.rayedb.Schema.Domain.Entity.Schema;
import org.rayedb.Schema.Domain.Interface.SchemaRepository;
import com.fasterxml.jackson.databind.JsonNode;

@ApplicationScoped
public class CreateSchema {
    private final SchemaRepository<Schema, String> schemaRepository;

    @Inject
    public CreateSchema(SchemaRepository<Schema, String> schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    public Schema handle(String slug, JsonNode schemaDefinition) {
        
        Schema schema = Schema.create(slug, schemaDefinition);
        return schemaRepository.create(schema);
    }

    
}
