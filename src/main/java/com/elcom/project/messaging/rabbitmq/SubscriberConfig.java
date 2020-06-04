/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.project.messaging.rabbitmq;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Admin
 */
@Configuration
public class SubscriberConfig {

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("fanout_exchange");
    }
    
    @Bean
    public DirectExchange direct() {
        return new DirectExchange("direct_exchange");
    }

    private static class SubscriberReceiverConfig {
        
        @Bean
        public Queue autoDeleteQueue1() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue2() {
            return new AnonymousQueue();
        }
        
        @Bean
        public Queue autoDeleteQueue3() {
            return new AnonymousQueue();
        }
        
        @Bean
        public Queue autoDeleteQueue4() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding1(FanoutExchange fanout, Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1).to(fanout);
        }

        @Bean
        public Binding binding2(FanoutExchange fanout, Queue autoDeleteQueue2) {
            return BindingBuilder.bind(autoDeleteQueue2).to(fanout);
        }
        
        @Bean
        public Binding binding3(FanoutExchange fanout, Queue autoDeleteQueue3) {
            return BindingBuilder.bind(autoDeleteQueue3).to(fanout);
        }
        
        @Bean
        public Binding bindingDirect1(DirectExchange direct, Queue autoDeleteQueue4) {
            return BindingBuilder.bind(autoDeleteQueue4).to(direct).with("key1");
        }

        @Bean
        public SubscriberReceiver subscriberReceiver() {
            return new SubscriberReceiver();
        }
        
        @Bean
        public SubscriberReceiver2 subscriberReceiver2() {
            return new SubscriberReceiver2();
        }
        
        @Bean
        public SubscriberReceiver3 subscriberReceiver3() {
            return new SubscriberReceiver3();
        }
    }

    @Bean
    public SubscriberSender subscriberSender() {
        return new SubscriberSender();
    }
}
