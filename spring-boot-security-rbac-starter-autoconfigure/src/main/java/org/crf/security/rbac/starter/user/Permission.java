package org.crf.security.rbac.starter.user;

import java.util.Objects;

/**
 * @author crf
 */
public class Permission {
    Integer id;
    String name;
    String resource;
    String describe;

    public Permission() {
    }

    public Permission(Integer id) {
        this.id = id;
    }

    public Permission(String name) {
        this.name = name;
    }

    public Permission(String name, String resource, String describe) {
        this.name = name;
        this.resource = resource;
        this.describe = describe;
    }

    public Permission(Integer id, String name, String resource, String describe) {
        this.id = id;
        this.name = name;
        this.resource = resource;
        this.describe = describe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(resource, that.resource) && Objects.equals(describe, that.describe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, resource, describe);
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", resource='" + resource + '\'' +
                ", describe='" + describe + '\'' +
                '}';
    }
}
