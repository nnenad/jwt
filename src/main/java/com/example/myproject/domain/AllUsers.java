package com.example.myproject.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AllUsers {


    @SuppressWarnings("serial")
    public List<User> users = new ArrayList() {
        {
            add(new User("user1", "1"));
            add(new User("user2", "2"));
            add(new User("user3", "3"));
        }
    };
}
