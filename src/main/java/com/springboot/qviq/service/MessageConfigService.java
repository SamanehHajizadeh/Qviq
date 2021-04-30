package com.springboot.qviq.service;

import com.springboot.qviq.model.MessageConfig;
import com.springboot.qviq.repository.MessageConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageConfigService {

    private final MessageConfigRepository messageConfigRepository;

    public void updateMaxAge(final int maxAge) {
        final MessageConfig messageConfig = getMessageConfig()
                .orElseGet(() -> MessageConfig.builder().build());

        messageConfig.setMaxAge(maxAge);
        messageConfigRepository.save(messageConfig);
    }

    public Optional<MessageConfig> getMessageConfig() {
        return messageConfigRepository.findAll()
                .stream()
                .findFirst();
    }

//    public void updateMaxAge(final int maxAge) {
//        MessageConfig.builder()
//                .maxAge(maxAge)
//                .build();
//    }


}
