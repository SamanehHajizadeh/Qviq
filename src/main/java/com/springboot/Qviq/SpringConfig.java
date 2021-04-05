package com.springboot.Qviq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SpringConfig {

    @Autowired
    IInfoService service;

    @Scheduled(fixedDelay = 1000)
    public void scheduleFixedDelayTask() {
        System.out.println(
                "Fixed delay task - " + System.currentTimeMillis() / 1000);
    }

    //Scheduling a task to be executed at 10:15 AM on the 15th day of every month.
//    @Scheduled(cron = "0 15 10 15 * ?")
    @Scheduled(cron = "0 * * * * *")
        public void scheduleTaskUsingCronExpression() {

        long now = System.currentTimeMillis() / 1000;
        System.out.println(
                "schedule tasks using cron jobs - " + now);

        service.deleteLogsOlder_thanMaxAge();
        System.out.println("DONE!!!!!!!!!!!!!!!!111");
    }

}
