package org.rayedb.adapters.primaries;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusIntegrationTest
public class SchemaResourceIT {

    @Test
    public void shouldCreateSchemaSuccessfully() {
        String schemaJson = """
            {
                "slug": "cust",
                "schemaDefinition": {
                    "type": "object",
                    "properties": {
                        "name": { "type": "string" },
                        "age": { "type": "integer", "minimum": 0 }
                    },
                    "required": ["name"]
                }
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(schemaJson)
        .when()
            .post("/schemas")
        .then()
            .statusCode(201)  // Should be 201 for resource creation
            .contentType(ContentType.JSON)
            .body("slug", is("cust"))
            .body("schemaDefinition", notNullValue())
            .body("version", notNullValue())
            .body("latest", is(true));
    }

    // @Test
    // public void shouldReturn400ForInvalidSchema() {
    //     String invalidSchemaJson = """
    //         {
    //             "slug": "cust",
    //             "schemaDefinition": {
    //                 "type": "invalid",
    //                 "properties": {
    //                     "name": { "type": "string" }
    //                 }
    //             }
    //         }
    //         """;

    //     given()
    //         .contentType(ContentType.JSON)
    //         .body(invalidSchemaJson)
    //     .when()
    //         .post("/schemas")
    //     .then()
    //         .statusCode(400)
    //         .contentType(ContentType.JSON)
    //         .body("message", notNullValue());
    // }

    @Test
    public void shouldReturn400WhenSchemaDefinitionIsMissing() {
        String invalidJson = """
            {
                "slug": "cust"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(invalidJson)
        .when()
            .post("/schemas")
        .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body("message", notNullValue());
    }
}