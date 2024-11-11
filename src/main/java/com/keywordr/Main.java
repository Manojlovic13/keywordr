package com.keywordr;


import com.keywordr.data.Job;
import com.keywordr.exception.KeywordrRuntimeException;
import com.keywordr.process.api.ExecutionPlan;
import com.keywordr.process.impl.ExportToFileProcess;
import com.keywordr.process.impl.JobScraperProcess;
import com.keywordr.provider.ConfigurationProvider;
import com.keywordr.provider.LoggerProvider;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        System.out.println("Please wait!");
        ConfigurationProvider configurationProvider = ConfigurationProvider.getInstance();

        // Execute job scraper process
        ExecutionPlan<Set<Job>> jobScraperProcess = new JobScraperProcess();
        jobScraperProcess.initialize(Map.of(
                "configurationProvider", configurationProvider
        ));
        Set<Job> jobList = jobScraperProcess.execute();

        // Execute export to file process
        ExportToFileProcess exportToFileProcess = new ExportToFileProcess();
        exportToFileProcess.initialize(Map.of(
                "configurationProvider", configurationProvider,
                "jobs", jobList
        ));
        exportToFileProcess.execute();
    }
}