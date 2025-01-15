package org.rayedb.adapters.primaries;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusIntegrationTest
public class ConvertingResourceIT {
    
    @BeforeEach
    void setUp() {
        // First create a schema that we'll use for validation
        String schemaJson = """
            {
    "slug": "customer3",
    "schemaDefinition": {
        "$schema": "http://json-schema.org/draft-07/schema#",
        "type": "object",
        "required": [
            "customer"
        ],
        "properties": {
            "customer": {
                "type": "object",
                "required": [
                    "name",
                    "age"
                ],
                "properties": {
                    "name": {
                        "type": "string"
                    },
                    "age": {
                        "type": "integer",
                        "minimum": 0
                    }
                },
                "additionalProperties": false
            }
        },
        "additionalProperties": false
    }
}
            """;

        // Create the schema first
        given()
            .contentType(ContentType.JSON)
            .body(schemaJson)
        .when()
            .post("/schemas")
        .then()
            .statusCode(201);
    }

    // @Test
    // public void shouldReturn400WhenObjectIsNotValid() {
    //     String xmlString = """
    //         <root>
    //             <name>John</name>
    //         </root>
    //         """;
        
    //     given()
    //         .contentType(ContentType.XML)
    //         .body(xmlString)
    //     .when()
    //         .post("/convert")
    //     .then()
    //         .statusCode(400);
    // }

    @Test
    public void shouldReturn200WhenObjectIsValid() {
        String xmlString = """
            <customer>
                <name>John</name>
                <age>25</age>
            </customer>
            """;
        
        given()
            .contentType(ContentType.XML)
            .body(xmlString)
        .when()
            .post("/api/schemas/customer3/convert")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("customer.name", is("John"))
            .body("customer.age", is(25));
    }
}

