package com.springboot.Qviq;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

public class LogData {
    private
    @Id
    @GeneratedValue
    Long LogId;
    private String logContent;

    public LogData() {

    }

    public LogData(Long logId, String logContent) {
        LogId = logId;
        this.logContent = logContent;
    }

    public Long getLogId() {
        return LogId;
    }

    public String getLogContent() {
        return logContent;
    }
}
