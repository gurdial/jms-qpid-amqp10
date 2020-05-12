package com.test.jmsqpid;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class OrderMessageListener {


    @JmsListener(destination = "Orders",
            containerFactory = "TopicListenerContainerFactory",
            subscription = "orders")
    public void onMessage(Order order) {
        System.out.println(order);
    }
/*

    @JmsListener(destination = "Orders", concurrency = "1-1")
    public void onMessage(Email email) {
        System.out.println("Received: " + email);
    }
  */
}
