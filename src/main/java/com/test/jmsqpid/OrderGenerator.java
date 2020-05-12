package com.test.jmsqpid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderGenerator {

    private JmsTemplate jmsTemplate;


        public OrderGenerator(@Qualifier("topicJmsTemplate") JmsTemplate jmsTemplate) {
            this.jmsTemplate = jmsTemplate;
        }


    @SuppressWarnings("Duplicates")
    @Scheduled(fixedDelay = 3000)
    public void generateOrder() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setAmount(BigDecimal.TEN);
        order.setReference("Reference1");
        jmsTemplate.convertAndSend("Orders", order);
        System.out.println("order message send");
    }
}
