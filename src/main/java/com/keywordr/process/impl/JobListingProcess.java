package com.keywordr.process.impl;

import com.keywordr.data.Company;
import com.keywordr.data.Job;
import com.keywordr.exception.KeywordrRuntimeException;
import com.keywordr.process.api.ExecutionPlan;
import com.keywordr.provider.ConfigurationProvider;
import com.keywordr.provider.LoggerProvider;
import com.keywordr.util.CsvReader;
import com.keywordr.util.Keywordr;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.*;

public class JobListingProcess extends ExecutionPlan<Set<Job>> {
    private ConfigurationProvider configurationProvider;
    private ArrayList<Company> companies;
    private Logger logger;
    @Override
    public void initialize(Map<String, Object> initializer) {
        try {
            configurationProvider = (ConfigurationProvider) initializer.get("configurationProvider");
            companies = (ArrayList<Company>) initializer.get("companies");
            logger = LoggerProvider.newInstance(JobListingProcess.class);
        } catch (ClassCastException | IOException e) {
            throw new KeywordrRuntimeException("Failed to initialize JobScraperProcess with message: " + e.getMessage());
        }
    }

    @Override
    public Set<Job> execute() {
        if (configurationProvider == null || companies == null) {
            throw new KeywordrRuntimeException("JobListingProcess is not initialized!");
        }

        logger.info("Executing job listing process.");
        logger.info("Reading JobKeywords.csv file.");

        HashSet<String> jobKeywords =
                CsvReader.readCsvFile(
                        configurationProvider.getConfigurationProperty(
                                ConfigurationProvider.PROPERTY_JOB_KEYWORDS_CSV_FILE));

        logger.info("Reading done. Total of " + jobKeywords.size() + " job keywords defined!");

        boolean useSelenium = Boolean.parseBoolean(
                configurationProvider.getConfigurationProperty(ConfigurationProvider.PROPERTY_USE_SELENIUM)
        );

        int timeout = Integer.parseInt(
                configurationProvider.getConfigurationProperty(ConfigurationProvider.PROPERTY_JS_EXECUTION_TIMEOUT)
        );

        Set<Job> jobs = Keywordr.companiesJobListingCheck(companies, jobKeywords, useSelenium, timeout);

        logger.info("Job listing process done. Jobs found: " + jobs.size() + ".");

        return jobs;
    }
}
