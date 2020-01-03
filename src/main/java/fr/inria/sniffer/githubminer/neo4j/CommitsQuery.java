/**
 *   Sniffer - Analyze the history of Android code smells at scale.
 *   Copyright (C) 2019 Sarra Habchi
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package fr.inria.sniffer.githubminer.neo4j;

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
            String query = "match(d:Developer)-[:AUTHORED]->(c:Commit) " +
                    "return c.sha as key, d.login as login, d.id as id, d.mail as mail";

            Result result = graphDatabaseService.execute(query);
            queryEngine.resultToCSV(result, "COMMITS.csv");
        }
    }
}
