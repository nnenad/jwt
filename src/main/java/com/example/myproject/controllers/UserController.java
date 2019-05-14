package com.example.myproject.controllers;

import com.example.myproject.domain.AllUsers;
import com.example.myproject.domain.User;
import com.example.myproject.security.Authentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private Authentication authentication;

    @Autowired
    private AllUsers allUsers;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpServletResponse response, @RequestBody User user) throws IOException {
        System.out.println(user);

        String token = authentication.compactJws(new ObjectMapper().writeValueAsString(user));

        response.setHeader("X-Auth-Token", token);

        return token;
    }

    @GetMapping("/user")
    public String user(HttpServletRequest request)throws IOException {
        System.out.println(request.getHeader("X-Auth-Token"));

        return authentication.desCompactJws(request.getHeader("X-Auth-Token"));
    }

    @GetMapping("/user/login")
    public String stupidOne(HttpServletRequest request)throws IOException {

        return "Stupid one";
    }

    @PostMapping("/validation/token")
    public boolean validationToken(HttpServletRequest request) throws IOException{
        return true;
    }

    @PostMapping("/users")
    public List<User> listUsers(HttpServletRequest request, HttpServletResponse response) throws Exception{
        return allUsers.users;
    }
}
