package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by sarra on 13/02/17.
 */
public class Repository {
    private String name;
    private Developer owner;
    private HashMap<Long, Developer> contributors;
    private HashMap<Long,Milestone> milestones;
    private ArrayList<Developer> collaborators;
    private ArrayList<PullRequest> pullRequests;
    private int stargazersCount;
    private int watchersCount;
    private String description;
    private Date commitDate;
    private Date pushDate;
    private ArrayList<Commit> commits;
    private Long ID;
    private ArrayList<Issue> issues;

    public static Repository createRepository(Long ID, String name, Developer owner, String description, int stargazersCount,int watchersCount, Date commitDate,
                                              Date pushDate ){
        Repository repository=new Repository(ID,name,owner,description,stargazersCount,watchersCount,commitDate,pushDate);
        owner.addRepository(repository);
        return repository;
    }

    private Repository(Long ID, String name, Developer owner, String description, int stargazersCount,int watchersCount, Date commitDate,
                       Date pushDate ) {
        this.ID=ID;
        this.name = name;
        this.owner = owner;
        this.contributors=new HashMap<>();
        this.collaborators=new ArrayList<>();
        this.stargazersCount=stargazersCount;
        this.watchersCount=watchersCount;
        this.commitDate=commitDate;
        this.pushDate=pushDate;
        this.description=description;
        this.commits=new ArrayList<>();
        this.issues=new ArrayList<>();
        this.milestones =new HashMap<>();
        this.pullRequests=new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Developer getOwner() {
        return owner;
    }

    public void setOwner(Developer owner) {
        this.owner = owner;
    }


    public HashMap<Long,Developer> getContributers() {
        return contributors;
    }


    public int getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public int getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(int watchersCount) {
        this.watchersCount = watchersCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
    }

    public Date getPushDate() {
        return pushDate;
    }

    public void setPushDate(Date pushDate) {
        this.pushDate = pushDate;
    }


    public void addCommit(Commit commit){
         this.commits.add(commit);
    }

    public void addContributor(Developer developer){
        this.contributors.put(developer.getID(),developer);
    }

    public void print(){
        System.out.println("______________________________________________________________________");
        System.out.println("ID : "+ this.ID);
        System.out.println("name: "+ this.name);
        System.out.println("description: "+this.description);
        System.out.println("commit date: "+ this.commitDate.toString());
        System.out.println("owner: ");
        System.out.println("+++++++++++++++++++++++++++++++++++++++");
        this.owner.print();
        System.out.println("+++++++++++++++++++++++++++++++++++++++");

        System.out.println("Commits: ");
        for(Commit commit:this.commits){
            System.out.println("***************************************");
            commit.print();
            System.out.println("***************************************");
        }
        System.out.println("______________________________________________________________________");
    }

    public ArrayList<Developer> getCollaborators() {
        return collaborators;
    }

    public void addCollaborator(Developer collaborator) {
        this.collaborators.add(collaborator) ;
    }

    public void setCollaborators(ArrayList<Developer> collaborators) {
        this.collaborators = collaborators;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public HashMap<Long,Developer> getContributors() {
        return contributors;
    }

    public ArrayList<Commit> getCommits() {
        return commits;
    }

    public ArrayList<Issue> getIssues() {
        return issues;
    }

    public void addIssue(Issue issue){
        this.issues.add(issue);
    }

    public void setContributors(HashMap<Long, Developer> contributors) {
        this.contributors = contributors;
    }

    public HashMap<Long, Milestone> getMilestones() {
        return milestones;
    }

    public void setMilestones(HashMap<Long, Milestone> milestones) {
        this.milestones = milestones;
    }

    public void setCommits(ArrayList<Commit> commits) {
        this.commits = commits;
    }

    public ArrayList<PullRequest> getPullRequests() {
        return pullRequests;
    }

    public void addPullRequest(PullRequest pullRequest) {
        this.pullRequests.add(pullRequest);
    }

}
