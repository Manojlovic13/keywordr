package com.keywordr.process.impl;

import com.keywordr.data.Job;
import com.keywordr.exception.KeywordrRuntimeException;
import com.keywordr.process.api.ExecutionPlan;
import com.keywordr.provider.ConfigurationProvider;
import com.keywordr.provider.LoggerProvider;
import com.keywordr.util.OutputFileWriter;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExportToFileProcess extends ExecutionPlan<Integer> {
    private ConfigurationProvider configurationProvider;
    private Set<Job> jobs;
    private Logger logger;
    @Override
    public void initialize(Map<String, Object> initializer) {
        try {
            configurationProvider = (ConfigurationProvider) initializer.get("configurationProvider");
            jobs = (HashSet<Job>) initializer.get("jobs");
            logger = LoggerProvider.newInstance(ExportToFileProcess.class);
        } catch (ClassCastException | IOException e) {
            throw new KeywordrRuntimeException("Failed to initialize ExportToFileProcess with message: " + e.getMessage());
        }
    }

    @Override
    public Integer execute() {
        if (configurationProvider == null) {
            throw new KeywordrRuntimeException("ExportToFileProcess is not initialized!");
        }

        logger.info("Executing export to file process.");

        String outputFile = configurationProvider.getConfigurationProperty(
                                ConfigurationProvider.PROPERTY_OUTPUT_JSON_FILE
        );

        String outputMode = configurationProvider.getConfigurationProperty(
                ConfigurationProvider.PROPERTY_OUTPUT_MODE
        );

        OutputFileWriter.writeToJson(jobs,outputFile,"jobs", outputMode);

        logger.info("Results successfully written to result file!");
        logger.info("Export to file process done.");

        return 1;
    }
}
