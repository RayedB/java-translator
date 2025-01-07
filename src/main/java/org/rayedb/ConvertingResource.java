package org.rayedb;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import com.fasterxml.jackson.databind.JsonNode;

@Path("/api")
public class ConvertingResource {
    @Inject
    ConvertingService service;

    @POST
    @Path("/convert")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonNode convertData(String xmlInput) {
        return service.convertXmlToJson(xmlInput);
    }
}
