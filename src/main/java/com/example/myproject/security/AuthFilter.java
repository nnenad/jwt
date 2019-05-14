package com.example.myproject.security;

import com.example.myproject.domain.AllUsers;
import com.example.myproject.domain.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class AuthFilter implements Filter {

    @Autowired
    private Authentication authentication;

    @Autowired
    private AllUsers allUsers;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if(request.getRequestURI().contains("login")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            if(getToken(request.getHeader("X-Auth-Token"))){
                response.setHeader("X-Auth-Token", request.getHeader("X-Auth-Token"));
                filterChain.doFilter(request, response);
                return;
            }

            response.sendError(HttpStatus.UNAUTHORIZED.value());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private boolean getToken(String token) throws Exception{
        if(token != null && !token.isEmpty() && validationToken(token)){
            return true;
        }

        return false;
    }

    private boolean validationToken(String token) throws JsonParseException, JsonMappingException, IOException {
        String user = authentication.desCompactJws(token);

        if(user.equals("Invalid Token")) return false;

        User out =  new ObjectMapper().readValue(user, User.class);

        Optional<User> userValid = allUsers.users.stream().
                filter(x-> x.getUsername().equals(out.getUsername())&& x.getPassword().equals(out.getPassword()))
                .findFirst();

        return userValid.isPresent();
    }

    @Override
    public void destroy() {

    }
}
