package org.rayedb.Schema.Domain.Entity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.lang.Nullable;

public class Schema {
    private String slug;
    private LocalDateTime createdAt;
    private String version;
    private boolean latest;
    private JsonNode schemaDefinition;

    public Schema(String slug, JsonNode schemaDefinition, String version) {
        this.slug = slug;
        this.version = version;
        this.schemaDefinition = schemaDefinition;
        this.createdAt = LocalDateTime.now();
        this.latest = true;
    }

    // For MongoDB reconstruction
    public Schema(String slug, LocalDateTime createdAt, String version, boolean latest, JsonNode schemaDefinition) {
        this.slug = slug;
        this.createdAt = createdAt;
        this.version = version;
        this.latest = latest;
        this.schemaDefinition = schemaDefinition;
    }

    public static Schema create(String slug, JsonNode schemaDefinition) {
        if (schemaDefinition.has("version")) {
            return new Schema(slug, schemaDefinition, schemaDefinition.get("version").asText());
        }
        return new Schema(slug, schemaDefinition, "1.0.0");
    }

    // Getters
    public String getSlug() {
        return slug;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getVersion() {
        return version;
    }

    public boolean isLatest() {
        return latest;
    }

    public JsonNode getSchemaDefinition() {
        return schemaDefinition;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setSchemaDefinition(JsonNode schemaDefinition) {
        this.schemaDefinition = schemaDefinition;
    }

}

