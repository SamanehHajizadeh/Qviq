package com.springboot.Qviq;


public interface IMessageService {

   Message getLog(int logId) ;
   Message addMessage(String name, int logId, String message);

}
