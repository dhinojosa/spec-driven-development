package com.evolutionnext.features.account;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com/evolutionnext/features/account")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "com.evolutionnext.features.account")
public class RunAccountAccessCucumberTest {
}
