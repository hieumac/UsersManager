package com.example.demo.Controller;

import com.example.demo.Auth.RegisterRequest;
import com.example.demo.Entity.Customer.Customer;
import com.example.demo.Entity.User.User;
import com.example.demo.Firebase.FirebaseMessageService;
import com.example.demo.Service.AuthenticationService;
import com.example.demo.Service.CustomerServiceImp;
import com.example.demo.Service.UserServiceImp;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/customers")
public class CustomerController {
    @Autowired
    private CustomerServiceImp customerServiceImp;

    @GetMapping("/listCustomers")
    public ResponseEntity<Object> getAllCustomer() throws JsonProcessingException {
        return customerServiceImp.getAllCustomer();
    }
    @PostMapping("/addNewCustomer")
    public ResponseEntity<Object> addNewCustomer(@RequestBody RegisterRequest request){
        return customerServiceImp.addNewCustomer(request);
    }
    @GetMapping("/idUpdate/{id}")
    public ResponseEntity<Object> getCustomerByIdUpdate(
            @PathVariable("id") Integer id) throws JsonProcessingException {
       return customerServiceImp.getCustomerById(id);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable("id")Integer id,@RequestBody Customer customer){
        return customerServiceImp.updateCustomer(id,customer.getFullname(),customer.getEmail(),customer.getPassword());
    }

    @DeleteMapping ("/delete/{userId}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("id") Integer id){
        return customerServiceImp.deleteCustomer(id);
    }
}
