package com.springboot.Qviq;

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

    public Result(Integer current_number_of_stored_logs, Integer maxAge_limit, Integer total_number_of_messages, Integer average_number_of_messages_per_log) {
        this.current_number_of_stored_logs = current_number_of_stored_logs;
        this.maxAge_limit = maxAge_limit;
        this.total_number_of_messages = total_number_of_messages;
        this.average_number_of_messages_per_log = average_number_of_messages_per_log;
    }

    public Result() {
    }
}
