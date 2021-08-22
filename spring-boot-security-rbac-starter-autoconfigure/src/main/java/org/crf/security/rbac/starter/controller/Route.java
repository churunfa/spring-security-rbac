package org.crf.security.rbac.starter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author crf
 */
@Controller
public class Route {
    @GetMapping("/rbac/**")
    public String rbac() {
        return "/dist/index.html";
    }

    @GetMapping("/rbac/assets/{filename}")
    public String assets(@PathVariable("filename") String filename) {
        return "/dist/assets/" + filename;
    }

}
