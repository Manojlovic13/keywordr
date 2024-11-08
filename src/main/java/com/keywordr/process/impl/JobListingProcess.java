package com.keywordr.process.impl;

import com.keywordr.data.Company;
import com.keywordr.data.Job;
import com.keywordr.exception.KeywordrRuntimeException;
import com.keywordr.process.api.ExecutionPlan;
import com.keywordr.provider.ConfigurationProvider;
import com.keywordr.util.CsvReader;
import com.keywordr.util.Keywordr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class JobListingProcess extends ExecutionPlan<HashSet<Job>> {
    private ConfigurationProvider configurationProvider;
    private ArrayList<Company> companies;
    @Override
    public void initialize(Map<String, Object> initializer) {
        try {
            configurationProvider = (ConfigurationProvider) initializer.get("configurationProvider");
            companies = (ArrayList<Company>) initializer.get("companies");
        } catch (ClassCastException e) {
            throw new KeywordrRuntimeException("Failed to initialize JobScraperProcess with message: " + e.getMessage());
        }
    }

    @Override
    public HashSet<Job> execute() {
        if (configurationProvider == null || companies == null) {
            throw new KeywordrRuntimeException("JobListingProcess is not initialized!");
        }

        HashSet<String> jobKeywords =
                CsvReader.readCsvFile(
                        configurationProvider.getConfigurationProperty(
                                ConfigurationProvider.PROPERTY_JOB_KEYWORDS_CSV_FILE));

        System.out.println("You have defined " + jobKeywords.size() + " job keywords!");


        boolean useSelenium = Boolean.parseBoolean(
                configurationProvider.getConfigurationProperty(ConfigurationProvider.PROPERTY_USE_SELENIUM)
        );

        int timeout = Integer.parseInt(
                configurationProvider.getConfigurationProperty(ConfigurationProvider.PROPERTY_JS_EXECUTION_TIMEOUT)
        );

        System.out.println("...scraping each company...");
        return Keywordr.companiesJobListingCheck(companies, jobKeywords, useSelenium, timeout);
    }
}
