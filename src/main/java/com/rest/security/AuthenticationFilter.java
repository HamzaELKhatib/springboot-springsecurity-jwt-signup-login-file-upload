package com.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.SpringApplicationContext;
import com.rest.presentationlayer.model.request.UserLoginRequestModel;
import com.rest.service.UserService;
import com.rest.service.impl.UserServiceImpl;
import com.rest.shared.dto.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    // This method is triggered when a user tries to login
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {

        try{

            // 1. Get credentials from request and populate UserLoginRequestModel
            // ObjectMapper().readValue(JSON, JavaClass.class) converts JSON to Java Object
            // Sidenote: ObjectMapper().writeValue(JSON, JavaClass) converts Java Object to JSON
            UserLoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), UserLoginRequestModel.class);

            // 2. Create an authentication object/token (contains credentials) which will be used by authenticationManager to authenticate the user
            // if credentials are valid, authenticationManager will return an Authentication object and trigger successfulAuthentication() method
            return authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    // 3. This method is triggered when authentication is successful
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {

        // auth.getPrincipal() returns the user object (The identity of the principal being authenticated)
        String userName = ((User) auth.getPrincipal()).getUsername();

        // 4. Create a token which will be used by the client to authenticate and Add the user details to the token
        String token = Jwts.builder()
                // 5. Set the subject of the token to the userName (the identity of the principal being authenticated)
                .setSubject(userName)
                // 6. Setting the expiration date of the token by setting the expiration date to be SecurityConstants.EXPIRATION_TIME(10 days) after the current time
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                // 7. Sign the token with the HS512 algorithm using the secret key we created in SecurityConstants
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                // 8. Build the token
                .compact()
                ;

        // Adding UserServiceImpl to the SpringApplicationContext so that we can use it in this class
        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");

        // 9. Get the user details from the database using getUser() method of UserServiceImpl (this is why we added UserServiceImpl to the SpringApplicationContext)
        UserDto userDto = userService.getUser(userName);

        // 10. Add the token to the response header(Authorization)
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);

        // 11. Add the user details to the response body
        res.addHeader("UserID", userDto.getUserId());
    }

}
