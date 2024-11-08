package com.keywordr.process.impl;

import com.keywordr.data.Company;
import com.keywordr.data.Job;
import com.keywordr.exception.KeywordrRuntimeException;
import com.keywordr.process.api.ExecutionPlan;
import com.keywordr.provider.ConfigurationProvider;
import com.keywordr.util.JsonReader;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JobScraperProcess extends ExecutionPlan<Set<Job>> {

    private ConfigurationProvider configurationProvider;

    @Override
    public void initialize(Map<String, Object> initializer) {
        try {
            configurationProvider = (ConfigurationProvider) initializer.get("configurationProvider");
        } catch (ClassCastException e) {
            throw new KeywordrRuntimeException("Failed to initialize JobScraperProcess with message: " + e.getMessage());
        }
    }

    @Override
    public Set<Job> execute() {
        // Check if EP is initialized
        if (configurationProvider == null) {
            throw new IllegalStateException("JobScraperProcess is not initialized!");
        }

        // Read Company.json file
        List<Company> companies =
                JsonReader.readObjectsFromJson(
                        configurationProvider.getConfigurationProperty(
                                ConfigurationProvider.PROPERTY_COMPANY_JSON_FILE),
                        Company.class,
                        "companies"
                        );

        System.out.println(companies.size() + " companies to check...");


        // For-each company check if it has any job listings
        JobListingProcess jobListingProcess = new JobListingProcess();
        jobListingProcess.initialize(Map.of(
                "configurationProvider", configurationProvider,
                "companies", companies
                ));
        Set<Job> jobs = jobListingProcess.execute();

        System.out.println("Okay, I found " + jobs.size() + " jobs!");

        // For-each job listing, check if job listing contains tech keywords (flag job with used tech keywords)
        JobKeywordFlagProcess jobKeywordFlagProcess = new JobKeywordFlagProcess();
        jobKeywordFlagProcess.initialize(Map.of(
                "configurationProvider", configurationProvider,
                "jobs", jobs
        ));
        jobs = jobKeywordFlagProcess.execute();

        System.out.println("DONE!");

        // Return list of jobs
        return jobs;
    }
}
