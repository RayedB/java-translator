package org.rayedb.Schema.Domain.Interface;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonSchemaValidator {
    void validate(JsonNode json, JsonNode schemaDefinition);
} 