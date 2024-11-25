package com.keywordr.provider;

import com.keywordr.exception.KeywordrRuntimeException;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ConfigurationProvider {
    private static ConfigurationProvider configurationProvider;
    private final Properties configurationFile;
    private final String LINUX_SYSTEM_PATH_REGEX = "^(\\/[^\\/\\\0]+)*\\/?$\n";
    private final String WINDOWS_SYSTEM_PATH_REGEX = "^(?:\\.\\.\\\\|[a-zA-Z]:\\\\)(?:[^<>:\"\\\\|?*\\n\\r]+\\\\)*[^<>:\"\\\\|?*\\n\\r]+$";
    private final String[] BOOLEAN_VALID_VALUES = {"true", "false", "1", "0"};
    private final String[] OUTPUT_MODE_VALID_VALUES = {"delete", "append"};
    private final String[] LOG_LEVEL_VALID_VALUES = {"debug", "info", "off"};
    private final Map<String, String> configurationMap;
    public static final String PROPERTY_COMPANY_JSON_FILE = "company_json_file_path";
    public static final String PROPERTY_TECH_KEYWORDS_CSV_FILE = "tech_keywords_file_path";
    public static final String PROPERTY_JOB_KEYWORDS_CSV_FILE = "job_keywords_file_path";
    public static final String PROPERTY_USE_SELENIUM = "use_selenium";
    public static final String PROPERTY_JS_EXECUTION_TIMEOUT = "js_execution_timeout";
    public static final String PROPERTY_OUTPUT_JSON_FILE = "output_json_file_path";
    public static final String PROPERTY_OUTPUT_MODE = "output_mode";
    public static final String PROPERTY_DEFAULT_LOG_LEVEL = "default_log_level";
    public static final String PROPERTY_FIRESTORE_ACCOUNT_KEY = "firestore_service_account_key_path";
    public static final String PROPERTY_FIRESTORE_UPLOAD = "firestore_upload";
    public static final String PROPERTY_DATABASE_URL = "firestore_database_url";
    public static final String PROPERTY_COLLECTION_NAME = "firestore_collection_name";
    public static final String PROPERTY_DOCUMENT_ID = "firestore_document_id";

    private ConfigurationProvider() {
        configurationMap = new HashMap<>();
        String configurationFilePath;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            configurationFilePath = "..\\configuration\\keywordr_configuration.properties";
        } else {
            configurationFilePath = "../configuration/keywordr_configuration.properties";
        }

        try(InputStream inputStream = new FileInputStream(configurationFilePath)) {
            configurationFile = new Properties();
            configurationFile.load(inputStream);
        } catch (IOException e) {
            throw new KeywordrRuntimeException("Failed to load configuration file: " + configurationFilePath);
        }
    }

    public static ConfigurationProvider getInstance() {
        if (configurationProvider == null) {
            configurationProvider = new ConfigurationProvider();
        }
        return configurationProvider;
    }

    public String getConfigurationProperty(String key) {
        return configurationMap.get(key);
    }

    public List<String> initialize() {
        List<String> configurationValidationList = new ArrayList<>();
        // read and validate 'company_json_file_path'
        String valueCompanyJsonFile = configurationFile.getProperty(PROPERTY_COMPANY_JSON_FILE).strip();
        configurationMap.put(PROPERTY_COMPANY_JSON_FILE, valueCompanyJsonFile);
        if (!(valueCompanyJsonFile.matches(LINUX_SYSTEM_PATH_REGEX) ||
                valueCompanyJsonFile.matches(WINDOWS_SYSTEM_PATH_REGEX))) {
            configurationValidationList.add("Non valid configuration value provided for 'company_json_file_path'!");
        }

        // read and validate 'job_keywords_file_path'
        String valueJobKeywordsCsvFile = configurationFile.getProperty(PROPERTY_JOB_KEYWORDS_CSV_FILE).strip();
        configurationMap.put(PROPERTY_JOB_KEYWORDS_CSV_FILE, valueJobKeywordsCsvFile);
        if (!(valueJobKeywordsCsvFile.matches(LINUX_SYSTEM_PATH_REGEX) ||
                valueJobKeywordsCsvFile.matches(WINDOWS_SYSTEM_PATH_REGEX))) {
            configurationValidationList.add("Non valid configuration value provided for 'job_keywords_file_path'!");
        }

        // read and validate 'tech_keywords_file_path'
        String valueTechKeywordsCsvFile = configurationFile.getProperty(PROPERTY_TECH_KEYWORDS_CSV_FILE).strip();
        configurationMap.put(PROPERTY_TECH_KEYWORDS_CSV_FILE, valueTechKeywordsCsvFile);
        if (!(valueTechKeywordsCsvFile.matches(LINUX_SYSTEM_PATH_REGEX) ||
                valueTechKeywordsCsvFile.matches(WINDOWS_SYSTEM_PATH_REGEX))) {
            configurationValidationList.add("Non valid configuration value provided for 'tech_keywords_file_path'!");
        }

        // read and validate 'use_selenium'
        String valueUseSelenium = configurationFile.getProperty(PROPERTY_USE_SELENIUM).strip();
        configurationMap.put(PROPERTY_USE_SELENIUM, valueUseSelenium);
        if (Arrays.stream(BOOLEAN_VALID_VALUES).noneMatch(a -> a.equalsIgnoreCase(valueUseSelenium))) {
            configurationValidationList.add("Non valid configuration value provided for 'use_selenium'!");
        }

        // read and validate 'js_execution_timeout'
        String valueJsExecutionTimeout = configurationFile.getProperty(PROPERTY_JS_EXECUTION_TIMEOUT).strip();
        configurationMap.put(PROPERTY_JS_EXECUTION_TIMEOUT, valueJsExecutionTimeout);
        if (!NumberUtils.isCreatable(valueJsExecutionTimeout)) {
            configurationValidationList.add("Non valid configuration value provided for 'js_execution_timeout'!");
        }

        // read and validate 'output_json_file_path'
        String valueOutputJsonFile = configurationFile.getProperty(PROPERTY_OUTPUT_JSON_FILE).strip();
        configurationMap.put(PROPERTY_OUTPUT_JSON_FILE, valueOutputJsonFile);
        if (!(valueOutputJsonFile.matches(LINUX_SYSTEM_PATH_REGEX) ||
                valueOutputJsonFile.matches(WINDOWS_SYSTEM_PATH_REGEX) ||
                valueOutputJsonFile.isBlank())) {
            configurationValidationList.add("Non valid configuration value provided for 'output_json_file_path'!");
        }

        // read and validate 'output_mode'
        String valueOutputMode = configurationFile.getProperty(PROPERTY_OUTPUT_MODE).strip();
        configurationMap.put(PROPERTY_OUTPUT_MODE, valueOutputMode);
        if (Arrays.stream(OUTPUT_MODE_VALID_VALUES).noneMatch(a -> a.equalsIgnoreCase(valueOutputMode))) {
            configurationValidationList.add("Non valid configuration value provided for 'output_mode'!");
        }

        // read and validate 'firestore_upload'
        String valueFirestoreUpload = configurationFile.getProperty(PROPERTY_FIRESTORE_UPLOAD).strip();
        configurationMap.put(PROPERTY_FIRESTORE_UPLOAD, valueFirestoreUpload);
        if (Arrays.stream(BOOLEAN_VALID_VALUES).noneMatch(a -> a.equalsIgnoreCase(valueFirestoreUpload))) {
            configurationValidationList.add("Non valid configuration value provided for 'firestore_upload'!");
        }

        if (Boolean.parseBoolean(valueFirestoreUpload)) {
            // read and validate 'firestore_service_account_key_path'
            String valueFirestoreAccountKey = configurationFile.getProperty(PROPERTY_FIRESTORE_ACCOUNT_KEY).strip();
            configurationMap.put(PROPERTY_FIRESTORE_ACCOUNT_KEY, valueFirestoreAccountKey);
            if (!(valueFirestoreAccountKey.matches(LINUX_SYSTEM_PATH_REGEX) ||
                    valueFirestoreAccountKey.matches(WINDOWS_SYSTEM_PATH_REGEX))) {
                configurationValidationList.add("Non valid configuration value provided for 'firestore_service_account_key_path'!");
            }

            // read and validate 'firestore_database_url'
            String valueDatabseUrl = configurationFile.getProperty(PROPERTY_DATABASE_URL).strip();
            configurationMap.put(PROPERTY_DATABASE_URL, valueDatabseUrl);
            if (valueDatabseUrl.isEmpty()) {
                configurationValidationList.add("Firestore is enabled but database url is not defined. Please define database url!");
            }

            // read and validate 'firestore_collection_name'
            String valueCollectionName = configurationFile.getProperty(PROPERTY_COLLECTION_NAME).strip();
            configurationMap.put(PROPERTY_COLLECTION_NAME, valueCollectionName);
            if (valueCollectionName.isEmpty()) {
                configurationValidationList.add("Firestore is enabled but collection name is not defined. Please define collection name!");
            }

            // read and validate 'firestore_document_id'
            String valueDocumentId = configurationFile.getProperty(PROPERTY_DOCUMENT_ID).strip();
            configurationMap.put(PROPERTY_DOCUMENT_ID, valueDocumentId);
            if (valueDocumentId.isEmpty()) {
                configurationValidationList.add("Firestore is enabled but document ID is not defined. Please define document ID!");
            }
        }

        // read and validate 'default_log_level'
        String valueDefaultLogLevel = configurationFile.getProperty(PROPERTY_DEFAULT_LOG_LEVEL);
        configurationMap.put(PROPERTY_DEFAULT_LOG_LEVEL, valueDefaultLogLevel);
        if (Arrays.stream(LOG_LEVEL_VALID_VALUES).noneMatch(a -> a.equalsIgnoreCase(valueDefaultLogLevel))) {
            configurationValidationList.add("Non valid configuration value provided for 'default_log_level'!");
        }

        return configurationValidationList;
    }
}
