package io.github.churunfa.security.test.rbac.pojo;

import io.github.churunfa.security.rbac.starter.user.RbacUser;

/**
 * @author crf
 */
public class User extends RbacUser {
    private String abc = "100";

    public String getAbc() {
        return abc;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }

    public User() {}

    public User(String username, String password, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        super(username, password, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
    }

    @Override
    public String toString() {
        return "User{" +
                "abc='" + abc + '\'' +
                '}';
    }
}
