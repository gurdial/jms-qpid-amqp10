package com.test.jmsqpid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJms
@EnableScheduling
@Configuration
public class JmsQpidApplication {

    public static void main(String... args) {
        SpringApplication.run(JmsQpidApplication.class, args);
    }
}
