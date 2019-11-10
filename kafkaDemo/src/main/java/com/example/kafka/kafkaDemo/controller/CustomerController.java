package com.example.kafka.kafkaDemo.controller;


import com.example.kafka.kafkaDemo.entity.Customer;
import com.example.kafka.kafkaDemo.services.Producer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/kafka")
class CustomerController {
    private final Producer producer;
    @Autowired
    public CustomerController(Producer producer) {
        this.producer = producer;
    }
    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestBody Customer customer) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String message = mapper.writeValueAsString(customer);
        this.producer.sendMessage(message);
    }
}

