/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.project.messaging.rabbitmq;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 *
 * @author Admin
 */
public class RpcServer {

    @RabbitListener(queues = "rpc_requests")
    public int fibonacci(int n) {
        System.out.println(" [-->] Server received request for " + n);
        int result = fib(n);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(RpcServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(" [<--] Server returned " + result);
        return result;
    }

    public int fib(int n) {
        return n == 0 ? 0 : n == 1 ? 1 : (fib(n - 1) + fib(n - 2));
    }
}
