package org.rayedb.Schema.Infrastructure.Validators;

import java.util.Set;

import org.rayedb.Schema.Infrastructure.Exceptions.JsonSchemaValidationException;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.*;

public class JsonSchemaValidator {
    public static void validate(JsonNode schemaDefinition) throws JsonSchemaValidationException {
        try {
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
            JsonSchema schema = factory.getSchema(schemaDefinition);
            Set<ValidationMessage> messages = schema.validate(schemaDefinition);
            if (!messages.isEmpty()) {
                throw new JsonSchemaValidationException("Schema validation failed: " + messages);
            }
        } catch (Exception e) {
            throw new JsonSchemaValidationException("Invalid schema: " + e.getMessage());
        }
    }
} 