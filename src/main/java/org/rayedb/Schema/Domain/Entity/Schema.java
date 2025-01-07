package org.rayedb.Schema.Domain.Entity;

import org.rayedb.Share.Core.Entity;

import com.fasterxml.jackson.databind.JsonNode;

public class Schema extends Entity<SchemaProps> {
    private final JsonNode schemaDefinition;

    private Schema(JsonNode schemaDefinition) {
        super(new SchemaProps(schemaDefinition));
        this.schemaDefinition = schemaDefinition;
    }

    public static Schema create(JsonNode schemaDefinition) {
        // Here you would add any actual business rules
        // For example: validateRequiredFields(schemaDefinition)
        return new Schema(schemaDefinition);
    }

    public JsonNode getSchemaDefinition() {
        return schemaDefinition;
    }
}
