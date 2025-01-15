package org.rayedb.Conversion.Adapters.Primaries;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.databind.JsonNode;
import org.rayedb.Conversion.Domain.UseCases.ConvertAndValidate;
import org.rayedb.Share.DTO.ErrorResponse;

@Path("/api")
public class ConvertingResource {
    private final ConvertAndValidate convertAndValidate;

    @Inject
    public ConvertingResource(ConvertAndValidate convertAndValidate) {
        this.convertAndValidate = convertAndValidate;
    }

    @POST
    @Path("/schemas/{slug}/convert")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response convertData(@PathParam("slug") String slug, String xmlInput) {
        try {
            JsonNode result = convertAndValidate.handle(xmlInput, slug);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(new ErrorResponse("Error converting XML to JSON: " + e.getMessage()))
                         .build();
        }
    }
}
