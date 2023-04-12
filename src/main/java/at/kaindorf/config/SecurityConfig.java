package at.kaindorf.config;

import at.kaindorf.security.JwtAuthenticationEntryPoint;
import at.kaindorf.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()// Enable Cross-Origin Resource Sharing (CORS) for the endpoints.
                .and()
                .csrf()// Disable Cross-Site Request Forgery (CSRF) protection.
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)// Set custom authentication entry point for handling unauthorized requests.
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)// Configure session management to use stateless sessions.
                .and()
                .authorizeHttpRequests()// Begin configuration for authorization rules for HTTP requests.
                .requestMatchers("/auth/**","/login.html","/login.js", "/messageBoard.html","/messageBoard.js", "/api/messages/all")
                .permitAll()// Allow specified request matchers to be accessed without authentication.
                .anyRequest()
                .authenticated()// Require authentication for any other requests that do not match the previous request matchers.
                .and()
                .formLogin()
                .loginPage("/login.html");// Configure form-based login authentication and specify login page URL.

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();//Return the configured HttpSecurity object as a SecurityFilterChain bean.
    }

}
