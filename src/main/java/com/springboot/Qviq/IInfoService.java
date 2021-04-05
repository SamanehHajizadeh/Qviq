package com.springboot.Qviq;


import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public interface IInfoService {

    Info getLog(int logId) ;
    Info updateLogByAddingMessage(String name, int logId, String message);
    List<Info> findAll() ;
    Info addNewMessage(Info message);
     ResponseCookie getCookie();
    long findMaxAgeOfMessage(long id);
    List<Info> showMessagesLessAnMaxAge(Integer max_age);
    Hashtable<String,Object> getLogByCache();
    List<Info>  older_than_max_age_(Long max_age);
    void deleteLogsOlder_thanMaxAge();

}
