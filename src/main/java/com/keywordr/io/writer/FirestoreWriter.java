package com.keywordr.io.writer;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.keywordr.provider.ConfigurationProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirestoreWriter {
    private final DocumentReference document;
    public FirestoreWriter() throws IOException {
        ConfigurationProvider configurationProvider = ConfigurationProvider.getInstance();
        String firestoreAccountKey = configurationProvider.getConfigurationProperty(
                ConfigurationProvider.PROPERTY_FIRESTORE_ACCOUNT_KEY
        );
        String databaseUrl = configurationProvider.getConfigurationProperty(
                ConfigurationProvider.PROPERTY_DATABASE_URL
        );
        String collectionName = configurationProvider.getConfigurationProperty(
                ConfigurationProvider.PROPERTY_COLLECTION_NAME
        );
        String documentId = configurationProvider.getConfigurationProperty(
                ConfigurationProvider.PROPERTY_DOCUMENT_ID
        );


        FileInputStream inputStream = new FileInputStream(firestoreAccountKey);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .setDatabaseUrl(databaseUrl)
                .build();
        FirebaseApp.initializeApp(options);

        Firestore firestore = FirestoreClient.getFirestore();
        document = firestore.collection(collectionName).document(documentId);
    }

    public void writeData(Map<String, Object> data) throws ExecutionException, InterruptedException {
        document.set(data).get();
    }
}
