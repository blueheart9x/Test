/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.project.messaging.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Admin
 */
@Configuration
public class WorkerConfig {

    @Bean("worker_queue")
    public Queue initWorkerQueue() {
        return new Queue("worker_queue");
    }

    private static class ReceiverConfig {
        @Bean
        public WorkerReceiver workerReceiver1() {
            return new WorkerReceiver(1);
        }
        @Bean
        public WorkerReceiver workerReceiver2() {
            return new WorkerReceiver(2);
        }
        @Bean
        public WorkerReceiver workerReceiver3() {
            return new WorkerReceiver(3);
        }
    }

    @Bean
    public WorkerSender workerSender() {
        return new WorkerSender();
    }
}
