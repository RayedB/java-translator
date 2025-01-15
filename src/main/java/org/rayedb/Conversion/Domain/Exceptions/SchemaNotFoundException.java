package org.rayedb.Conversion.Domain.Exceptions;

import jakarta.ws.rs.WebApplicationException;

public class SchemaNotFoundException extends WebApplicationException {
    public SchemaNotFoundException(String message) {
        super(message, 206);
    }
} 