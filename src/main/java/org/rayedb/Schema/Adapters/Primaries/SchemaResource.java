package org.rayedb.Schema.Adapters.Primaries;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.util.Set;

// Use cases
import org.rayedb.Schema.Domain.UseCases.CreateSchema;

import com.fasterxml.jackson.databind.JsonNode;
import org.rayedb.Schema.Infrastructure.Exceptions.JsonSchemaValidationException;
import org.rayedb.Schema.Infrastructure.Validators.JsonSchemaValidator;
import org.rayedb.Share.DTO.ErrorResponse;
import org.rayedb.Schema.Domain.Entity.Schema;

@Path("/schemas")
public class SchemaResource {
    private final CreateSchema createSchema;

    @Inject
    public SchemaResource(CreateSchema createSchema) {
        this.createSchema = createSchema;
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(JsonNode jsonSchema) {
        try {
            JsonSchemaValidator.validate(jsonSchema);
            
            Schema schema = Schema.create(jsonSchema);
            Schema createdSchema = createSchema.handle(schema);
            return Response.ok(schema).build();
            
        } catch (JsonSchemaValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(new ErrorResponse(e.getMessage()))
                         .build();
        }
    }
}
