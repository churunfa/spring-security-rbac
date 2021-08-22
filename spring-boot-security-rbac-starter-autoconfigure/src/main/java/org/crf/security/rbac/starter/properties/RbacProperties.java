package org.crf.security.rbac.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author crf
 */
@ConfigurationProperties(prefix = "security-rbac")
public class RbacProperties {

    private String rbacUserClassStr = "org.crf.security.rbac.starter.user.RbacUser";
    private String userTableName = "user";
    private boolean autoCreateRootAccount = true;
    private Long tokenExpiration = 15 * 24 * 60 * 60 * 1000L;
    private String uriPrefix = "";
    private String autoTypePath = null;
    private String[] authWhitelist = new String[0];
    private String loginUri = "/api/login";

    public boolean isAutoCreateRootAccount() {
        return autoCreateRootAccount;
    }

    public void setAutoCreateRootAccount(boolean autoCreateRootAccount) {
        this.autoCreateRootAccount = autoCreateRootAccount;
    }

    public String getRbacUserClassStr() {
        return rbacUserClassStr;
    }

    public void setRbacUserClassStr(String rbacUserClassStr) {
        this.rbacUserClassStr = rbacUserClassStr;
    }

    public String getUserTableName() {
        return userTableName;
    }

    public void setUserTableName(String userTableName) {
        this.userTableName = userTableName;
    }

    public Long getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(Long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public String getUriPrefix() {
        return uriPrefix;
    }

    public void setUriPrefix(String uriPrefix) {
        this.uriPrefix = uriPrefix;
    }

    public String getAutoTypePath() {
        return autoTypePath;
    }

    public void setAutoTypePath(String autoTypePath) {
        this.autoTypePath = autoTypePath;
    }

    public String[] getAuthWhitelist() {
        return authWhitelist;
    }

    public void setAuthWhitelist(String[] authWhitelist) {
        this.authWhitelist = authWhitelist;
    }

    public String getLoginUri() {
        return loginUri;
    }

    public void setLoginUri(String loginUri) {
        this.loginUri = loginUri;
    }

    public Class getRbacUserClass() {
        Class clz = null;
        try {
            clz = Class.forName(rbacUserClassStr);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(rbacUserClassStr + "加载失败");
        }
        return clz;
    }
}
