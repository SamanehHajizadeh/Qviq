package com.springboot.qviq.service;

import com.springboot.qviq.dto.SystemStatusDTO;
import com.springboot.qviq.model.Info;
import com.springboot.qviq.exception.InfoNotFoundException;
import com.springboot.qviq.model.MessageConfig;
import com.springboot.qviq.repository.InfoRepository;
import com.springboot.qviq.exception.InfoUnSupportedFieldPatchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {

    private final MessageConfigService messageConfigService;

    private final InfoRepository infoRepository;

    @Override
    public Info addNewMessage(Info message) {
        return Info.builder()
                .name(message.getName())
                .messageContent(message.getMessageContent())
                .date(message.getDate() != null ? message.getDate() : new Date())
                .build();
    }

    @Override
    public Info getLog(int logId) {
        return infoRepository.findById(Long.valueOf(logId))
                .orElseThrow(() -> new InfoNotFoundException(Long.valueOf(logId)));
    }


    @Override
    public List<Info> findAll() {
        return infoRepository.findAll();
    }

    @Override
    public Info updateLogByAddingMessage(String name, int logId, String messageContent) {
        return infoRepository.findById(Long.valueOf(logId))
                .map(x -> {
                    if (!StringUtils.isEmpty(messageContent)) {
                        return Info.builder()
                                .name(x.getName())
                                .messageContent(x.getMessageContent() + messageContent)
                                .logId((long) x.getLogId())
                                .date(new Date())
                                .build();

                    } else {
                        throw new InfoUnSupportedFieldPatchException(logId);
                    }
                })
                .orElseGet(() -> {
                    throw new RuntimeException();
                });
    }

    @Override
    public long findMaxAgeOfMessage(long id) {
        Info message = infoRepository.findById(id)
                .orElseThrow(() -> new InfoNotFoundException(id));

        Date dateMessage = message.getDate();
        if ((dateMessage.equals(null)))
            throw new NullPointerException("message_Date is null");

        long diffResult = new Date().getTime() - dateMessage.getTime();
        log.info("message_Date: {} ", dateMessage, "Now Date : {}", new Date(), "MaxAgeOfMessage: {}" , diffResult );

        return diffResult;
    }

    @Override
    public List<Info> getMessagesLessThanMaxAge() {
        return infoRepository.findAll()
                .stream()
                .filter(x ->
                        Long.compare(findMaxAgeOfMessage(x.getLogId()),
                                messageConfigService
                                        .getMessageConfig()
                                        .get()
                                        .getMaxAge()) == -1)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMessagesOlderThanMaxAge() {
        int maxAge = messageConfigService.getMessageConfig().get().getMaxAge();
        log.info("MaxAge : {}", maxAge);

        infoRepository
                .findAll()
                .removeIf(x -> x.getDate() != null
                        && Long.compare(findMaxAgeOfMessage(x.getLogId()), maxAge) == -1);
    }

    public SystemStatusDTO getSystemStatus() {
        long countOfLogs = infoRepository.count();

        log.info("Count of logs is: {}", countOfLogs);

        final Integer maxAge = messageConfigService.getMessageConfig()
                .map(MessageConfig::getMaxAge)
                .orElse(0);

        return SystemStatusDTO.builder()
                .currentNumberOfStoredLogs(countOfLogs)
                .maxAgeLimit(maxAge)
                .totalNumberOfMessages(countOfLogs)
                .averageNumberOfMessagesPerLog(1)
                .build();
    }

    protected String configure_() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails user = (UserDetails) principal;
                username = user.getUsername();
                // User is logged in, now you can access its details
            }
        }
        return username;
    }

}
