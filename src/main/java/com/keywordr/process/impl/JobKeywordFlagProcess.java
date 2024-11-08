package com.keywordr.process.impl;

import com.keywordr.data.Job;
import com.keywordr.exception.KeywordrRuntimeException;
import com.keywordr.process.api.ExecutionPlan;
import com.keywordr.provider.ConfigurationProvider;
import com.keywordr.util.CsvReader;
import com.keywordr.util.Keywordr;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JobKeywordFlagProcess extends ExecutionPlan<Set<Job>> {
    private ConfigurationProvider configurationProvider;
    private HashSet<Job> jobs;
    @Override
    public void initialize(Map<String, Object> initializer) {
        try {
            configurationProvider = (ConfigurationProvider) initializer.get("configurationProvider");
            jobs = (HashSet<Job>) initializer.get("jobs");
        } catch (ClassCastException e) {
            throw new KeywordrRuntimeException("Failed to initialize JobKeywordFlagProcess with message: " + e.getMessage());
        }
    }

    @Override
    public Set<Job> execute() {
        if (configurationProvider == null || jobs == null) {
            throw new KeywordrRuntimeException("JobListingProcess is not initialized!");
        }

        HashSet<String> techKeywords =
                CsvReader.readCsvFile(
                        configurationProvider.getConfigurationProperty(
                                ConfigurationProvider.PROPERTY_TECH_KEYWORDS_CSV_FILE));

        System.out.println("You have defined " + techKeywords.size() + " tech keywords!");

        boolean useSelenium = Boolean.parseBoolean(
                configurationProvider.getConfigurationProperty(ConfigurationProvider.PROPERTY_USE_SELENIUM)
        );

        int timeout = Integer.parseInt(
                configurationProvider.getConfigurationProperty(ConfigurationProvider.PROPERTY_JS_EXECUTION_TIMEOUT)
        );

        System.out.println("Give me a minute to flag each job...");

        return Keywordr.flagJobsWithKeywords(jobs, techKeywords, useSelenium, timeout);
    }
}
