package com.keywordr.provider;

import com.keywordr.exception.KeywordrRuntimeException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationProvider {
    private static ConfigurationProvider configurationProvider;
    private final Properties CONFIGURATION_FILE;
    public static final String PROPERTY_COMPANY_JSON_FILE = "company_json_file_path";
    public static final String PROPERTY_TECH_KEYWORDS_CSV_FILE = "tech_keywords_file_path";
    public static final String PROPERTY_JOB_KEYWORDS_CSV_FILE = "job_keywords_file_path";
    public static final String PROPERTY_USE_SELENIUM = "use_selenium";
    public static final String PROPERTY_JS_EXECUTION_TIMEOUT = "js_execution_timeout";
    public static final String PROPERTY_OUTPUT_JSON_FILE = "output_json_file_path";
    public static final String PROPERTY_OUTPUT_MODE = "output_mode";
    public static final String PROPERTY_DEFAULT_LOG_LEVEL = "default_log_level";
    public static final String PROPERTY_FIRESTORE_ACCOUNT_KEY = "firestore_service_account_key_path";

    private ConfigurationProvider() {
        String CONFIGURATION_FILE_PATH;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            CONFIGURATION_FILE_PATH = "..\\configuration\\keywordr_configuration.ini";
        } else {
            CONFIGURATION_FILE_PATH = "../configuration/keywordr_configuration.ini";
        }

        try(InputStream inputStream = new FileInputStream(CONFIGURATION_FILE_PATH)) {
            CONFIGURATION_FILE = new Properties();
            CONFIGURATION_FILE.load(inputStream);
        } catch (IOException e) {
            throw new KeywordrRuntimeException("Failed to load configuration file: " + CONFIGURATION_FILE_PATH);
        }
    }

    public static ConfigurationProvider getInstance() {
        if (configurationProvider == null) {
            configurationProvider = new ConfigurationProvider();
        }
        return configurationProvider;
    }

    public String getConfigurationProperty(String key) {
        return CONFIGURATION_FILE.getProperty(key);
    }
}
