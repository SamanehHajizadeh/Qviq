package com.springboot.Qviq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


@RestController
public class Controller   {
    private final static Logger log = Logger.getLogger(Controller.class.getName());

    @Autowired
    private IInfoService service;

    @Autowired
    private InfoRepository repository;

    @GetMapping("/read-spring-cookie")
    public String readCookie(
            @CookieValue(name = "user-id", defaultValue = "default-user-id") String userId) {
        return userId;
    }

    @GetMapping(value = "/ping")
    public ResponseEntity<Object> getPong() {
        return new ResponseEntity<>("Pong", HttpStatus.OK);
    }

//    @GetMapping(value = "/")
//    public ResponseEntity<Object> getHello() {
//        return ResponseEntity
//                .ok()
//                .header(HttpHeaders.SET_COOKIE, service.createCookie().toString())
//                .body("Hello World!");
//    }

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

    @GetMapping(value = "/max_age_message/{id}")
    public ResponseEntity<String> max_age_(@PathVariable("id") Long id) {

        long ageOfMessage = service.findMaxAgeOfMessage(id);

        String daysBetween =""+(TimeUnit.SECONDS.convert(ageOfMessage, TimeUnit.MILLISECONDS));

        return new ResponseEntity<>(daysBetween, HttpStatus.OK);
    }

    @GetMapping(value = "/allMessagesLessThan_max_age/{max_age}")
    public ResponseEntity<Object> max_age_(@PathVariable() Integer max_age) {
        return  ResponseEntity
                .ok()
                .body(service.max_age_(max_age));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Object> getLogByCache(){

        Hashtable<String, Object> hash = service.getLogByCache();
        ObjectMapper mapper = new ObjectMapper();
        Result result = mapper.convertValue(hash, Result.class);

//        CacheControl cacheControl =
//                CacheControl.maxAge(max_age, TimeUnit.SECONDS)
//         .noTransform()
//         .cachePrivate();

//        HttpHeaders headers = new HttpHeaders();
//        headers.setCacheControl("max-age=600");

        Long maxAge_limit = (Long) hash.get("maxAge_limit");
        System.out.println(maxAge_limit);
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(maxAge_limit, TimeUnit.SECONDS))
                .body(result);
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

