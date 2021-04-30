package com.springboot.qviq.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@Access(AccessType.FIELD)
@NoArgsConstructor
@AllArgsConstructor
public class Info {

    private
    @Id
    @GeneratedValue
    Long logId;
    private String name;  // name of to submitting party
    private String messageContent;
    private Date date;

}

