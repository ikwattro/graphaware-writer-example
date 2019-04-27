package com.graphaware.neo4j.writer.example;

import com.graphaware.runtime.module.thirdparty.DefaultThirdPartyIntegrationModule;
import org.neo4j.graphdb.GraphDatabaseService;

public class ExampleModule extends DefaultThirdPartyIntegrationModule {

    public ExampleModule(String moduleId, GraphDatabaseService database) {
        super(moduleId, new ExampleWriter(database));
    }




}
