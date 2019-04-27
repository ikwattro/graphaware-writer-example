package com.graphaware.neo4j.writer.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphaware.common.representation.DetachedEntity;
import com.graphaware.common.representation.DetachedRelationship;
import com.graphaware.writer.thirdparty.BaseThirdPartyWriter;
import com.graphaware.writer.thirdparty.WriteOperation;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class ExampleWriter extends BaseThirdPartyWriter {

    private final GraphDatabaseService database;
    private final ObjectMapper MAPPER = new ObjectMapper();

    public ExampleWriter(GraphDatabaseService database) {
        this.database = database;
    }

    @Override
    protected void processOperations(List<Collection<WriteOperation<?>>> list) {

        // Retrieve all the relationships created in the transaction
        List<DetachedRelationship> createdRelationships = new ArrayList<>();
        for (Collection<WriteOperation<?>> operations : list) {
            operations.stream().forEach(writeOperation -> {
                if (writeOperation.getType().equals(WriteOperation.OperationType.RELATIONSHIP_CREATED)) {
                    createdRelationships.add((DetachedRelationship) writeOperation.getDetails());
                }
            });
        }

        // Get more infos of the start and end nodes in batch
        List<Map<String, Object>> improvedOperations = getImprovedOperationInfosInBatch(createdRelationships);

        // Do something ( example here, will just log to console )
        writeOperations(improvedOperations);
    }

    private void writeOperations(List<Map<String, Object>> operations) {
        operations.forEach(o -> {
            try {
                System.out.println(MAPPER.writeValueAsString(o));
            } catch (Exception e) {
                //
            }
        });
    }

    private List<Map<String, Object>> getImprovedOperationInfosInBatch(List<DetachedRelationship> detachedRelationships) {
        String query = "UNWIND $ids AS id " +
                "MATCH (n)-[r]->(x) " +
                "WHERE id(r) = id " +
                "RETURN {startNodeUuid: n.uuid, startNodeId: id(n), relationshipId: id, endNodeUuid: x.uuid, endNodeId: id(x)} AS result";

        List<Long> relIds =
                detachedRelationships
                .stream()
                .map(DetachedEntity::getGraphId)
                .collect(Collectors.toList());

        List<Map<String, Object>> improvedOperations = new ArrayList<>();

        try (Transaction tx = database.beginTx()) {
            String qx = "MATCH (n) RETURN count(n)";
            Result result = database.execute(query, Collections.singletonMap("ids", relIds));
            while (result.hasNext()) {
                Map<String, Object> record = result.next();
                improvedOperations.add((Map<String, Object>) record.get("result"));
            }
            tx.success();
        }

        return improvedOperations;
    }
}
