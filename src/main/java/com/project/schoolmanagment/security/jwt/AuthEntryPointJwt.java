package com.project.schoolmanagment.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will be responsible for possible authentication errors handling
 */

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    /**
     *
     * @param request that resulted in an <code>AuthenticationException</code>
     * @param response so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LOGGER.error("Unauthorized error : {}", authException.getMessage());
        //We are specifying the content type of the response
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //We are specifying the response status (401)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //We are specifying the body for our custom response in case of error
        final Map<String,Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error type", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), body);

    }
}
