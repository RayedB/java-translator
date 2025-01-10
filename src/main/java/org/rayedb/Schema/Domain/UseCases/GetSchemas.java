package org.rayedb.Schema.Domain.UseCases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import org.rayedb.Schema.Domain.Entity.Schema;
import org.rayedb.Schema.Domain.Interface.SchemaRepository;

@ApplicationScoped
public class GetSchemas {
    private final SchemaRepository schemaRepository;
    @Inject
    public GetSchemas(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    public List<Schema> handle() {
        return schemaRepository.getAll();
    }
}