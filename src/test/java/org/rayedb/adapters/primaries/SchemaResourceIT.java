package org.rayedb.adapters.primaries;


import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.junit.jupiter.api.Test;
import org.rayedb.Schema.Domain.UseCases.CreateSchema;
import org.rayedb.Schema.Domain.Entity.Schema;

import jakarta.ws.rs.core.MediaType;
import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
@QuarkusTest
public class SchemaResourceIT {

    @InjectMock
    CreateSchema createSchema;

    @Test
    public void shouldReturn400WhenSchemaIsInvalid() {
        // Arrange
        String invalidSchema = """
            {
                "type": "invalid_type",
                "properties": {
                    "name": {
                        "type": "string"
                    }
                }
            }
            """;

        // Act & Assert
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(invalidSchema)
            .when()
            .post("/schemas")
            .then()
            .statusCode(400);

        // Verify that createSchema.handle was never called
        verify(createSchema, never()).handle(any(Schema.class));
    }

    @Test
    public void shouldReturn400WhenJsonIsMalformed() {
        // Arrange
        String malformedJson = "{ 'foo':'bar' }";

        // Act & Assert
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(malformedJson)
            .when()
            .post("/schemas")
            .then()
            .statusCode(400);

        verify(createSchema, never()).handle(any(Schema.class));
    }
}