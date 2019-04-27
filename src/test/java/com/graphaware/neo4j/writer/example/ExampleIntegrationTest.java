package com.graphaware.neo4j.writer.example;


import com.graphaware.runtime.GraphAwareRuntime;
import com.graphaware.runtime.GraphAwareRuntimeFactory;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

public class ExampleIntegrationTest {

    @Test
    public void exampleTest() throws Exception {
        GraphDatabaseService database = new TestGraphDatabaseFactory().newImpermanentDatabase();
        GraphAwareRuntime runtime = GraphAwareRuntimeFactory.createRuntime(database);
        runtime.registerModule(new ExampleModule("EX", database));
        runtime.start();
        runtime.waitUntilStarted();


        try (Transaction tx = database.beginTx()) {
            database.execute("UNWIND range(0, 100) AS i " +
                    "CREATE (a:Node {uuid: 'hello' + i})-[r:RELATES]->(x:Node2 {uuid: 'hello2' + i})");
            tx.success();
        }

        Thread.sleep(1000);

    }

}
