package com.example.demo.controller;

import com.example.demo.entity.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private ElasticsearchRepository elasticSearchRepository;

 /*   @GetMapping("/customers")
    public ResponseEntity<?> getCustomersDetail() {
        Iterable customers = elasticSearchRepository.findAll();
        return new ResponseEntity<>(customers, HttpStatus.OK);

    }*/

    Pageable sortByName = PageRequest.of(0, 3, Sort.by("firstName").ascending().and(Sort.by("lastName").descending()));
    Pageable secondPageWithFiveElements = PageRequest.of(1, 5);


    @GetMapping("/customers")
    public Page<Customer> getCustomersDetail() {
        Iterable customers = elasticSearchRepository.findAll(sortByName);
        return (Page<Customer>) customers;

    }


    @GetMapping("/customers/{id}")
    public ResponseEntity<?> getCustomerDetails(@PathVariable("id") int id) {
        Optional customer = elasticSearchRepository.findById(id);
        if(customer.isPresent()) {
            return new ResponseEntity<>(customer,HttpStatus.FOUND);
        }

        else {
            return new ResponseEntity<>("Customer with id: "+id+" not found",HttpStatus.NOT_FOUND);
        }


    }

    @PostMapping("/customers")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        if(elasticSearchRepository.save(customer)!=null) {
            return  new ResponseEntity<>("New customer added", HttpStatus.CREATED);
        }

        else {
            return new ResponseEntity<>("Customer with id: "+customer.getId()+" could not be added",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer, @PathVariable("id") Integer Id) {

        Customer updatedCustomer = (Customer) elasticSearchRepository.save(customer);
        if(updatedCustomer!=null) {
            return  new ResponseEntity<>("Customer details updated", HttpStatus.OK);
        }

        else {
            return new ResponseEntity<>("Customer not found with id: "+customer.getId(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") int id) {

        if(elasticSearchRepository.findById(id).isPresent()) {
            elasticSearchRepository.deleteById(id);
            return new ResponseEntity<>("Customer with id: "+id+" deleted", HttpStatus.OK );
        }

        else {
            return new ResponseEntity<>("Customer with id: "+id+" not found", HttpStatus.NOT_FOUND);
        }

    }



}
