package com.springboot.Qviq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Access( AccessType.FIELD )

public class Result {


    private
    @Id
    @GeneratedValue
//    @JsonProperty
    Long Id;
    private Integer current_number_of_stored_logs;
    private Integer maxAge_limit;
    private Integer total_number_of_messages;
    private Integer average_number_of_messages_per_log;

    public Result() {
    }
}
