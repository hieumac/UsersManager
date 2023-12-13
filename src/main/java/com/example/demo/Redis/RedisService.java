package com.example.demo.Redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RedisService {

    Object getKey(String key);
    Long getExpiresKey(String key);
    Boolean hasKey(String key);
    void clear();
    void deleteKey(String key);
    List<Object> getAll(String key) throws JsonProcessingException;
    void saveAll(List<Object> list,String key) throws JsonProcessingException;
}
