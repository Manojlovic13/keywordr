package com.keywordr.process.impl;

import com.keywordr.data.Job;
import com.keywordr.exception.KeywordrRuntimeException;
import com.keywordr.process.api.ExecutionPlan;
import com.keywordr.provider.ConfigurationProvider;
import com.keywordr.util.OutputFileWriter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExportToFileProcess extends ExecutionPlan<Integer> {
    private ConfigurationProvider configurationProvider;
    private Set<Job> jobs;
    @Override
    public void initialize(Map<String, Object> initializer) {
        try {
            configurationProvider = (ConfigurationProvider) initializer.get("configurationProvider");
            jobs = (HashSet<Job>) initializer.get("jobs");
        } catch (ClassCastException e) {
            throw new KeywordrRuntimeException("Failed to initialize ExportToFileProcess with message: " + e.getMessage());
        }
    }

    @Override
    public Integer execute() {
        if (configurationProvider == null) {
            throw new KeywordrRuntimeException("ExportToFileProcess is not initialized!");
        }

        String outputFile = configurationProvider.getConfigurationProperty(
                                ConfigurationProvider.PROPERTY_OUTPUT_JSON_FILE
        );

        String outputMode = configurationProvider.getConfigurationProperty(
                ConfigurationProvider.PROPERTY_OUTPUT_MODE
        );

        System.out.println("ExportToFileProcess outputFile: " + outputFile + ", outputMode: " + outputMode);

        OutputFileWriter.writeToJson(jobs,outputFile,"jobs", outputMode);

        return 1;
    }
}
