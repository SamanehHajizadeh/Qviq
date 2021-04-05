package com.springboot.Qviq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.logging.Logger;


@RestController
public class Controller   {
    private final static Logger log = Logger.getLogger(Controller.class.getName());

    @Autowired
    private IInfoService service;

    @Autowired
    private InfoRepository repository;

    @GetMapping(value = "/ping")
    public ResponseEntity<Object> getPong() {
        return new ResponseEntity<>("Pong", HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public ResponseEntity<Object> getHello() {
          return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @RequestMapping(value = "/getLog/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getLog(@PathVariable("id") int logId) {
        return new  ResponseEntity<>(service.getLog(logId), HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllLogs() {
        return new  ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/NewMessage", method = RequestMethod.POST)
    public ResponseEntity<Object> addNewMessage(@RequestBody Info message) {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, getCookie().toString())
                .body(repository.save(service.addNewMessage(message)));
    }

    @PutMapping("/addMessage")
    ResponseEntity<Object> updateLogByAddingMessage(@RequestParam("logId") int logId,
                                                    @RequestParam("name") String name,
                                                    @RequestParam("message") String message) {

        Info save = repository.save(service.updateLogByAddingMessage(name, logId, message));
        return ResponseEntity.ok().body(save);
    }

    public HttpCookie getCookie(){
        return
                ResponseCookie
                        .from("heroku-nav-data", "nav_data")
//                    .maxAge(1000)
                        .path("/")
                        .build();
    }
}

