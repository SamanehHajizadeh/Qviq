package com.springboot.Qviq;

import com.springboot.Qviq.service.IInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SpringConfig {

    @Autowired
    IInfoService service;

    @Autowired
    private Environment env;
//
//
//    //    @Scheduled(fixedDelay = 1000)
//    public void scheduleFixedDelayTask() {
//        System.out.println(
//                "Fixed delay task - " + System.currentTimeMillis() / 1000);
//    }

    @Scheduled(cron = "0 * * * * *")
    public void scheduleTaskUsingCronExpression() {
        long now = System.currentTimeMillis() / 1000;
        System.out.println(
                "schedule tasks using cron jobs - " + now);

        String max_age = env.getProperty("max_age");
        System.out.println("********max_age*******" + max_age);
        service.deleteLogsOlder_thanMaxAge(Long.valueOf(max_age));
        System.out.println("DONE!!!!!!!!!!!!!!!!");
    }

}
