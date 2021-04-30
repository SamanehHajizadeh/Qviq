package com.springboot.qviq.controller;

import com.springboot.qviq.model.MessageConfig;
import com.springboot.qviq.service.MessageConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/age")
public class MessageConfigController {

    private final MessageConfigService messageConfigService;

    @PostMapping(value = "/maxAge/{max}")
    public void updateMaxAge(@PathVariable final int max) {
        messageConfigService.updateMaxAge(max);
    }

    @GetMapping(value = "/maxAge")
    public void getMaxAge() {
        messageConfigService.getMessageConfig().stream().findFirst();
    }
}
