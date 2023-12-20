package com.example.demo.Firebase.Service;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import com.example.demo.Firebase.Entity.PushNotificationRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class FCMService {
    private static Logger logger = LoggerFactory.getLogger(FCMService.class);

private static FirebaseDatabase firebaseDatabase;
public static void pushNotice(Object notice, String list, String sys) {
    firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("customers");
    excutedFirebase(notice, databaseReference);
}

public static void excutedFirebase(Object notice, DatabaseReference databaseReference) {
    String key = databaseReference.getParent().push().getKey().toString();
    DatabaseReference childReference = databaseReference.child(key);
    DatabaseReference childReference2 = childReference.child("/");
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    childReference2.setValue(notice, (de,dr)->{
        countDownLatch.countDown();
    });
    logger.info("Send notice: " + notice + " ,key: " + key + " ,child: " + childReference2);
}
    public void sendMessageToToken(PushNotificationRequest request)
            throws InterruptedException, ExecutionException {

        Message message = getPreconfiguredMessageToToken(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);
        String response = sendAndGetResponse(message);
        logger.info("Sent message to token. Device token: " + request.getToken() + ", " + response+ " msg "+jsonOutput);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }
    private Message getPreconfiguredMessageToToken(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request)
//                .setTopic("topic")
//                .setToken(request.getToken())
                .build();
    }
    private Message getPreconfiguredMessageWithoutData(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setTopic(request.getTitle())
                .build();
    }
    private Message getPreconfiguredMessageWithData(Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).putAllData(data).setToken(request.getToken())
                .build();
    }
    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {

        return Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getMessage())
                        .build());
    }

}
