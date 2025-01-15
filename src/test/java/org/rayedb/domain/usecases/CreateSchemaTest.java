package org.rayedb.domain.usecases;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rayedb.Schema.Domain.Entity.Schema;
import org.rayedb.Schema.Domain.UseCases.CreateSchema;
import org.rayedb.Schema.Infrastructure.Exceptions.JsonSchemaValidationException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class CreateSchemaTest {
    
    @Inject
    CreateSchema createSchema;
    
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateValidSchema() throws Exception {
        // Arrange
        String jsonString = """
            {
                "type": "object",
                "properties": {
                    "name": {
                        "type": "string"
                    },
                    "age": {
                        "type": "integer",
                        "minimum": 0
                    }
                },
                "required": ["name"]
            }
            """;
        JsonNode schemaDefinition = objectMapper.readTree(jsonString);

        // Act
        Schema result = createSchema.handle("slug",schemaDefinition);

        // Assert
        assertNotNull(result);
        assertEquals(schemaDefinition, result.getSchemaDefinition());
    }

    @Test
    void shouldThrowExceptionForInvalidSchema() {
        // Arrange
        String invalidJsonString = "{ invalid json }";

        // Act & Assert
        assertThrows(Exception.class, () -> {
            JsonNode invalidSchema = objectMapper.readTree(invalidJsonString);
            createSchema.handle("slug",invalidSchema);
        });
    }

    @Test
    void shouldHandleEmptySchema() throws Exception {
        // Arrange
        String emptySchemaJson = """
            {
                "type": "object",
                "properties": {}
            }
            """;
        JsonNode emptySchema = objectMapper.readTree(emptySchemaJson);

        // Act
        Schema result = createSchema.handle("slug",emptySchema);

        // Assert
        assertNotNull(result);
        assertTrue(result.getSchemaDefinition().get("properties").isEmpty());
    }
}

// TEST REPOSITORY 