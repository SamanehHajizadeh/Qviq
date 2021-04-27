
package com.springboot.qviq;


import com.springboot.qviq.model.Info;
import com.springboot.qviq.repository.InfoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

//Tags the class as a source of bean definitions for the application context.
@Configuration
class LoadDatabase {
    private final static Logger logger = Logger.getLogger(LoadDatabase.class.getName());
    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


   @Bean
    CommandLineRunner initDatabase(InfoRepository messageRepository) throws ParseException {
       Info M1 =  new Info("sami", null, new Date());

       final   Date date = myFormat.parse("2020-09-03 09:32:30");
       Info M2 =  new Info("B", "messageContent B ", date);


        return args -> {
            Info info = messageRepository.saveAndFlush(M1);
            logger.info("M1 " + info);

            Info info2 = messageRepository.saveAndFlush(M2);
            logger.info("M2 " + info2);
        };
    }
}
