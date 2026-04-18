package com.evolutionnext.features.account;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com/evolutionnext/features/account")
public class RunAccountAccessCucumberTest {
}
