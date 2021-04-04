package com.springboot.Qviq;

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
    Long Id;
    private String name;  // name of to submitting party
    private String messageContent;
    private Date date;

    public Info() {
    }

    public Info(Long messageId, String name, String messageContent, Date date) {
        this.Id = messageId;
        this.name = name;
        this.messageContent = messageContent;
        this.date = date;
    }

    public Long getId() {
        return Id;
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

    public void setId(Long id) {
        Id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    //No setter to make it immutable(threadSafe)
}

