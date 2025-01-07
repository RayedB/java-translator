package org.rayedb.Schema.Domain.Entity;

public record MetadataProps(
    String id,
    String name,
    String description,
    String createdAt,
    String createdBy,
    String version,
    String organizationSlug
) {}
