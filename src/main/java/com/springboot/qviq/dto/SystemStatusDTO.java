package com.springboot.qviq.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemStatusDTO {

    private Long id;

    private Long currentNumberOfStoredLogs;

    private Integer maxAgeLimit;

    private Long totalNumberOfMessages;

    private Integer averageNumberOfMessagesPerLog;

}
