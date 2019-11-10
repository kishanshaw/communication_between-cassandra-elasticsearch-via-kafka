package com.example.demo.customerrelationshipmanager.controller;


import com.example.demo.customerrelationshipmanager.entity.Customer;
import com.example.demo.customerrelationshipmanager.entity.CustomerEntity;
import com.example.demo.customerrelationshipmanager.repository.CassandraRepositoryCustom;
import com.example.demo.customerrelationshipmanager.repository.ElasticsearchRepositoryCustom;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CassandraRepositoryCustom customerCassandraRepository;

    @Autowired
    private ElasticsearchRepositoryCustom customerElasticsearchRepository;

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomersDetail() {
        List<Customer> customers = customerCassandraRepository.findAll();
       /* List<CustomerEntity> customerEntities = new ArrayList<>();
        for(Customer customer: customers) {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(customer.getId());
            customerEntity.setFirstName(customer.getFirstName());
            customerEntity.setLastName(customer.getLastName());
            customerEntity.setAddress(customer.getAddress());
            customerEntity.setCompanyName(customer.getCompanyName());
            customerEntity.setCity(customer.getCity());
            customerEntity.setState(customer.getState());
            customerEntity.setCounty(customer.getCounty());
            customerEntity.setEmail(customer.getEmail());
            customerEntity.setWebsite(customer.getWebsite());
            customerEntity.setZip(customer.getZip());
            customerEntity.setPhone1(customer.getPhone1());
            customerEntity.setPhone2(customerEntity.getPhone2());
            customerEntities.add(customerEntity);
        }
        customerElasticsearchRepository.saveAll(customerEntities);
*/        return new ResponseEntity<>(customers, HttpStatus.OK);

    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<?> getCustomerDetails(@PathVariable("id") int id) {
        Optional<Customer> customer = customerCassandraRepository.findById(id);
        if(customer.isPresent()) {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        }

        else {
            return new ResponseEntity<>("Customer with id: "+id+" not found",HttpStatus.NOT_FOUND);
        }


    }

    @PostMapping("/customers")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        if(customerCassandraRepository.save(customer)!=null) {
           /* CustomerEntity customerEntity = new CustomerEntity(customer.getId(), customer.getFirstName(), customer.getLastName(),
                    customer.getAddress(), customer.getCompanyName(), customer.getCity(),customer.getCounty(), customer.getState()
            , customer.getZip(), customer.getPhone1(), customer.getPhone2(), customer.getEmail(), customer.getWebsite());
            //customerElasticsearchRepository.save(customerEntity);
*/

           //Making post call to kafka endpoint
            String kafkaUrl = "http://localhost:9000/kafka/publish";
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Customer> request = new HttpEntity<>(customer);
            ResponseEntity<Customer> customerEntity = restTemplate.postForEntity(kafkaUrl, request, Customer.class);

            return  new ResponseEntity<>("New customer added", HttpStatus.CREATED);
        }

        else {
            return new ResponseEntity<>("Customer with id: "+customer.getId()+" could not be added",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer, @PathVariable("id") Integer Id) {

        Customer updatedCustomer = customerCassandraRepository.save(customer);
        CustomerEntity customerEntity = new CustomerEntity(customer.getId(), customer.getFirstName(), customer.getLastName(),
                customer.getAddress(), customer.getCompanyName(), customer.getCity(),customer.getCounty(), customer.getState()
                , customer.getZip(), customer.getPhone1(), customer.getPhone2(), customer.getEmail(), customer.getWebsite());

        customerElasticsearchRepository.save(customerEntity);

        if(updatedCustomer!=null) {
            return  new ResponseEntity<>("Customer details updated", HttpStatus.OK);
        }

        else {
            return new ResponseEntity<>("Customer not found with id: "+customer.getId(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") int id) {

        if(customerCassandraRepository.findById(id).isPresent()) {
            customerCassandraRepository.deleteById(id);
            customerElasticsearchRepository.deleteById(id);
            return new ResponseEntity<>("Customer with id: "+id+" deleted", HttpStatus.OK );
        }

        else {
            return new ResponseEntity<>("Customer with id: "+id+" not found", HttpStatus.NOT_FOUND);
        }

    }


    @GetMapping("/customers/name/{name}")
    public ResponseEntity<?> getCustomers(@PathVariable("name") String name) {
       List<Customer> customers= customerCassandraRepository.getCustomersByFirstName(name);
        if(!customers.isEmpty()) {
            return new ResponseEntity<>(customers, HttpStatus.OK);
        }

        else {
            return new ResponseEntity<>("Customer(s) not found with name: "+name, HttpStatus.NOT_FOUND);
        }

    }

}
