package com.springboot.Qviq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;


/*
ConcurrentHashMap OR Hashtable: Alternatively to synchronized collections, we can use concurrent collections to create thread-safe collections.
   */
@Service
public class InfoService implements IInfoService {

    @Autowired
    private InfoRepository repository;

    @Override
    public Info addNewMessage(Info message) {

//        String userName = configure_();
        Hashtable<String, Object> hash = new Hashtable<>();
        ObjectMapper mapper = new ObjectMapper();
        Info message1 = new Info();
        try {
            hash.put("name", message.getName());
//            hash.put("logId", String.valueOf(logId));
            hash.put("messageContent", message.getMessageContent());
            hash.put("date", LocalDateTime.now().toString());

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
    public Info getLog(int logId)  {
        if(repository.findById(Long.valueOf(logId)).isPresent() == false)
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "404"
            );
        return  repository.findById(Long.valueOf(logId))
                .orElseThrow(() -> new InfoNotFoundException(Long.valueOf(logId)));
    }

    @Override
    public Info updateLogByAddingMessage(String name, int logId, String message){
        Hashtable<String, String> updateMap = new Hashtable<>();
        ObjectMapper mapper = new ObjectMapper();
        return repository.findById(Long.valueOf(logId))
                .map(x -> {
//                    String messageContent = updateMap.get("messageContent");
                    if (!StringUtils.isEmpty(message)) {
                        updateMap.put("messageContent", message );
                        updateMap.put("name", name);
                        updateMap.put("logId", String.valueOf(logId));
//                        updateMap.put("messageContent", information.getMessageContent().isEmpty() ? message : information.getMessageContent() + message);
                        updateMap.put("date", LocalDateTime.now().toString());

                        //Convert Map to JSON
                        // String json = mapper.writeValueAsString(hashmap);
                        x = mapper.convertValue(updateMap, Info.class);
                        // better create a custom method to update a value = :newValue where id = :id
                        return x;
                    }else {
                        throw new InfoUnSupportedFieldPatchException(updateMap.keySet());
                    }
                })
                .orElseGet(() -> {
                    throw new RuntimeException();
//                    throw new InfoUnSupportedFieldPatchException();
                });
    }
}
