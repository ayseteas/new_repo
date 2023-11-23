package com.project.schoolmanagment.security.jwt;

import com.project.schoolmanagment.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            //1- get JWT from header
            String jwt = parseJwt(request);
            //2- validate JWT
            if (jwt != null && jwtUtils.validateJwt(jwt)) {
                //3 we need username
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                //4 check DB if we have user like that and extend it to UserDetails
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //5- we are setting the username info into the username attribute
                request.setAttribute("username", username);
                //6- we have to inform security context about the logged in users
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (UsernameNotFoundException e){
            LOGGER.error("Cannot set user authentication", e);
        }
        filterChain.doFilter(request, response);
    }


    private String parseJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7);
        }
        LOGGER.error("Header does not contain JWT info");
        return null;
    }
}
