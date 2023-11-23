package com.project.schoolmanagment.security.config;

import com.project.schoolmanagment.security.jwt.AuthEntryPointJwt;
import com.project.schoolmanagment.security.jwt.AuthTokenFilter;
import com.project.schoolmanagment.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthEntryPointJwt unauthorizedHandler;

    /**
     * main configuration class for spring security
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                //we are specifying the exception handling for unauthorized login try
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                //we are not keeping the session info in backend.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //we are disabling security for some URL.s
                .authorizeRequests().antMatchers(AUTH_WHITE_LIST).permitAll()
                //The rest URL.s will be authenticated
                .anyRequest().authenticated();

        //when you logged in other requests should come from the same origin
        http.headers().frameOptions().sameOrigin();

        //injecting the authentication provider that we will use
        http.authenticationProvider(daoAuthenticationProvider());


        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * this will be our authentication manager in spring security
     *
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }



    /**
     *
     * @return our own token filter class that exist in jwt package
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer(){
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**")
                        // all origins are allowed
                        .allowedOrigins("*")
                        // all header contents are allowed
                        .allowedHeaders("*")
                        //POST - GET - PUT.... every request type is allowed
                        .allowedMethods("*");
            }
        };
    }

    /**
     * these URL.s will be out of the scope of security
     */
    private static final String[] AUTH_WHITE_LIST = {
            "/v3/api-docs/**",
            "swagger-ui.html",
            "/swagger-ui/**",
            "/user/save/*",
            "index.html",
            "/images/**",
            "/css/**",
            "/js/**",
            "/contactMessages/save",
            "/auth/login"
    };
}
