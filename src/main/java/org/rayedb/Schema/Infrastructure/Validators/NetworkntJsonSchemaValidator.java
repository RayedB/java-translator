package org.rayedb.Schema.Infrastructure.Validators;

import jakarta.enterprise.context.ApplicationScoped;
import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.rayedb.Schema.Domain.Interface.JsonSchemaValidator;
import org.rayedb.Schema.Infrastructure.Exceptions.JsonSchemaValidationException;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class NetworkntJsonSchemaValidator implements JsonSchemaValidator {
    
    @Override
    public void validate(JsonNode json, JsonNode schemaDefinition) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        JsonSchema schema = factory.getSchema(schemaDefinition);
        
        Set<ValidationMessage> validationResult = schema.validate(json);
        
        if (!validationResult.isEmpty()) {
            throw new JsonSchemaValidationException("JSON validation failed: " + 
                validationResult.stream()
                    .map(ValidationMessage::getMessage)
                    .collect(Collectors.joining(", "))
            );
        }
        System.err.println("Validation passed");
    }
} 