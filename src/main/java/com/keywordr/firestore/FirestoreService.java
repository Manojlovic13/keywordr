package com.keywordr.firestore;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.keywordr.exception.KeywordrRuntimeException;
import com.keywordr.provider.ConfigurationProvider;

import java.io.FileInputStream;
import java.io.IOException;

public class FirestoreService {
    private ConfigurationProvider configurationProvider;
    private final String FIRESTORE_DATABASE_URL = "https://keywordr-5b031.firebaseio.com";
    public FirestoreService() {
        configurationProvider = ConfigurationProvider.getInstance();
        String firestoreAccountKey = configurationProvider.getConfigurationProperty(
                ConfigurationProvider.PROPERTY_FIRESTORE_ACCOUNT_KEY
        );

        try {
            FileInputStream inputStream = new FileInputStream(firestoreAccountKey);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .setDatabaseUrl(FIRESTORE_DATABASE_URL)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new KeywordrRuntimeException("Failed to initialize FirestoreService with message: " + e.getMessage());
        }
    }

    
}
