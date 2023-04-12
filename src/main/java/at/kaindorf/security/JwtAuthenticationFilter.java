package at.kaindorf.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomAccountDetailsService customAccountDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Extract the JWT token from the request
        String jwt = getJwtFromRequest(request);

        //Validate the JWT token
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            //Extract user email from the JWT token
            String mail = jwtTokenProvider.getUserMailFromToken(jwt);
            request.setAttribute("mail", mail);
            //Load user details based on email
            UserDetails userDetails = customAccountDetailsService.loadUserByUsername(mail);
            //Create an authenticated user object
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            //Set additional details for the authentication
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //Set the authentication object to the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //Continue to the next filter or servlet
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        //Retrieve the "Authorization" header from the request
        String bearerToken = request.getHeader("Authorization");
        //Check if the header value is not empty and starts with "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            //Extract the JWT token from the header value
            return bearerToken.substring(7);
        }
        //Return null if no JWT token was found
        return null;
    }

}
