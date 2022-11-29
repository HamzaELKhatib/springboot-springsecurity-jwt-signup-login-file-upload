package com.rest.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        // 1. Retrieve the header from the request (specifically the Authorization header)
        String header = request.getHeader(SecurityConstants.HEADER_STRING);

        // 2. If the header is not present, or it does not start with "Bearer " return nothing
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        // 3. If the header is present, and the token does start with "Bearer " we apply getAuthentication method
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);

        // 7. If the authentication is not null, we set the authentication in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 8. We continue the filter chain
        chain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // Retrieve the header from the request (specifically the Authorization header)
        String token = request.getHeader(SecurityConstants.HEADER_STRING);

        if (token != null) {
            // 4. If the token is valid, we create a new UsernamePasswordAuthenticationToken object
            // and pass it to the SecurityContextHolder
            String user = Jwts.parser()
                    .setSigningKey(SecurityConstants.getTokenSecret())
                    // 5. Removing the "Bearer " prefix from the token
                    .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            if (user != null) {
                // 6. If the token is valid, we create a new UsernamePasswordAuthenticationToken object and pass it to the SecurityContextHolder
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
