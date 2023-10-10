package com.example.restcrud.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
@Getter
@Setter
@JacksonXmlRootElement(localName = "customers")
public class Customers {
    @JacksonXmlProperty(localName = "customer")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Customer> customers = new ArrayList<>();

    public Customers(){

    }

    public Customers(Spliterator<Customer> spliterator) {
      customers =  StreamSupport.stream(spliterator,false)
                .collect(Collectors.toList());
    }
}
