package org.tandoori.gitminer.neo4j;

import org.neo4j.graphdb.RelationshipType;

/**
 * Created by sarra on 13/04/17.
 */
public enum  RelationTypes implements RelationshipType {
    HAS_COMMIT, COMMITTED, AUTHORED, MAKES_FILE_MODIFICATION, OWNS, COLLABORATED_TO, HAS_ISSUE, HAS_PULL_REQUEST,HAS_COMMENT, HAS_LABEL, WRITES_COMMENT, ASSIGNED_TO
}
