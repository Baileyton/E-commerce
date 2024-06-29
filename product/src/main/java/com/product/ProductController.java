package com.product;

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
@RequestMapping("/api/product")
public class ProductController {

    Environment env;

    @Autowired
    public ProductController(Environment env) {
        this.env = env;
    }

    @GetMapping
    public String product() {
        return "Welcome to the Product service.";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("product") String header) {
        log.info(header);
        return "Welcome to the Product service.";
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("Server port = {}", request.getServerPort());
        return String.format("This is a message from Product Service on PORT %s",
                env.getProperty("local.server.port"));
    }
}