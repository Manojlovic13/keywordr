package com.keywordr.process.impl;

import com.keywordr.exception.KeywordrRuntimeException;
import com.keywordr.process.api.ExecutionPlan;
import com.keywordr.provider.ConfigurationProvider;

import java.util.List;
import java.util.Map;

public class ConfigurationInitializationProcess extends ExecutionPlan<Boolean> {
    @Override
    public void initialize(Map<String, Object> initializer) {
    }

    @Override
    public Boolean execute() {
        ConfigurationProvider configurationProvider;

        try {
            configurationProvider = ConfigurationProvider.getInstance();
        } catch (KeywordrRuntimeException e) {
            System.out.println(e.getMessage());
            return false;
        }

        List<String> errorList = configurationProvider.initialize();

        if (!errorList.isEmpty()) {
            System.out.println("Faulty configuration values detected...");
            errorList.forEach(System.out::println);
            return false;
        }

        return true;
    }
}
