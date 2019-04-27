package com.graphaware.neo4j.writer.example;

import com.graphaware.common.policy.inclusion.InclusionPolicies;
import com.graphaware.runtime.config.BaseTxDrivenModuleConfiguration;

public class ExampleModuleConfiguration extends BaseTxDrivenModuleConfiguration<ExampleModuleConfiguration> { ;

    private final long initializeUntil;

    public ExampleModuleConfiguration(InclusionPolicies inclusionPolicies, long initializeUntil) {
        super(inclusionPolicies);
        this.initializeUntil = initializeUntil;
    }

    public static ExampleModuleConfiguration defaultConfiguration() {
        return new ExampleModuleConfiguration(InclusionPolicies.all(), NEVER);
    }

    @Override
    protected ExampleModuleConfiguration newInstance(InclusionPolicies inclusionPolicies, long l) {
        return defaultConfiguration();
    }
}
