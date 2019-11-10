package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticSearchRepository extends ElasticsearchRepository<Customer, Integer>, PagingAndSortingRepository<Customer, Integer> {

}
