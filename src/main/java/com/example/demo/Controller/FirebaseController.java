package com.example.demo.Controller;

import com.example.demo.Firebase.FirebaseMessageService;
import com.example.demo.Firebase.PushNotificationRequest;
import com.example.demo.Service.AuthenticationService;
import com.example.demo.Service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/firebase")
public class FirebaseController {
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private AuthenticationService authService;
    @Autowired
    private FirebaseMessageService firebaseMessageService;


    @PostMapping("/sendFirebase")
    public void sendFirebaseMessageing(@RequestBody PushNotificationRequest pushNotificationRequest) {
        firebaseMessageService.sendPushNotificationToToken(pushNotificationRequest);
    }
    @PostMapping("/createFirebaseIndex")
    public void createFirebaseIndex(@RequestBody PushNotificationRequest pushNotificationRequest) throws ExecutionException, InterruptedException {
        firebaseMessageService.createFirebaseIndex(pushNotificationRequest);
    }
    @PostMapping("/getFirebaseIndex")
    public void getFirebaseIndex(@RequestBody PushNotificationRequest pushNotificationRequest) throws ExecutionException, InterruptedException {
        firebaseMessageService.getFirebaseIndex(pushNotificationRequest);
    }
    @PostMapping("/updateFirebaseIndex")
    public void updateFirebaseIndex(@RequestBody PushNotificationRequest pushNotificationRequest) throws ExecutionException, InterruptedException {
        firebaseMessageService.createFirebaseIndex(pushNotificationRequest);
    }
    @PostMapping("/deleteFirebaseIndex")
    public void deleteFirebaseIndex(@RequestBody PushNotificationRequest pushNotificationRequest) {
        firebaseMessageService.deleteFirebaseIndex(pushNotificationRequest);
    }
}
