package com.example.demo.customerrelationshipmanager.repository;

import com.example.demo.customerrelationshipmanager.entity.Customer;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CassandraRepositoryCustom extends CassandraRepository<Customer, Integer> {

    @Query("select  * from customer  where firstname = ?0")
    List<Customer> getCustomersByFirstName(String firstname);

}
