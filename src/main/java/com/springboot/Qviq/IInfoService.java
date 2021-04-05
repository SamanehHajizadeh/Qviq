package com.springboot.Qviq;


import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IInfoService {

    Info getLog(int logId) ;
    Info updateLogByAddingMessage(String name, int logId, String message);
    List<Info> findAll() ;
    Info addNewMessage(Info message);


}
