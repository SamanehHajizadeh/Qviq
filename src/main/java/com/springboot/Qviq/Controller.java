package com.springboot.Qviq;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;


@RestController
public class Controller   {
    private final static Logger log = Logger.getLogger(Controller.class.getName());

    @GetMapping(value = "/ping")
    public ResponseEntity<Object> getPong() {
        return new ResponseEntity<>("Pong", HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public ResponseEntity<Object> getHello() {
          return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }


}

