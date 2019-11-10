package com.example.kafka.kafkaDemo.services;

import com.example.kafka.kafkaDemo.entity.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Consumer {

    @KafkaListener(topics ="CustomerTopic", groupId = "group_id")
    public void consume(String message) throws JsonProcessingException, JSONException {
        System.out.println("consumed message: "+message);
        //Mapping string message to Customer entity
        ObjectMapper mapper = new ObjectMapper();
        Customer customer = mapper.readValue(message, Customer.class);
        //System.out.println("Printing data:" +customer.toString() );


        //making post call to the elasticsearch endpoint
        RestTemplate restTemplate = new RestTemplate();
        String elasticUrl = "http://localhost:8082/api/customers";
        HttpEntity<Customer> request = new HttpEntity<>(customer);
        ResponseEntity<Customer> customerEntity = restTemplate.postForEntity(elasticUrl, request, Customer.class);
        System.out.println("resource sent to elasticsearch endpoint");

    }
}