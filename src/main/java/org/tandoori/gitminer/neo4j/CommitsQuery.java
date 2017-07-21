package org.tandoori.gitminer.neo4j;

import org.neo4j.cypher.CypherException;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.io.IOException;

/**
 * Created by sarra on 21/07/17.
 */
public class CommitsQuery extends Query {

    private CommitsQuery(QueryEngine queryEngine) {
        super(queryEngine);
    }

    public static CommitsQuery createCommitsQuery(QueryEngine queryEngine) {
        return new CommitsQuery(queryEngine);
    }

    @Override
    public void execute() throws CypherException, IOException {
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = "match(d:Developer)-[:AUTHORED]->(c:Commit) where d.id<0 " +
                    "return c.sha as key, d.login as login, d.id as id, d.mail as mail";

            Result result = graphDatabaseService.execute(query);
            queryEngine.resultToCSV(result, "COMMITS.csv");
        }
    }
}
