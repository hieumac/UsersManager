package com.example.demo.Firebase.Config;

import java.io.IOException;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
@Service
public class FCMInitializer {

    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;
    @Value("${app.firebase-config-file}")
    private String firebaseConfig;
    Logger logger = LoggerFactory.getLogger(FCMInitializer.class);
    @PostConstruct
    public void initialize() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()))
                    .setDatabaseUrl("https://earnest-setup-363908-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase application has been initialized" + FirebaseApp.getInstance());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
