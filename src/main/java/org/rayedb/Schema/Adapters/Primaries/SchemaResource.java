package org.rayedb.Schema.Adapters.Primaries;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.databind.JsonNode;
import org.rayedb.Schema.Infrastructure.Exceptions.JsonSchemaValidationException;
import org.rayedb.Schema.Infrastructure.Validators.JsonSchemaValidator;
import org.rayedb.Share.DTO.ErrorResponse;

import java.util.List;

import org.rayedb.Schema.Domain.Entity.Schema;
import org.rayedb.Schema.Domain.UseCases.CreateSchema;
import org.rayedb.Schema.Domain.UseCases.GetSchemas;
import org.rayedb.Schema.Domain.UseCases.UpdateSchema;
@Path("/schemas")
public class SchemaResource {
    private final CreateSchema createSchema;
    private final UpdateSchema updateSchema;
    private final GetSchemas getSchemas;
    @Inject
    public SchemaResource(CreateSchema createSchema, UpdateSchema updateSchema, GetSchemas getSchemas) {
        this.createSchema = createSchema;
        this.updateSchema = updateSchema;
        this.getSchemas = getSchemas;
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Schema> schemas = getSchemas.handle();
        return Response.ok(schemas).build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(JsonNode requestBody) {
        try {
            JsonNode schemaDefinition = requestBody.get("schemaDefinition");
            String slug = requestBody.get("slug").asText();
            
            // JsonSchemaValidator.validate(schemaDefinition);
            
            Schema createdSchema = createSchema.handle(slug, schemaDefinition);
            return Response.status(Response.Status.CREATED).entity(createdSchema).build();
            
        } catch (JsonSchemaValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(new ErrorResponse(e.getMessage()))
                         .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(new ErrorResponse("Invalid request body. Required fields: slug, schemaDefinition"))
                         .build();
        }
    }


    @PUT
    @Path("/{slug}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("slug") String slug, JsonNode requestBody) {
        try {
            JsonNode schemaDefinition = requestBody.get("schemaDefinition");
            JsonSchemaValidator.validate(schemaDefinition);
            Schema updatedSchema = updateSchema.handle(slug, schemaDefinition);
            return Response.ok(updatedSchema).build();
        } catch (JsonSchemaValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(new ErrorResponse(e.getMessage()))
                         .build();
        }
    }
}
