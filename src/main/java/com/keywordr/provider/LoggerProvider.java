package com.keywordr.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerProvider {
    private static final String SLF4J_LOG_FILE = "org.slf4j.simpleLogger.logFile";
    private static final String SLF4J_DEFAULT_LOG_LEVEL = "org.slf4j.simpleLogger.defaultLogLevel";
    private static final String SLF4J_SHOW_DATE_TIME = "org.slf4j.simpleLogger.showDateTime";
    private static final String SLF4J_DATE_TIME_FORMAT = "org.slf4j.simpleLogger.dateTimeFormat";

    public static <T> Logger newInstance(Class<T> tClass) throws IOException {
        if (System.getProperty(SLF4J_LOG_FILE) == null) {
            initializeLogging();
        }

        return LoggerFactory.getLogger(tClass);
    }

    private static void initializeLogging() throws IOException {
        // Generate unique log file path
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String directory = "../logs";
        String logFile = directory + "/keywordr-execution-" + timestamp + ".log";

        // Create path
        Files.createDirectories(Path.of(directory));

        // Create file
        Files.createFile(Path.of(logFile));

        // Set the system property before initializing the logger
        System.setProperty(SLF4J_LOG_FILE, logFile);
        System.setProperty(SLF4J_DEFAULT_LOG_LEVEL,
                ConfigurationProvider
                        .getInstance()
                        .getConfigurationProperty(ConfigurationProvider.PROPERTY_DEFAULT_LOG_LEVEL)
        );
        System.setProperty(SLF4J_SHOW_DATE_TIME, "true");
        System.setProperty(SLF4J_DATE_TIME_FORMAT, "yyyy-MM-dd HH:mm:ss");
    }
}
