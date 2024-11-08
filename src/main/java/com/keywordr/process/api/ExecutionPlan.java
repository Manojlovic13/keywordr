package com.keywordr.process.api;

import java.util.Map;

public abstract class ExecutionPlan<T> {
    public abstract void initialize(Map<String, Object> initializer);
    public abstract T execute();
}
