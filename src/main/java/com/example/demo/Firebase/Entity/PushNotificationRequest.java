package com.example.demo.Firebase.Entity;

import lombok.Data;

import java.util.Map;

@Data
public class PushNotificationRequest {
    private String title;
    private String message;
    private String token;
}
