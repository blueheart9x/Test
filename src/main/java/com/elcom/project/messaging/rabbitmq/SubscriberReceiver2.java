/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.project.messaging.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 *
 * @author Admin
 */
public class SubscriberReceiver2 {

    @RabbitListener(queues = "#{autoDeleteQueue3.name}")
    public void receiveForSub2(String in) throws InterruptedException {
        System.out.println("instance " + SubscriberReceiver2.class + "[x] Received '" + in + "'");
    }
}
