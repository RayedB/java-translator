package org.rayedb.Schema.Domain.Interface;

import java.util.List;

public interface SchemaRepository<T, ID> {
    T create(T entity);
    T get(ID id);
    T update(T entity);
    void delete(ID id);
    List<T> getAll();
} 