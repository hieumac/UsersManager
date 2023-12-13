package com.example.demo.Redis;

import com.example.demo.Auth.ResponseHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImp implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    @Override
    public Object getKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Long getExpiresKey(String key) {
        return redisTemplate.getExpire(key);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    //    private String getKeyFrom( String keyWord){
//    String key = String.format("all_user");
//    return key;
//    }
    @Override
    public void clear() {
    redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Override
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }


    @Override
    public List<Object> getAll(String key) throws JsonProcessingException {
//    String key = this.getKeyFrom(keyword);
    String json = (String) redisTemplate.opsForValue().get(key);
    List<Object> list = json != null ? redisObjectMapper.readValue(json, new TypeReference<List<Object>>() {}) : null;
        return list;
    }

    @Override
    public void saveAll(List<Object> list, String key) throws JsonProcessingException {
//    String key = this.getKeyFrom(keyword);
    String json = redisObjectMapper.writeValueAsString(list);
    redisTemplate.opsForValue().set(key, json, 3, TimeUnit.MINUTES);
    }
}
