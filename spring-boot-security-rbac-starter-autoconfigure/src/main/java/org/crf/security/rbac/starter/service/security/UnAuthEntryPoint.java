package org.crf.security.rbac.starter.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.crf.security.rbac.starter.model.RestResult;
import org.crf.security.rbac.starter.model.RestResultUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author crf
 */
public class UnAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        RestResult<Object> failed = RestResultUtils.failed(403, exception.getMessage());
        out.write(new ObjectMapper().writeValueAsString(failed));
        out.flush();
        out.close();
    }
}
