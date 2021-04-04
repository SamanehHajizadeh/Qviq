package com.springboot.Qviq;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Access( AccessType.FIELD )


//Since String is immutable in Java, it's inherently thread-safe.
// Read-only or final variables in Java are also thread-safe in Java.
// Static variables if not synchronized properly become a major cause of thread-safety issues.

//@JsonIgnoreProperties(ignoreUnknown = true) //:rom the Jackson JSON processing library to indicate that any properties not bound in this type should be ignored.
public class Message {

    private
    @Id
    @GeneratedValue
    Long messageId;
    private int LogId;
    private String name;  // name of to submitting party
    private String messageContent;
    private Date date;

    public Message() {
    }

    public Message(Long messageId, String name, String messageContent, Date date) {
        this.messageId = messageId;
        this.name = name;
        this.messageContent = messageContent;
        this.date = date;
    }

    public Message(Long messageId, int logId, String name, String messageContent, Date date) {
        this.messageId = messageId;
        this.LogId = logId;
        this.name = name;
        this.messageContent = messageContent;
        this.date = date;
    }

    public Long getMessageId() {
        return messageId;
    }

    public int getLogId() {
        return LogId;
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

