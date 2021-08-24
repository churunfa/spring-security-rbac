package io.github.churunfa.security.test.rbac;

import io.github.churunfa.security.rbac.starter.service.security.DefaultAuAuthRule;
import io.github.churunfa.security.rbac.starter.user.RbacUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class InvokeTest {
    @Test
    void test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        RbacUser user = new RbacUser("root", "123456", true, true, true, true);

        Class<? extends RbacUser> clz = user.getClass();

        Field enabled = clz.getDeclaredField("enabled");
        enabled.setAccessible(true);

        System.out.println(enabled.getType().getTypeName());
        System.out.println(boolean.class.getTypeName());

        System.out.println(enabled.getType().getTypeName().equals(boolean.class.getTypeName()));

//        Method getUsername = clz.getDeclaredMethod("getUsername");
//        getUsername.setAccessible(true);
//        Object invoke = getUsername.invoke(user);
//        System.out.println(invoke);

    }

    @Autowired
    DefaultAuAuthRule defaultAuAuthRule;

    @Test
    void getStrTest() throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        RbacUser user = new RbacUser("root", "123456", true, true, true, true);

        class MyUser extends RbacUser {
            private String pp;

            public MyUser(String username, String password, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, String pp) {
                super(username, password, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
                this.pp = pp;
            }

            public String getPp() {
                return pp;
            }

            public void setPp(String pp) {
                this.pp = pp;
            }
        }

        MyUser myUser = new MyUser("root", "123456", true, false, false, true, "哈哈");


//        Field accountNonExpired = myUser.getClass().getDeclaredField("accountNonExpired");
//        System.out.println(accountNonExpired);

        String accountNonExpired = defaultAuAuthRule.getStr("username", myUser.getClass(), myUser);

        System.out.println(accountNonExpired);
    }

    @Test
    void uriTest() {
        String uri = "/user/{id}/{user_id}/{a123}";

        String pattern = "\\{(\\w+)\\}";

        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(uri);;
        System.out.println(matcher);
        while (matcher.find()) {
            String group = matcher.group(1);
            String regex = "\\{" + group + "\\}";
            uri = uri.replaceAll(regex, "1");
        }
        System.out.println(uri);
    }

    @Test
    void matcherTest() {
        boolean matcher = defaultAuAuthRule.matcher("/admin/1/user/a.do", "/admin/*/a.do");
        System.out.println(matcher);

        System.out.println(defaultAuAuthRule.matcher("/admin/1/user/abc", "/admin/**/abc"));
    }

    @Test
    void parseParam() {
        System.out.println(defaultAuAuthRule.paramParse("abc&name=1"));
    }

}
