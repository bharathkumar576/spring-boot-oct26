package com.mahendra.demo3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class HomeController {

    private Logger log = Logger.getLogger("Home Controller");

    @GetMapping(value="/", produces = "text/plain`")
    public String goHome(){
        log.info("Going home ....");

        return "Welcome to the Home";
    }
}
