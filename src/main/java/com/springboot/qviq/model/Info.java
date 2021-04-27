package com.springboot.qviq.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Access( AccessType.FIELD )


//Since String is immutable in Java, it's inherently thread-safe.
// Read-only or final variables in Java are also thread-safe in Java.
// Static variables if not synchronized properly become a major cause of thread-safety issues.

//@JsonIgnoreProperties(ignoreUnknown = true) //:rom the Jackson JSON processing library to indicate that any properties not bound in this type should be ignored.
public class Info {

    private
    @Id
    @GeneratedValue
     Long logId;
    private  String name;  // name of to submitting party
    private  String messageContent;
    private  Date date;

    public Info() {
    }

    public Info(Long id, String name, String messageContent, Date date) {
        logId = id;
        this.name = name;
        this.messageContent = messageContent;
        this.date = date;
    }

    public Info(String name, String messageContent, Date date) {
        this.name = name;
        this.messageContent = messageContent;
        this.date = date;
    }

    public Long getLogId() {
        return logId;
    }


    public String getName() {
        return name;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public Date getDate() {
        return date;
    }

    //No setter to make it immutable(threadSafe)
}

