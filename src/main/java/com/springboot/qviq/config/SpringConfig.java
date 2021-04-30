package com.springboot.qviq.config;

import com.springboot.qviq.service.InfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
public class SpringConfig {

    @Autowired
    private InfoService service;

    @Autowired
    private Environment env;


    //    @Scheduled(fixedDelay = 1000)
    public void scheduleFixedDelayTask() {
        System.out.println(
                "Fixed delay task - " + System.currentTimeMillis() / 1000);
    }

//    @Scheduled(cron = "0 * * * * *")
    public void scheduleTaskUsingCronExpression() {
        long start = System.currentTimeMillis() / 1000;
        System.out.println(
                "schedule tasks using cron jobs - " + start);

//        String max_age = env.getProperty("max_age");
//        System.out.println("********max_age*******" + max_age);
        service.deleteMessagesOlderThanMaxAge();

        long end = System.currentTimeMillis();
        log.info("Cronjob completed in {}ms.", end - start);
    }

}
