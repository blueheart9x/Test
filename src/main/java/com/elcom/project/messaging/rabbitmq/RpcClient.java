/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.project.messaging.rabbitmq;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author Admin
 */
public class RpcClient {

    @Autowired
    private RabbitTemplate rpcTemplate;

    @Autowired
    @Qualifier(value = "rpcClientExchange")
    private DirectExchange directExchange;

    public void send(int maxNumber) {
        for (int start = 0; start <= maxNumber; start++) {
            System.out.println(" [???] Client request for fib(" + start + ")");
            Integer response = (Integer) rpcTemplate.convertSendAndReceive(directExchange.getName(), "rpc", start);
            System.out.println(" [ok] Client get '" + response + "'");
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RpcClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
