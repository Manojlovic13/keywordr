package com.keywordr;


import com.keywordr.data.Job;
import com.keywordr.process.api.ExecutionPlan;
import com.keywordr.process.impl.ExportToFileProcess;
import com.keywordr.process.impl.JobScraperProcess;
import com.keywordr.provider.ConfigurationProvider;
import com.keywordr.util.CsvReader;
import com.keywordr.util.JsonReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hi :)");
        System.out.println("Please wait!");
        ExecutionPlan<Set<Job>> jobScraperProcess = new JobScraperProcess();
        ConfigurationProvider configurationProvider = new ConfigurationProvider();


        jobScraperProcess.initialize(Map.of(
                "configurationProvider", configurationProvider
        ));
        Set<Job> jobList = jobScraperProcess.execute();

        ExportToFileProcess exportToFileProcess = new ExportToFileProcess();
        exportToFileProcess.initialize(Map.of(
                "configurationProvider", configurationProvider,
                "jobs", jobList
        ));
        exportToFileProcess.execute();
    }
}