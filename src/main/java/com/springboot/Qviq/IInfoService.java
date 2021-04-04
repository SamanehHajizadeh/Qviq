package com.springboot.Qviq;


import java.util.List;

public interface IInfoService {

    Info getLog(int logId) ;
    Info addMessage(String name, int logId, String message);
    List<Info> findAll() ;

}
