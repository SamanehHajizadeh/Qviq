package com.springboot.Qviq.service.imp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.Qviq.model.Info;
import com.springboot.Qviq.exception.InfoNotFoundException;
import com.springboot.Qviq.repository.InfoRepository;
import com.springboot.Qviq.exception.InfoUnSupportedFieldPatchException;
import com.springboot.Qviq.service.IInfoService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;


/*
ConcurrentHashMap OR Hashtable: Alternatively to synchronized collections, we can use concurrent collections to create thread-safe collections.
   */
@Service
public class InfoService implements IInfoService {

    private final static Logger log = Logger.getLogger(InfoService.class.getName());

    @Autowired
    private InfoRepository repository;

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
    public List<Info> findAll() {
        return repository.findAll();
    }

    @Override
    public Info getLog(int logId) {
        if (repository.findById(Long.valueOf(logId)).isPresent() == false)
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "404"
            );
        return repository.findById(Long.valueOf(logId))
                .orElseThrow(() -> new InfoNotFoundException(Long.valueOf(logId)));
    }

    @Override
    public Info updateLogByAddingMessage(String name, int logId, String message) {
        Hashtable<String, String> updateMap = new Hashtable<>();
        ObjectMapper mapper = new ObjectMapper();
        return repository.findById(Long.valueOf(logId))
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
        Info message = repository.findById(id)
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

        List<Info> messages = repository.findAll()
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
            repository.delete(oldMessage);
        }
    }

    public List<Info> older_than_max_age_(Long max_age) {
        System.out.println("max_age: " + max_age);

        List<Info> messages =
                repository
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

    public Hashtable<String, Object> getLogByCache(Long max_age) {
        int countOfMessages = 0;

        List<Info> allLogs = findAll();
        if (allLogs.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "404"
            );
        int countOfLogs = allLogs.size();
        log.info("**********List Size*********" + countOfLogs);

        for (Info infoLog : allLogs) {

            if (infoLog.getMessageContent() != null)
                countOfMessages++;
        }
        log.info("**********Count of messages*********" + countOfMessages);

        Hashtable<String, Object> hash = new Hashtable<>();
        hash.put("current_number_of_stored_logs", countOfLogs);
        hash.put("maxAge_limit", max_age);
        hash.put("total_number_of_messages", countOfMessages);
        hash.put("average_number_of_messages_per_log", countOfMessages / countOfMessages);

        return hash;
    }
}
