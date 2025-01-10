package org.rayedb.Schema.Domain.Interface;

import java.util.List;

import org.rayedb.Schema.Domain.Entity.Schema;

public interface SchemaRepository {
    Schema create(Schema schema);
    Schema get(String id);
    List<Schema> getAll();
    Schema update(Schema schema);
    void delete(String id);
}
