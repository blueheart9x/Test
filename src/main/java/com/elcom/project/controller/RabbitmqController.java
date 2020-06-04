/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.project.controller;

import com.elcom.project.messaging.rabbitmq.RpcClient;
import com.elcom.project.messaging.rabbitmq.Sender;
import com.elcom.project.messaging.rabbitmq.SubscriberSender;
import com.elcom.project.messaging.rabbitmq.WorkerSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/rabbitmq")
public class RabbitmqController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitmqController.class);

    @Autowired
    Sender sender;

    @Autowired
    WorkerSender workerSender;

    @Autowired
    SubscriberSender subscriberSender;

    @Autowired
    RpcClient rpcClient;

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message) {
        sender.send(message);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping(value = "/worker", method = RequestMethod.GET)
    public ResponseEntity<String> sendWorkerMessage(@RequestParam("message") String message,
            @RequestParam("numOfSend") int numOfSend) {
        workerSender.send(message, numOfSend);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping(value = "/publisher", method = RequestMethod.GET)
    public ResponseEntity<String> sendSubscriberMessage(@RequestParam("message") String message,
            @DefaultValue("fanout") @RequestParam("type") String type) {
        if ("direct".equalsIgnoreCase(type)) {
            subscriberSender.sendDirect(message);
        } else {
            subscriberSender.send(message);
        }
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping(value = "/rpc", method = RequestMethod.GET)
    public ResponseEntity<String> sendRpcMessage(@RequestParam("number") int number) {
        rpcClient.send(number);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

}
