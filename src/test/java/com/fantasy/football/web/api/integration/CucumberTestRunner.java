package com.fantasy.football.web.api.integration;

import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@AutoConfigureMockMvc
@IncludeEngines("cucumber")
@CucumberContextConfiguration
@SelectClasspathResource("feature")
@SpringBootTest(properties = {"spring.profiles.include=int"})
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.fantasy.football.web.api.integration")
public class CucumberTestRunner {
}