package com.springboot.Qviq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


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


    public long findMaxAgeOfMessage(long id){
            Info message = repository.findById(id)
                    .orElseThrow(() -> new InfoNotFoundException(id));

            Date dateMessage = message.getDate();
            if((dateMessage.equals(null)))
                throw new NullPointerException("message_Date is null" );

            System.out.println( "message_Date: " +  dateMessage);

            Date date2 = new Date();
            System.out.println("NOW: " + date2 );


            long diffResult = dateMessage.getTime() - new Date().getTime();
            System.out.println(diffResult +"********MaxAgeOfMessage**********");

        return diffResult;

    }

    public List<Info>  max_age_(Integer max_age) {
        System.out.println("max_age: " + max_age);

        List<Info> messages = repository.findAll()
                .stream()
                .filter(x ->
                        Long.compare(findMaxAgeOfMessage(x.getLogId()), max_age)== -1 )
                .collect(Collectors.toList());
        return messages;
    }


    public ResponseCookie createCookie(){
        return ResponseCookie.from("user-id", "Samane")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60)
//                .domain("example.com")
                .build();
    }


    protected String configure_()   {
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
        return  username;
    }
}
