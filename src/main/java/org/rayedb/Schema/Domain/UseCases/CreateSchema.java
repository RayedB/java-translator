package org.rayedb.Schema.Domain.UseCases;

import jakarta.enterprise.context.ApplicationScoped;

import org.rayedb.Schema.Domain.Entity.Schema;



@ApplicationScoped
public class CreateSchema {
    public Schema handle(Schema schema) {
        return schema;
    }
}
