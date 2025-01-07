package org.rayedb;

import jakarta.enterprise.context.ApplicationScoped;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@ApplicationScoped
public class ConvertingService {
    private final XmlMapper xmlMapper = new XmlMapper();
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public JsonNode convertXmlToJson(String xml) {
        try {
            JsonNode node = xmlMapper.readTree(xml);
            return node;
        } catch (Exception e) {
            throw new RuntimeException("Error converting XML to JSON", e);
        }
    }
}

