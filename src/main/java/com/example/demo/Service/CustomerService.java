package com.example.demo.Service;

import com.example.demo.Auth.RegisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;


public interface CustomerService {

    public ResponseEntity<Object> getAllCustomer() throws JsonProcessingException;
    public ResponseEntity<Object> getCustomerById(Integer customerId) throws JsonProcessingException;
    ResponseEntity<Object> addNewCustomer(RegisterRequest request);
    public ResponseEntity<Object> deleteCustomer(Integer customerId) ;
    public ResponseEntity<Object> updateCustomer(Integer customerid, String fullname, String email, String password) ;
}
