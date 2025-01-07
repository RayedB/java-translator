package org.rayedb.Share.Core;
public abstract class Entity<T> {
    protected final T props;

    protected Entity(T props) {
        this.props = props;
    }

    public T toState() {
        return props;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return false;
    }

    @Override
    public int hashCode() {
        return props != null ? props.hashCode() : 0;
    }
} 