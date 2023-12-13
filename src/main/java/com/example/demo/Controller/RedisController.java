package com.example.demo.Controller;

import com.example.demo.Auth.ResponseHandler;
import com.example.demo.Redis.RedisServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/redis")
public class RedisController {
    @Autowired
    private RedisServiceImp redisServiceImp;
    @GetMapping("/{key}")
    public ResponseEntity<Object> getAll(@PathVariable String key){
        return ResponseHandler.responseBuilder("Key: " + key + " ,expires: " + redisServiceImp.getExpiresKey(key), HttpStatus.OK,redisServiceImp.getKey(key));
    }

    @GetMapping("/clearAll")
    public ResponseEntity<Object> clearAll(){
        redisServiceImp.clear();
        return ResponseHandler.responseBuilder("clear all cache",HttpStatus.OK,null);
    }
}
