package com.springboot.qviq.controller;

import com.springboot.qviq.model.MessageConfig;
import com.springboot.qviq.service.MessageConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MessageConfigController {

    private final MessageConfigService messageConfigService;

    @PostMapping(value = "maxAge/{maxAge}")
    public void updateMaxAge(@PathVariable final int maxAge) {
        messageConfigService.updateMaxAge(maxAge);
    }

}
