package com.springboot.Qviq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
ConcurrentHashMap OR Hashtable: Alternatively to synchronized collections, we can use concurrent collections to create thread-safe collections.
   */
@Service
public class InfoService implements IInfoService {

    @Autowired
    private InfoRepository repository;

//    @Override
//    public Info addMessage(String name, int logId, String message){
//        Hashtable<String,String> hash = new Hashtable<>();
//        ObjectMapper mapper = new ObjectMapper();
//        Info information = getLog(logId);
//        System.out.println("Find Log With Id: " + information);
//        try {
//            hash.put("Id", information.getId().toString());
//            hash.put("name", information.getName().isEmpty() ? name : information.getName());
//            hash.put("message",  message);
//            hash.put("date", LocalDateTime.now().toString());
//
//            System.out.println("HASH: " + hash.entrySet());
//            information = mapper.convertValue(hash, Info.class);
//            System.out.println(information + "********************");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return information;
//    }

    @Override
    public Info addMessage(String name, int logId, String message){
        Hashtable<String, String> hash = new Hashtable<>();
        ObjectMapper mapper = new ObjectMapper();
        Info information = getLog(logId);
        System.out.println("Find Log With Id: " + information);
        try {
            hash.put("name", name);
            hash.put("logId", String.valueOf(logId));
            hash.put("messageContent", information.getMessageContent().isEmpty() ? message : information.getMessageContent() + message);
            hash.put("date", LocalDateTime.now().toString());

            information = mapper.convertValue(hash, Info.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return information;
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


}
