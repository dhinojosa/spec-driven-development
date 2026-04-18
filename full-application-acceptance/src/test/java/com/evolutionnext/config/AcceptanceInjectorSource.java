package com.evolutionnext.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.cucumber.guice.CucumberModules;
import io.cucumber.guice.InjectorSource;

public final class AcceptanceInjectorSource implements InjectorSource {
    @Override
    public Injector getInjector() {
        return Guice.createInjector(CucumberModules.createScenarioModule());
    }
}
