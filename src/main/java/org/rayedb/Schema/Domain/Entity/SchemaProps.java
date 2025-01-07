package org.rayedb.Schema.Domain.Entity;

import com.fasterxml.jackson.databind.JsonNode;

public class SchemaProps {
    private final JsonNode schemaDefinition;

    public SchemaProps(JsonNode schemaDefinition) {
        this.schemaDefinition = schemaDefinition;
    }
}