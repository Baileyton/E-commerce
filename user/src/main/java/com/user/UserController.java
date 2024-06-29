package com.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    Environment env;

    @Autowired
    public UserController(Environment env) {
        this.env = env;
    }

    @GetMapping
    public String user() {
        return "Welcome to the User service.";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signUp Page";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("user") String header) {
        log.info(header);
        return "Welcome to the user service.";
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("Server port = {}", request.getServerPort());
        return String.format("This is a message from User Service on PORT %s",
                env.getProperty("local.server.port"));
    }
}
