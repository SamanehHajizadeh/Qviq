package com.springboot.qviq.service;


import com.springboot.qviq.dto.SystemStatusDTO;
import com.springboot.qviq.model.Info;

import java.util.List;

public interface InfoService {

    Info getLog(int logId);

    List<Info> findAll();

    Info updateLogByAddingMessage(String name, int logId, String message);

    Info addNewMessage(Info message);

    long findMaxAgeOfMessage(long id);

    List<Info> getMessagesLessThanMaxAge();

    void deleteMessagesOlderThanMaxAge();

    SystemStatusDTO getSystemStatus();

}
