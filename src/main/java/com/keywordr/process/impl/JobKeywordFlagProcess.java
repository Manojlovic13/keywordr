package com.keywordr.process.impl;

import com.keywordr.data.Job;
import com.keywordr.exception.KeywordrRuntimeException;
import com.keywordr.process.api.ExecutionPlan;
import com.keywordr.provider.ConfigurationProvider;
import com.keywordr.provider.LoggerProvider;
import com.keywordr.util.CsvReader;
import com.keywordr.util.Keywordr;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JobKeywordFlagProcess extends ExecutionPlan<Set<Job>> {
    private ConfigurationProvider configurationProvider;
    private Set<Job> jobs;
    private Logger logger;
    @Override
    public void initialize(Map<String, Object> initializer) {
        try {
            configurationProvider = (ConfigurationProvider) initializer.get("configurationProvider");
            jobs = (HashSet<Job>) initializer.get("jobs");
            logger = LoggerProvider.newInstance(JobKeywordFlagProcess.class);
        } catch (ClassCastException | IOException e) {
            throw new KeywordrRuntimeException("Failed to initialize JobKeywordFlagProcess with message: " + e.getMessage());
        }
    }

    @Override
    public Set<Job> execute() {
        if (configurationProvider == null || jobs == null) {
            throw new KeywordrRuntimeException("JobListingProcess is not initialized!");
        }

        logger.info("Executing job keyword flag process.");
        logger.info("Reading TechKeywords.csv file.");

        HashSet<String> techKeywords =
                CsvReader.readCsvFile(
                        configurationProvider.getConfigurationProperty(
                                ConfigurationProvider.PROPERTY_TECH_KEYWORDS_CSV_FILE));

        logger.info("Reading done. Total of " + techKeywords.size() + " tech keywords defined!");

        boolean useSelenium = Boolean.parseBoolean(
                configurationProvider.getConfigurationProperty(ConfigurationProvider.PROPERTY_USE_SELENIUM)
        );

        int timeout = Integer.parseInt(
                configurationProvider.getConfigurationProperty(ConfigurationProvider.PROPERTY_JS_EXECUTION_TIMEOUT)
        );

        Set<Job> flaggedJobs = Keywordr.flagJobsWithKeywords(jobs, techKeywords, useSelenium, timeout);

        logger.info("Job flag process done.");

        return flaggedJobs;
    }
}
