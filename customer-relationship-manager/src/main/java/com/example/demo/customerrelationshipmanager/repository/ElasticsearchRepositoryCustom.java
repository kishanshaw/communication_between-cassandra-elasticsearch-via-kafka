package com.example.demo.customerrelationshipmanager.repository;

import com.example.demo.customerrelationshipmanager.entity.Customer;
import com.example.demo.customerrelationshipmanager.entity.CustomerEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticsearchRepositoryCustom extends ElasticsearchRepository<CustomerEntity, Integer> {
}
