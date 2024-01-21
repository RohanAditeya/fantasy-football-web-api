package com.fantasy.football.web.api.integration.step.definitions;

import io.cucumber.java.ParameterType;

public class CucumberCustomParameterTypes {

    @ParameterType(value = "true|false", name = "boolean")
    public boolean booleanParameterType(String booleanString) {
        return Boolean.parseBoolean(booleanString);
    }
}