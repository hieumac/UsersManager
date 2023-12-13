package com.example.demo.Firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@DependsOn("FCMInitializer")
@Service
public class FirebaseMessageService {
    private Logger logger = LoggerFactory.getLogger(FirebaseMessageService.class);

    @Autowired
    private FCMService fcmService;

    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void createFirebaseIndex(PushNotificationRequest request) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> colectionsApiFuture = dbFirestore.collection("crud-index").document(request.getTitle()).set(request);
        logger.info(colectionsApiFuture.get().getUpdateTime().toString());
    }
    public void getFirebaseIndex(PushNotificationRequest request) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("crud-index").document(request.getTitle());
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot documentSnapshot = future.get();
        PushNotificationRequest request1;
        if (documentSnapshot.exists()){
            request1 = documentSnapshot.toObject(PushNotificationRequest.class);
            logger.info(request1.toString());
        } else {
            logger.warn("doc not exists");
        }

    }
    public void updateFirebaseIndex(PushNotificationRequest request) {

    }
    public void deleteFirebaseIndex(PushNotificationRequest request) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = dbFirestore.collection("crud-index").document(request.getTitle()).delete();
        logger.info("deleted " + request);
    }
}
