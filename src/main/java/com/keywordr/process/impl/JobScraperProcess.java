package com.keywordr.process.impl;

import com.keywordr.data.Company;
import com.keywordr.data.Job;
import com.keywordr.exception.KeywordrRuntimeException;
import com.keywordr.process.api.ExecutionPlan;
import com.keywordr.provider.ConfigurationProvider;
import com.keywordr.provider.LoggerProvider;
import com.keywordr.util.JsonReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

public class JobScraperProcess extends ExecutionPlan<Set<Job>> {

    private ConfigurationProvider configurationProvider;
    private Logger logger;

    @Override
    public void initialize(Map<String, Object> initializer) {
        try {
            configurationProvider = (ConfigurationProvider) initializer.get("configurationProvider");
            logger = LoggerProvider.newInstance(JobScraperProcess.class);
        } catch (ClassCastException |IOException e) {
            throw new KeywordrRuntimeException("Failed to initialize JobScraperProcess with message: " + e.getMessage());
        }
    }

    @Override
    public Set<Job> execute() {
        // Check if EP is initialized
        if (configurationProvider == null) {
            throw new IllegalStateException("JobScraperProcess is not initialized!");
        }

        logger.info("Executing job scraper process.");
        logger.info("Reading company.json file.");

        // Read Company.json file
        List<Company> companies =
                JsonReader.readObjectsFromJson(
                        configurationProvider.getConfigurationProperty(
                                ConfigurationProvider.PROPERTY_COMPANY_JSON_FILE),
                        Company.class,
                        "companies"
                        );

        logger.info("Reading done. Total of " + companies.size() + " companies to check!");

        // For-each company check if it has any job listings
        JobListingProcess jobListingProcess = new JobListingProcess();
        jobListingProcess.initialize(Map.of(
                "configurationProvider", configurationProvider,
                "companies", companies
                ));
        Set<Job> jobs = jobListingProcess.execute();

        // For-each job flag job with used tech keywords
        JobKeywordFlagProcess jobKeywordFlagProcess = new JobKeywordFlagProcess();
        jobKeywordFlagProcess.initialize(Map.of(
                "configurationProvider", configurationProvider,
                "jobs", jobs
        ));
        jobs = jobKeywordFlagProcess.execute();

        logger.info("Job scraper process done.");

        // Return list of jobs
        return jobs;
    }
}
