package com.springboot.Qviq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;


@RestController
public class Controller   {
    private final static Logger log = Logger.getLogger(Controller.class.getName());

    @Autowired
    private IInfoService service;

    @GetMapping(value = "/ping")
    public ResponseEntity<Object> getPong() {
        return new ResponseEntity<>("Pong", HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public ResponseEntity<Object> getHello() {
          return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @RequestMapping(value = "/addMessage", method = RequestMethod.PUT)
    public ResponseEntity<Object> addMessageToLog(@RequestParam("logId") int logId,
                                             @RequestParam("name") String name,
                                             @RequestParam("message") String message){
        Info info = service.addMessage(name, logId, message);
        return new ResponseEntity<>(info, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/getLog/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getLog(@PathVariable("id") int logId) {
        return new  ResponseEntity<>(service.getLog(logId), HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllLogs() {
        return new  ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }





}

