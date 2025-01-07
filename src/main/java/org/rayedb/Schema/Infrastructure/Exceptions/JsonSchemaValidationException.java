package org.rayedb.Schema.Infrastructure.Exceptions;

public class JsonSchemaValidationException extends RuntimeException {
    public JsonSchemaValidationException(String message) {
        super(message);
    }
} 