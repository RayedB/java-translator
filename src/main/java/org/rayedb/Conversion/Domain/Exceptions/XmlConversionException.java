package org.rayedb.Conversion.Domain.Exceptions;

import jakarta.ws.rs.WebApplicationException;

public class XmlConversionException extends WebApplicationException {
    public XmlConversionException(String message) {
        super(message, 422);
    }
} 