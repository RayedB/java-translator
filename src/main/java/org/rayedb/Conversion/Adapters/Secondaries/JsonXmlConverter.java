package org.rayedb.Conversion.Adapters.Secondaries;

import jakarta.enterprise.context.ApplicationScoped;

import org.json.JSONObject;
import org.json.XML;
import org.rayedb.Conversion.Domain.Interface.XmlConverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class JsonXmlConverter implements XmlConverter {
    @Override
    public JsonNode convertToJson(String xml) {
        try {
            JSONObject jsonObj = XML.toJSONObject(xml);
            System.out.println("jsonObj: " + jsonObj);
            return new ObjectMapper().readTree(jsonObj.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error converting XML to JSON", e);
        }
    }
}