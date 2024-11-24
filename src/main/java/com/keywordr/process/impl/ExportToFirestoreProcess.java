package com.keywordr.process.impl;

import com.keywordr.exception.KeywordrRuntimeException;
import com.keywordr.io.writer.FirestoreWriter;
import com.keywordr.process.api.ExecutionPlan;
import com.keywordr.provider.LoggerProvider;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ExportToFirestoreProcess extends ExecutionPlan<Integer> {
    private Map<String, Object> jobMap;
    private FirestoreWriter firestoreWriter;
    private Logger logger;
    @Override
    public void initialize(Map<String, Object> initializer) {
        try {
            jobMap = (HashMap<String, Object>) initializer.get("jobMap");
            firestoreWriter = new FirestoreWriter();
            logger = LoggerProvider.newInstance(ExportToFirestoreProcess.class);
        } catch (ClassCastException | IOException e) {
            throw new KeywordrRuntimeException("Failed to initialize ExportToFirestoreProcess with message: " + e.getMessage());
        }

    }

    @Override
    public Integer execute() {
        if (jobMap == null) {
            throw new KeywordrRuntimeException("ExportToFirestoreProcess is not initialized!");
        }

        logger.info("Executing export to Firestore process.");

        try {
            firestoreWriter.writeData(jobMap);
        } catch (InterruptedException | ExecutionException e) {
            throw new KeywordrRuntimeException("Failed to write to Firestore with message: " + e.getMessage());
        }

        logger.info("Export to Firestore process done.");

        return 1;
    }
}
