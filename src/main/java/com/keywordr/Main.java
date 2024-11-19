package com.keywordr;


import com.keywordr.data.Job;
import com.keywordr.firestore.FirestoreService;
import com.keywordr.process.api.ExecutionPlan;
import com.keywordr.process.impl.ExportToFileProcess;
import com.keywordr.process.impl.ExportToFirestoreProcess;
import com.keywordr.process.impl.JobScraperProcess;
import com.keywordr.provider.ConfigurationProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        System.out.println("Please wait!");
        ConfigurationProvider configurationProvider = ConfigurationProvider.getInstance();
        FirestoreService firestoreService;

        // Execute job scraper process
        ExecutionPlan<Set<Job>> jobScraperProcess = new JobScraperProcess();
        jobScraperProcess.initialize(Map.of(
                "configurationProvider", configurationProvider
        ));
        Set<Job> jobSet = jobScraperProcess.execute();

        // Execute export to file process
        ExportToFileProcess exportToFileProcess = new ExportToFileProcess();
        exportToFileProcess.initialize(Map.of(
                "configurationProvider", configurationProvider,
                "jobs", jobSet
        ));
        exportToFileProcess.execute();

        // Execute upload to Firestore
        if (Boolean.parseBoolean(configurationProvider.getConfigurationProperty(
                ConfigurationProvider.PROPERTY_FIRESTORE_UPLOAD
        ))) {
            Map<String, Object> jobMap = new HashMap<>();
            jobMap.put("jobs", new ArrayList<>(jobSet));
            ExecutionPlan<Integer> exportToFirestoreProcess = new ExportToFirestoreProcess();
            exportToFirestoreProcess.initialize(Map.of(
                    "jobMap", jobMap
            ));

            exportToFirestoreProcess.execute();
        }
    }
}