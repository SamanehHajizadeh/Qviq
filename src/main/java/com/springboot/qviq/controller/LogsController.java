package com.springboot.qviq.controller;

import com.springboot.qviq.dto.SystemStatusDTO;
import com.springboot.qviq.model.Info;
import com.springboot.qviq.repository.InfoRepository;
import com.springboot.qviq.service.InfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.MappedSuperclass;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;


@Slf4j
@RestController
@RequiredArgsConstructor
public class LogsController {

    private final InfoService messageService;

    private final InfoRepository repository;

    private final Environment env;

    @GetMapping(value = "/ping")
    public String getPong() {
        return "Pong";
    }

    @GetMapping(value = "/")
    public ResponseEntity<String> getHello(HttpServletRequest request, HttpServletResponse response) {
        response.addCookie(new Cookie("name", "samane"));
        return ResponseEntity
                .ok()
                .body("Hello World!");
    }

    @GetMapping(value = "/log/{id}")
    public Info getLog(@PathVariable("id") final int logId) {
        return messageService.getLog(logId);
    }

    @PutMapping("/message")
    public ResponseEntity<Object> updateLogByAddingMessage(@RequestParam("logId") final int logId,
                                                           @RequestParam("name") final String name,
                                                           @RequestParam("message") final String message) {

        Info save = repository.save(messageService.updateLogByAddingMessage(name, logId, message));
        return ResponseEntity.ok().body(save);
    }

    @RequestMapping(value = "/NewMessage", method = RequestMethod.POST)
    public ResponseEntity<Object> addNewMessage(@RequestBody Info message) {
        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, cookie().toString())
                .body(repository.save(messageService.addNewMessage(message)));
    }

    @GetMapping(value = "/messageMaxAge/{id}")
    public ResponseEntity<String> getMessagesMaxAge(@PathVariable("id") Long id) {

        long ageOfMessage = messageService.findMaxAgeOfMessage(id);

        String daysBetween = "" + (TimeUnit.SECONDS.convert(ageOfMessage, TimeUnit.MILLISECONDS));

        return new ResponseEntity<>(daysBetween, HttpStatus.OK);
    }

    @GetMapping(value = "/MessagesLessThanMaxAge")
    public ResponseEntity<Object> allMessagesLessThanMaxAge() {
        return ResponseEntity
                .ok()
                .body(messageService.getMessagesLessThanMaxAge());
    }

    @DeleteMapping("/deleteOldMessages")
    void deleteMessagesOlderThanMaxAge() {
        messageService.deleteMessagesOlderThanMaxAge();
    }

    @DeleteMapping("/Message/{id}")
    void deleteLog(@PathVariable Long id) {
        repository.deleteById(id);
    }


    public String readCookie(
            @CookieValue(name = "user-id", defaultValue = "default-user-id") String userId) {
        return userId;
    }

    @GetMapping(value = "/system-status")
    public SystemStatusDTO getSystemStatus() {
        return messageService.getSystemStatus();
    }
}

