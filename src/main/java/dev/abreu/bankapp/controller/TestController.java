package dev.abreu.bankapp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(path = "/test")
public class TestController {

    private static final Logger log = LogManager.getLogger(TestController.class);

    @GetMapping()
    public String test() {
        log.info("Performing GET method to test connection to database...");
        return "Connection to database successful!";
    }
}
