package io.github.churunfa.security.rbac.starter.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.churunfa.security.rbac.starter.dao.RbacUserSecurityDao;
import io.github.churunfa.security.rbac.starter.properties.RbacProperties;
import io.github.churunfa.security.rbac.starter.service.rbac.RbacService;
import io.github.churunfa.security.rbac.starter.user.RbacUser;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author crf
 */

@Service
public class DefaultAuAuthRule {

    private final RbacProperties rbacProperties;

    private final RbacUserSecurityDao rbacUserSecurityDao;

    private final RbacService rbacService;

    public DefaultAuAuthRule(RbacProperties rbacProperties, RbacUserSecurityDao rbacUserSecurityDao, RbacService rbacService) {
        this.rbacProperties = rbacProperties;
        this.rbacUserSecurityDao = rbacUserSecurityDao;
        this.rbacService = rbacService;
    }
    
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        RbacUser user = (RbacUser) authentication.getPrincipal();
        if (user == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_root"))) {
            return true;
        }

        String uri = request.getRequestURI();


        Class<? extends RbacUser> clz = user.getClass();

        String httpMethod = request.getMethod();

        for (GrantedAuthority authority : authorities) {
            String p = authority.getAuthority();
            if (p.startsWith("ROLE_")) {
                continue;
            }

            if (p.startsWith("GET:") && !"GET".equalsIgnoreCase(httpMethod)) {
                continue;
            }

            if (p.startsWith("POST:") && !"POST".equalsIgnoreCase(httpMethod)) {
                continue;
            }

            if (p.startsWith("PUT:") && !"PUT".equalsIgnoreCase(httpMethod)) {
                continue;
            }

            if (p.startsWith("DELETE:") && !"DELETE".equalsIgnoreCase(httpMethod)) {
                continue;
            }

            String[] split = p.split(":");

            String pUrl = split[split.length - 1];

            String[] pUrlSplit = pUrl.split("\\?");

            String pUri = pUrlSplit[0];


            pUri = getRealStr(pUri, clz, user);

            if (!pUri.startsWith("/")) {
                pUri = rbacProperties.getUriPrefix() + pUri;
            }

            if (!matcher(uri, pUri)) {
                continue;
            }

            if (pUrlSplit.length == 1) {
                return true;
            }

            String paramStr = pUrlSplit[1];

            paramStr = getRealStr(paramStr, clz, user);

            Map<String, String> map = paramParse(paramStr);


            boolean isJson = false;
            Map<String, Object> loginData = null;
            if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getContentType()) || MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getContentType()) || MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(request.getContentType())) {
                isJson = true;
                try {
                    loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                } catch (IOException e) {}
            }

            boolean flag = true;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String parameter = null;

                if (isJson && loginData != null && loginData.get(key) != null) {
                    parameter = loginData.get(key).toString();
                }

                if (parameter == null) {
                    parameter = request.getParameter(key);
                }
                if (parameter != null) {
                    parameter = parameter.trim();
                    if (parameter.length() == 0) {
                        parameter = null;
                    }
                }
                if (value == null) {
                    if (parameter != null) {
                        continue;
                    }
                    flag = false;
                    break;
                }

                if (!value.equals(parameter)) {
                    flag = false;
                    break;
                }

            }
            if (flag) {
                return true;
            }

        }
        return false;
    }

    public String getRealStr(String oleStr, Class<? extends RbacUser> clz, Object user) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String pattern = "\\{(\\w+)}";

        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(oleStr);;

        while (matcher.find()) {
            String fieldName = matcher.group(1);

            String str = getStr(fieldName, clz, user);

            String regex = "\\{" + fieldName + "}";

            oleStr = oleStr.replaceAll(regex, str);
        }
        return oleStr;
    }

    public String getStr(String fieldName, Class<? extends RbacUser> clz, Object user) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Field field = null;

        for (Class<?> c = clz; c != Object.class; c = c.getSuperclass()) {
            try {
                field = c.getDeclaredField(fieldName);
                break;
            }catch (NoSuchFieldException e) {
                continue;
            }
        }

        if (field == null) {
            throw new NoSuchFieldException( rbacProperties.getUserTableName() +  "中没有名为" + fieldName + "的字段");
        }

        field.setAccessible(true);
        String typeName = field.getType().getTypeName();

        StringBuilder sb = new StringBuilder();
        if (boolean.class.getTypeName().equals(typeName)) {
            sb.append("is");
        } else {
            sb.append("get");
        }

        sb.append(fieldName.substring(0,1).toUpperCase()).append(fieldName.substring(1));

        Method method = null;

        for (Class<?> c = clz; c != Object.class; c = c.getSuperclass()) {
            try {
                method = c.getDeclaredMethod(sb.toString());
                break;
            }catch (NoSuchMethodException e) {
                continue;
            }
        }

        if (method == null) {
            throw new NoSuchMethodException("没有找到" + sb + "方法");
        }
        method.setAccessible(true);
        return method.invoke(user).toString();
    }

    public boolean matcher(String uri, String pUri) {

        String[] uriSplit = uri.split("/");
        String[] pUriSplit = pUri.split("/");

        int i = 0;

        for (int j = 0; j < pUriSplit.length && i < uriSplit.length; j++) {
            if (!pUriSplit[j].equals("*") && !pUriSplit[j].equals("**") && !pUriSplit[j].equals(uriSplit[i])) {
                return false;
            }
            if ("*".equals(pUriSplit[j]) || pUriSplit[j].equals(uriSplit[i])) {
                i++;
                continue;
            }

            if ("**".equals(pUriSplit[j])) {
                if (j == pUriSplit.length - 1) {
                    return true;
                }
                j++;
            }

            if ("*".equals(pUriSplit[j]) || "**".equals(pUriSplit[j])) {
                throw new RuntimeException(pUri + "解析错误，通配符不可以连续使用");
            }

            boolean flag = false;
            while(i < uriSplit.length) {
                if (pUriSplit[j].equals(uriSplit[i])) {
                    i++;
                    flag = true;
                    break;
                }
                i++;
            }
            if (!flag) {
                return false;
            }
        }

        return i == uriSplit.length;
    }

    public Map paramParse(String paramStr) {
        Map<String, String> map = new HashMap<>();
        for (String s : paramStr.split("&")) {
            String[] split = s.split("=");
            if (split.length == 1) {
                map.put(split[0], null);
            } else {
                map.put(split[0], split[1]);
            }
        }
        return map;
    }

}
