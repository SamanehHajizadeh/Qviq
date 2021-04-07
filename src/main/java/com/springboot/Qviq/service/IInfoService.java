package com.springboot.Qviq.service;


import com.springboot.Qviq.model.Info;
import org.springframework.http.ResponseCookie;

import java.util.Hashtable;
import java.util.List;

public interface IInfoService {

    Info getLog(int logId);

    Info updateLogByAddingMessage(String name, int logId, String message);

    List<Info> findAll();

    Info addNewMessage(Info message);

    long findMaxAgeOfMessage(long id);

    List<Info> showMessagesLessAnMaxAge(Integer max_age);

    List<Info> older_than_max_age_(Long max_age);

    void deleteLogsOlder_thanMaxAge(Long maxAge);

    Hashtable<String, Object> getLogByCache(Long max_age);

}
