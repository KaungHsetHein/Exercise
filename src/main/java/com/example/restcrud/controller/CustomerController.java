package com.example.restcrud.controller;

import com.example.restcrud.entity.Customer;
import com.example.restcrud.entity.Customers;
import com.example.restcrud.service.CustomerService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CustomerController {

    private final CustomerService customerService;

    @DeleteMapping("/customers")
    public ResponseEntity deleteCustomerById(@RequestParam("id") int id){
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }

    record CustomerResponse(int id, String code, @JsonProperty("first_name") String firstName, @JsonProperty("last_name") String lastName, String address){}
    //localhost:8080/api/1.xml
   @GetMapping("/customers/{id}/{type}")
    public ResponseEntity<CustomerResponse> findCustomerByIdXmlJson(@PathVariable int id,@PathVariable String type){
        if("xml".equalsIgnoreCase(type)){
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_XML)
                    .body(todto(customerService.findCustomerById(id)));
        }
        else {
            return ResponseEntity.ok(todto(customerService.findCustomerById(id)));
        }
    }
    //curl -X POST -H "Content-Type: application/json" -d '{"code":"AE","first_name","JOhn","last_name","Doe","address","Yangon"}' localhost:8080/api/customers
    @GetMapping(value = "/customers/{id}",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public CustomerResponse findCustomerById(@PathVariable int id){

        Customer customer = customerService.findCustomerById(id);
        return todto(customer);
    }



    private static CustomerResponse todto(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getCode(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getAddress()
        );
    }



    record CustomerRequest(String code,@JsonProperty("first_name") String firstName,@JsonProperty("last_name") String lastName,String address){}
    @PostMapping(value = "/customers",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@RequestBody CustomerRequest request){
       return todto(customerService.createCustomer(toEntity(request)));
    }

    @PostMapping(value = "/customer/v2",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CustomerResponse> createCustomerV2(@RequestBody CustomerRequest request) throws Exception{
        CustomerResponse response = todto(customerService.createCustomer(toEntity(request)));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build()
                .created(URI.create("http://localhost:8080/api/customers/"+response.id))
                .body(response);
    }

    private Customer toEntity(CustomerRequest request){
        return Customer.builder()
                .code(request.code)
                .firstName(request.firstName)
                .lastName(request.lastName)
                .address(request.address)
                .build();
    }

    @GetMapping(value = "/customers",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public Customers listAllCustomers(){
        return customerService.listAllCustomers();
    }

    @GetMapping("/customers")
    public ResponseEntity<Customers> listCustomersXmlOrJson(@RequestParam("type")String type){
        if("xml".equalsIgnoreCase(type)){
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_XML)
                    .body(customerService.listAllCustomers());
        }
        else {
            return ResponseEntity.ok()
                    .body(customerService.listAllCustomers());
        }

    }


}
