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
        //Check if the request URI does not contain "classname"
        if(!request.getRequestURI().contains("message")){
            //If not, redirect to "/login.html"
            response.sendRedirect("/login.html");
        }else{
            //If yes, send HTTP error response with status code 401 (UNAUTHORIZED)
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
