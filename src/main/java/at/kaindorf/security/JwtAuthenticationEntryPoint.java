package at.kaindorf.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        if(!request.getRequestURI().contains("message")){
            response.sendRedirect("/login.html");
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

        /*
        log.debug(request.getRequestURI());
        if(request.getRequestURI().contains("examView")){
            log.debug(request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }else{
            response.sendRedirect("/login.html");
        }

         */

    }

}
