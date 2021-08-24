package io.github.churunfa.security.test.rbac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author crf
 */
@SpringBootApplication
@EnableSwagger2
public class RbacTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbacTestApplication.class, args);
    }

}
