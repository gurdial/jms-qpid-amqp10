package com.test.jmsqpid;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class EMailMessageListener {

    @JmsListener(destination = "Emails", concurrency = "3-10")
    public void onMessage(Email email) {
        System.out.println("Received: " + email);
    }

}
