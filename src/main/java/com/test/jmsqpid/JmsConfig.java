package com.test.jmsqpid;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.ConnectionFactory;

@Configuration
public class JmsConfig {


    @Autowired
    private AmqpProperties properties;

    @Bean
    @Primary
    public ConnectionFactory qpidConnectionFactory( ) {
        ConnectionFactory connectionFactory = new JmsConnectionFactory(properties.getUsername(), properties.getPassword(),properties.getUrl());
        return connectionFactory;
    }

    @Bean
    public MessageConverter jacksonMessageConverter() {
        MappingJackson2MessageConverter converter
                = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_messageType");
        return converter;
    }

    @Bean
    @Primary
    public JmsTemplate queueJmsTemplate(
            @Qualifier("qpidConnectionFactory")
                    ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setDestinationResolver(new DynamicDestinationResolver());
        jmsTemplate.setMessageConverter(jacksonMessageConverter());
        jmsTemplate.setPriority(4);
        jmsTemplate.setTimeToLive(30000L);
        jmsTemplate.setExplicitQosEnabled(true);
        return jmsTemplate;
    }

    @Bean
    public JmsTemplate topicJmsTemplate(
            @Qualifier("qpidConnectionFactory")
                    ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setDestinationResolver(new DynamicDestinationResolver());
        jmsTemplate.setMessageConverter(jacksonMessageConverter());
        return jmsTemplate;
    }

    @Value("${clientId:default_clientId}")
    String clientId;

    @Bean(name = "TopicListenerContainerFactory")
    public DefaultJmsListenerContainerFactory exampleListenerContainerFactory (
            @Qualifier("qpidConnectionFactory")
                    ConnectionFactory connectionFactory) throws Exception {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency("1-1");
        factory.setClientId(clientId);
    //   factory.setSubscriptionDurable(true);
        factory.setCacheLevel(DefaultMessageListenerContainer.CACHE_AUTO);
        factory.setMessageConverter(jacksonMessageConverter());
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean
    @Primary
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(@Qualifier("qpidConnectionFactory")
                                                                                  ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency("3-10");

        factory.setMessageConverter(jacksonMessageConverter());

        return factory;

    }

}
