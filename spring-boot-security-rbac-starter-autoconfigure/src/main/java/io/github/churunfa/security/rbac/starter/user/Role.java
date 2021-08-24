package io.github.churunfa.security.rbac.starter.user;

/**
 * @author crf
 */
public class Role {
    Integer id;
    String name;
    String resource;
    String describe;

    public Role() {
    }

    public Role(Integer id) {
        this.id = id;
    }

    public Role(String name, String resource, String describe) {
        this.name = name;
        this.resource = resource;
        this.describe = describe;
    }

    public Role(Integer id, String name, String resource, String describe) {
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
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", resource='" + resource + '\'' +
                ", describe='" + describe + '\'' +
                '}';
    }
}
