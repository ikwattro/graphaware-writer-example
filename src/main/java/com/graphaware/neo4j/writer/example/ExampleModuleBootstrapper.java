package com.graphaware.neo4j.writer.example;

import com.graphaware.runtime.module.BaseRuntimeModuleBootstrapper;
import com.graphaware.runtime.module.RuntimeModule;
import org.neo4j.graphdb.GraphDatabaseService;

import java.util.Map;

public class ExampleModuleBootstrapper extends BaseRuntimeModuleBootstrapper<ExampleModuleConfiguration> {

    @Override
    protected ExampleModuleConfiguration defaultConfiguration() {
        return ExampleModuleConfiguration.defaultConfiguration();
    }

    @Override
    protected RuntimeModule doBootstrapModule(String s, Map<String, String> map, GraphDatabaseService graphDatabaseService, ExampleModuleConfiguration exampleModuleConfiguration) {
        return new ExampleModule(s, graphDatabaseService);
    }
}
