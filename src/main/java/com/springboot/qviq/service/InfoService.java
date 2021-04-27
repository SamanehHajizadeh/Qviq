package com.springboot.qviq.service;


import com.springboot.qviq.dto.SystemStatusDTO;
import com.springboot.qviq.model.Info;

import java.util.List;

public interface InfoService {

    Info getLog(int logId);

    Info updateLogByAddingMessage(String name, int logId, String message);

    Info addNewMessage(Info message);

    long findMaxAgeOfMessage(long id);

    List<Info> showMessagesLessAnMaxAge(Integer max_age);

    List<Info> older_than_max_age_(Long max_age);

    void deleteLogsOlder_thanMaxAge(Long maxAge);

    SystemStatusDTO getSystemStatus();

}
