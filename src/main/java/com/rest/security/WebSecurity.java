package com.rest.security;

import com.rest.service.UserService;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().authorizeRequests()
                // We're permitting all post requests to the path "/users"
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/swagger-ui/**", "/webjars/**").permitAll()
                // We're requiring all other requests to be authenticated
                .anyRequest().authenticated()
                 /*
                 This will implement a custom authentication filter which is the JWT filter we created in the AuthenticationFilter class.
                 This line used to be .and().addFilter(new AuthenticationFilter(authenticationManager()))
                 */
                // We're calling the getAuthenticationFilter() method that we just created in this class to get the custom filter.
                .and().addFilter(getAuthenticationFilter())
                // We're calling the getAuthorizationFilter() method that we just created in this class to get the custom filter.
                .addFilter( new AuthorizationFilter(authenticationManager()))
                // This will stop spring security from using cookies and every request needs to be authenticated
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        // To help us authenticate users, we need to tell Spring Security how to load user details from the database.
        // userDetailsService is a AuthenticationManagerBuilder method that uses the UserDetailsService interface to load user details from the database
        // To help load user details from the database, we need to tell Spring Security how to encode and decode passwords.
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    public AuthenticationFilter getAuthenticationFilter() throws Exception {

        final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
        // The method setFilterProcessesUrl() is used to set the URL that the filter will process (we're basically creating a custom login endpoint "/users/login")
        filter.setFilterProcessesUrl("/users/login");
        return filter;
    }
}
