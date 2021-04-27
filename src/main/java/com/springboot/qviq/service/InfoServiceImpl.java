package com.springboot.qviq.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.qviq.dto.SystemStatusDTO;
import com.springboot.qviq.model.Info;
import com.springboot.qviq.exception.InfoNotFoundException;
import com.springboot.qviq.model.MessageConfig;
import com.springboot.qviq.repository.InfoRepository;
import com.springboot.qviq.exception.InfoUnSupportedFieldPatchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/*
ConcurrentHashMap OR Hashtable: Alternatively to synchronized collections, we can use concurrent collections to create thread-safe collections.
   */
@Slf4j
@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {

    private final MessageConfigService messageConfigService;

    private final InfoRepository infoRepository;

    @Override
    public Info addNewMessage(Info message) {

//        String userName = configure_();
        ConcurrentHashMap<String, Object> hash = new ConcurrentHashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        Info message1 = new Info();
        try {
            hash.put("name", message.getName());
//            hash.put("logId", String.valueOf(logId));
            hash.put("messageContent", message.getMessageContent());
            hash.put("date", message.getDate() != null ? message.getDate() : new Date());

            //Convert Map to JSON
            message1 = mapper.convertValue(hash, Info.class);

            //Print JSON output
            System.out.println(message1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message1;
    }

    @Override
    public Info getLog(int logId) {
        if (infoRepository.findById(Long.valueOf(logId)).isPresent() == false)
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "404"
            );
        return infoRepository.findById(Long.valueOf(logId))
                .orElseThrow(() -> new InfoNotFoundException(Long.valueOf(logId)));
    }

    @Override
    public Info updateLogByAddingMessage(String name, int logId, String message) {
        Hashtable<String, String> updateMap = new Hashtable<>();
        ObjectMapper mapper = new ObjectMapper();
        return infoRepository.findById(Long.valueOf(logId))
                .map(x -> {
//                    String messageContent = updateMap.get("messageContent");
                    if (!StringUtils.isEmpty(message)) {
                        updateMap.put("messageContent", message);
                        updateMap.put("name", name);
                        updateMap.put("logId", String.valueOf(logId));
//                        updateMap.put("messageContent", information.getMessageContent().isEmpty() ? message : information.getMessageContent() + message);
                        updateMap.put("date", LocalDateTime.now().toString());

                        //Convert Map to JSON
                        // String json = mapper.writeValueAsString(hashmap);
                        x = mapper.convertValue(updateMap, Info.class);
                        // better create a custom method to update a value = :newValue where id = :id
                        return x;
                    } else {
                        throw new InfoUnSupportedFieldPatchException(updateMap.keySet());
                    }
                })
                .orElseGet(() -> {
                    throw new RuntimeException();
//                    throw new InfoUnSupportedFieldPatchException();
                });
    }


    public long findMaxAgeOfMessage(long id) {
        Info message = infoRepository.findById(id)
                .orElseThrow(() -> new InfoNotFoundException(id));

        Date dateMessage = message.getDate();
        if ((dateMessage.equals(null)))
            throw new NullPointerException("message_Date is null");

        System.out.println("message_Date: " + dateMessage);

        Date date2 = new Date();
        System.out.println("NOW: " + date2);


        long diffResult = new Date().getTime() - dateMessage.getTime();
        System.out.println(diffResult + "********MaxAgeOfMessage**********");

        return diffResult;

    }


    public List<Info> showMessagesLessAnMaxAge(Integer max_age) {
        System.out.println("max_age: " + max_age);

        List<Info> messages = infoRepository.findAll()
                .stream()
                .filter(x ->
                        Long.compare(findMaxAgeOfMessage(x.getLogId()), max_age) == -1)
                .collect(Collectors.toList());
        return messages;
    }

    public void deleteLogsOlder_thanMaxAge(Long max_age) {

        System.out.println(max_age);
        List<Info> oldMessages = older_than_max_age_(Long.valueOf(max_age));
        System.out.println("We have " + oldMessages + "Old Messages!");
        for (Info oldMessage : oldMessages) {
            System.out.println("Old Messages " + oldMessage.getLogId() + "Will be Deleted! " + oldMessage.getDate());
            infoRepository.delete(oldMessage);
        }
    }

    public List<Info> older_than_max_age_(Long max_age) {
        System.out.println("max_age: " + max_age);

        List<Info> messages =
                infoRepository
                        .findAll()
                        .stream()
                        .filter(x -> x.getDate() != null)
                        .filter(x ->
                                Long.compare(findMaxAgeOfMessage(x.getLogId()), max_age) == -1)
                        .collect(Collectors.toList());
        return messages;
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
}
