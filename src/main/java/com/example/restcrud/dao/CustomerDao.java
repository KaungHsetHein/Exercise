package com.example.restcrud.dao;

import com.example.restcrud.entity.Customer;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface CustomerDao extends JpaRepositoryImplementation<Customer,Integer> {
}
