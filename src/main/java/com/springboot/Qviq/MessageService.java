package com.springboot.Qviq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Hashtable;


@Service
public class MessageService implements IMessageService{
    @Autowired
    private MessageRepository repository;


    @Override
    public Message addMessage(String name, int logId, String message){
        Hashtable<String, Object> hashmap = new Hashtable<>();
        ObjectMapper mapper = new ObjectMapper();
        Message message1 = new Message();
        try {
            hashmap.put("name", name);
            hashmap.put("logId", logId);
            hashmap.put("messageContent", message);
            hashmap.put("date", LocalDateTime.now());

            message1 = mapper.convertValue(hashmap, Message.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return repository.save(message1);
    }

    @Override
    public Message getLog(int logId)  {
        return repository.findById(Long.valueOf(logId)).get();
    }

}
