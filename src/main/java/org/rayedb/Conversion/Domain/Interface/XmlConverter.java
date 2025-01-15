package org.rayedb.Conversion.Domain.Interface;

import com.fasterxml.jackson.databind.JsonNode;

public interface XmlConverter {
    JsonNode convertToJson(String xml);
} 