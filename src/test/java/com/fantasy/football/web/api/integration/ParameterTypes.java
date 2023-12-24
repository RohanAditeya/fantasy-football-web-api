package com.fantasy.football.web.api.integration;

import io.cucumber.java.ParameterType;

public class ParameterTypes {

    @ParameterType(value = ".*", name = "boolean")
    public boolean booleanParameterType (String value) {
        return Boolean.parseBoolean(value);
    }
}