package neo4j;

import model.*;
import org.neo4j.graphdb.*;

import javax.management.relation.RelationType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public  class ModelToGraph {
    private GraphDatabaseService graphDatabaseService;
    private DatabaseManager databaseManager;
    private static final Label repositoryLabel = DynamicLabel.label("Repository");
    private static final Label developerLabel = DynamicLabel.label("Developer");
    private static final Label commitLabel = DynamicLabel.label("Commit");
    private static final Label fileModificationLabel = DynamicLabel.label("FileModification");
    private static final Label issueLabel = DynamicLabel.label("Issue");
    private static final Label pullRequestLabel = DynamicLabel.label("PullRequest");


    private Map<Developer,Node> developerNodeMap;
    private Map<Commit,Node> commitNodeMap;


    public ModelToGraph(String DatabasePath){
        this.databaseManager = new DatabaseManager(DatabasePath);
        this.databaseManager.start();
        this.graphDatabaseService = databaseManager.getGraphDatabaseService();
        this.developerNodeMap = new HashMap<>();
        this.commitNodeMap=new HashMap<>();

    }

    public Node insertRepository(Repository repository){

        Node repoNode;
        try ( Transaction tx = graphDatabaseService.beginTx() ){
            repoNode = graphDatabaseService.createNode(repositoryLabel);
            repoNode.setProperty("name",repository.getName());
            repoNode.setProperty("id",repository.getID());
            repoNode.setProperty("stargazers_count=",repository.getStargazersCount());
            repoNode.setProperty("watchers_count",repository.getWatchersCount());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            repoNode.setProperty("committed_at",simpleDateFormat.format(repository.getCommitDate()));
            repoNode.setProperty("pushed_at",simpleDateFormat.format(repository.getPushDate()));
            Node ownerNode= insertDeveloper(repository.getOwner());
            ownerNode.createRelationshipTo(repoNode,RelationTypes.OWNS);

            for(Commit commit: repository.getCommits()){
                repoNode.createRelationshipTo(insertCommit(commit),RelationTypes.HAS_COMMIT);
            }

            for(Developer developer:repository.getCollaborators()){
                insertDeveloper(developer).createRelationshipTo(repoNode,RelationTypes.COLLABORATED_TO);
            }

            for(Issue issue: repository.getIssues()){
                repoNode.createRelationshipTo(insertIssue(issue,repoNode), RelationTypes.HAS_ISSUE);
            }
            tx.success();
        }
        try ( Transaction tx = graphDatabaseService.beginTx() ){
            tx.success();
        }
        return repoNode;
    }


    public Node insertDeveloper(Developer developer){
        Node developerNode;
        if((developerNode = this.developerNodeMap.get(developer))!=null){
            return developerNode;
        }
        try ( Transaction tx = graphDatabaseService.beginTx() ){
            developerNode = graphDatabaseService.createNode(developerLabel);
            developerNode.setProperty("login",developer.getLogin());
            developerNode.setProperty("id",developer.getID());
            if(developer.getMail()!=null)
            {
                developerNode.setProperty("mail=",developer.getMail());
            }

            tx.success();
        }
        try ( Transaction tx = graphDatabaseService.beginTx() ){
            tx.success();
        }
        this.developerNodeMap.put(developer,developerNode);
        return developerNode;
    }

    public Node insertCommit(Commit commit){
        Node commitNode;
        Node fileModificationNode;
        if((commitNode = this.commitNodeMap.get(commit))!=null){
            return commitNode;
        }
        try ( Transaction tx = graphDatabaseService.beginTx() ){
            commitNode = graphDatabaseService.createNode(commitLabel);
            commitNode.setProperty("sha",commit.getSha());
            commitNode.setProperty("message",commit.getMessage());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            if(commit.getCommitDate()!=null)
            {
                commitNode.setProperty("committed_at",simpleDateFormat.format(commit.getCommitDate()));
            }
            if(commit.getAuthoringDate()!=null)
            {
                commitNode.setProperty("authored_at",simpleDateFormat.format(commit.getAuthoringDate()));
            }

            if(commit.getAuthor()!=null) {
                Node authorNode = insertDeveloper(commit.getAuthor());
                authorNode.createRelationshipTo(commitNode, RelationTypes.AUTHORED);
            }
            if(commit.getCommiter()!=null) {
                Node committerNode = insertDeveloper(commit.getCommiter());
                committerNode.createRelationshipTo(commitNode, RelationTypes.COMMITTED);
            }
            for(FileModification fileModification: commit.getFileModifications()){
                fileModificationNode =insertFileModification(fileModification);
                commitNode.createRelationshipTo(fileModificationNode,RelationTypes.MAKES_FILE_MODIFICATION);
            }
            tx.success();
        }
        try ( Transaction tx = graphDatabaseService.beginTx() ){
            tx.success();
        }
        this.commitNodeMap.put(commit,commitNode);
        return commitNode;
    }


    public Node insertFileModification(FileModification fileModification){
        Node fileModificationNode;
        try ( Transaction tx = graphDatabaseService.beginTx() ){
            fileModificationNode = graphDatabaseService.createNode(fileModificationLabel);
            fileModificationNode.setProperty("file_name",fileModification.getFileName());
            fileModificationNode.setProperty("additions",fileModification.getAdditions());
            fileModificationNode.setProperty("deletions",fileModification.getDeletions());
            fileModificationNode.setProperty("sha",fileModification.getSha());
            fileModificationNode.setProperty("status",fileModification.getStatus().toString().toLowerCase());
            tx.success();
        }
        try ( Transaction tx = graphDatabaseService.beginTx() ){
            tx.success();
        }
        return fileModificationNode;
    }


    public Node insertIssue(Issue issue, Node repoNode){
        Node issueNode;
        Node pullRequestNode;

        try ( Transaction tx = graphDatabaseService.beginTx() ){
            issueNode = graphDatabaseService.createNode(issueLabel);
            issueNode.setProperty("number",issue.getNumber());
            issueNode.setProperty("id",issue.getId());
            issueNode.setProperty("title",issue.getTitle());
            issueNode.setProperty("body",issue.getBody());
            issueNode.setProperty("state",issue.getState().toString().toLowerCase());
            if(issue.getPullRequest()!=null){
                pullRequestNode=insertPullRequest(issue.getPullRequest());
                issueNode.createRelationshipTo(pullRequestNode,RelationTypes.HAS_PULL_REQUEST);
            }
            tx.success();
        }
        try ( Transaction tx = graphDatabaseService.beginTx() ){
            tx.success();
        }
        return issueNode;
    }

    public Node insertPullRequest(PullRequest pullRequest){
        Node pullRequestNode;
        try ( Transaction tx = graphDatabaseService.beginTx() ){
            pullRequestNode = graphDatabaseService.createNode(pullRequestLabel);
            pullRequestNode.setProperty("id",pullRequest.getID());
            pullRequestNode.setProperty("additions",pullRequest.getAdditions());
            pullRequestNode.setProperty("deletions",pullRequest.getDeletions());
            pullRequestNode.setProperty("changed_files",pullRequest.getChangedFiles());
            pullRequestNode.setProperty("maintainer_can_modify",pullRequest.isMaintainerCanModify());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            pullRequestNode.setProperty("created_at",simpleDateFormat.format(pullRequest.getCreatedAT()));
            if(pullRequest.getUpdatedAt()!=null)
            {
                pullRequestNode.setProperty("updated_at",simpleDateFormat.format(pullRequest.getUpdatedAt()));
            }
            if(pullRequest.getClosedAt()!=null)
            {
                pullRequestNode.setProperty("closed_at",simpleDateFormat.format(pullRequest.getClosedAt()));
            }
            if(pullRequest.getMergedAt()!=null)
            {
                pullRequestNode.setProperty("merged_at",simpleDateFormat.format(pullRequest.getMergedAt()));
            }
            if(pullRequest.isMergeable()!=null)
            {
                pullRequestNode.setProperty("mergeable",pullRequest.isMergeable());
            }
            if(pullRequest.isMerged()!=null)
            {
                pullRequestNode.setProperty("merged",pullRequest.isMerged());
            }
            tx.success();
        }
        try ( Transaction tx = graphDatabaseService.beginTx() ){
            tx.success();
        }
        return pullRequestNode;
    }





}
