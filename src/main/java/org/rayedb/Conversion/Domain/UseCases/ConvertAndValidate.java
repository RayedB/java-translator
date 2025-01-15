package org.rayedb.Conversion.Domain.UseCases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.rayedb.Conversion.Domain.Interface.XmlConverter;
import org.rayedb.Schema.Domain.Entity.Schema;
import org.rayedb.Schema.Domain.Interface.SchemaRepository;
import org.rayedb.Conversion.Domain.Exceptions.XmlConversionException;
import org.rayedb.Conversion.Domain.Exceptions.SchemaNotFoundException;
import org.rayedb.Schema.Domain.Interface.JsonSchemaValidator;

import com.fasterxml.jackson.databind.JsonNode;

@ApplicationScoped
public class ConvertAndValidate {
    private final XmlConverter xmlConverter;
    private final SchemaRepository<Schema, String> schemaRepository;
    private final JsonSchemaValidator jsonSchemaValidator;

    @Inject
    public ConvertAndValidate(
        XmlConverter xmlConverter, 
        SchemaRepository<Schema, String> schemaRepository,
        JsonSchemaValidator jsonSchemaValidator
    ) {
        this.xmlConverter = xmlConverter;
        this.schemaRepository = schemaRepository;
        this.jsonSchemaValidator = jsonSchemaValidator;
    }

    public JsonNode handle(String xml, String slug) {
        // Convert xml to json
        JsonNode json = convertOrFail(xml);
        
        Schema schema = getSchemaOrFail(slug);

        // Validate json against schema
        JsonNode validatedJson = validateOrFail(json, schema);

        // Return json
        return validatedJson;
    }
    

    private JsonNode convertOrFail(String xml) {
        JsonNode json = xmlConverter.convertToJson(xml);
        // OPTIONALS 
        if (json == null) {
            throw new XmlConversionException("Failed to convert XML to JSON");
        }
        return json;
    }

    private Schema getSchemaOrFail(String slug) {
        Schema schema = schemaRepository.get(slug);
        if (schema == null) {
            throw new SchemaNotFoundException("Schema not found for slug: " + slug + ". Try with another schema or try a generic conversion.");
        }
        return schema;
    }

    private JsonNode validateOrFail(JsonNode json, Schema schema) {
        jsonSchemaValidator.validate(json, schema.getSchemaDefinition());
        return json;
    }
} 
