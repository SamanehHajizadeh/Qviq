package com.springboot.Qviq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.Qviq.model.Info;
import com.springboot.Qviq.model.Result;
import com.springboot.Qviq.repository.ResultRepository;
import com.springboot.Qviq.service.IInfoService;
import com.springboot.Qviq.repository.InfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Hashtable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


@RestController
public class LogsController {
    private final static Logger log = Logger.getLogger(LogsController.class.getName());

    @Autowired
    private IInfoService service;

    @Autowired
    private InfoRepository repository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private Environment env;


    @GetMapping(value = "/ping")
    public ResponseEntity<Object> getPong() {
        return new ResponseEntity<>("Pong", HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public ResponseEntity<Object> getHello() {
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie().toString())
                .body("Hello World!");
    }

    @RequestMapping(value = "/getLog/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getLog(@PathVariable("id") int logId) {
        return new  ResponseEntity<>(service.getLog(logId), HttpStatus.OK);
    }

    @PutMapping("/addMessage")
    ResponseEntity<Object> updateLogByAddingMessage(@RequestParam("logId") int logId,
                                                    @RequestParam("name") String name,
                                                    @RequestParam("message") String message) {

        Info save = repository.save(service.updateLogByAddingMessage(name, logId, message));
        return ResponseEntity.ok().body(save);
    }
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllLogs() {
        return new  ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/NewMessage", method = RequestMethod.POST)
    public ResponseEntity<Object> addNewMessage(@RequestBody Info message) {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie().toString())
                .body(repository.save(service.addNewMessage(message)));
    }

    @GetMapping(value = "/max_age_message/{id}")
    public ResponseEntity<String> max_age_(@PathVariable("id") Long id) {

        long ageOfMessage = service.findMaxAgeOfMessage(id);

        String daysBetween =""+(TimeUnit.SECONDS.convert(ageOfMessage, TimeUnit.MILLISECONDS));

        return new ResponseEntity<>(daysBetween, HttpStatus.OK);
    }

    @GetMapping(value = "/set_max_age/{max_age}")
    public ResponseEntity<Object> allMessagesLessThanMaxAge() {
        String max_age = env.getProperty("max_age");
        System.out.println(max_age);
        return  ResponseEntity
                .ok()
                .body(service.showMessagesLessAnMaxAge(Integer.valueOf(max_age)));
    }

    @DeleteMapping("/Message/{id}")
    void deleteLog(@PathVariable Long id) {
        repository.deleteById(id);
    }


    @GetMapping("/read-spring-cookie")
    public String readCookie(
            @CookieValue(name = "user-id", defaultValue = "default-user-id") String userId) {
        return userId;
    }


    public ResponseCookie cookie() {
        String max_age = env.getProperty("max_age");
        return
               ResponseCookie
                        .from("heroku-nav-data", "nav_data")
                        .httpOnly(true)
                        .maxAge(Long.valueOf(max_age))
                        .path("/")
                        .build();
    }

    /*
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Object> getLogByCache() {

        String max_age = env.getProperty("max_age");
        System.out.println(max_age);

        Hashtable<String, Object> hash = service.getLogByCache(Long.valueOf(max_age));
        ObjectMapper mapper = new ObjectMapper();
        Result result = mapper.convertValue(hash, Result.class);

//        HttpHeaders headers = new HttpHeaders();
//        headers.setCacheControl("max-age=600");

        Long maxAge_limit = (Long) hash.get("maxAge_limit");
        System.out.println(maxAge_limit);
        System.out.println(maxAge_limit);
        Result save = resultRepository.save(result);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie().toString())
                .cacheControl(CacheControl.maxAge(maxAge_limit, TimeUnit.SECONDS))
                .body(save);
    }
*/
}

