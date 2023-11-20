package com.kerdagreat.authserverspringweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class AuthServerSpringWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerSpringWebApplication.class, args);
    }

    @GetMapping("/")
    public String message() {
        return "HELLO AUTHORIZATION SERVER";
    }

}
