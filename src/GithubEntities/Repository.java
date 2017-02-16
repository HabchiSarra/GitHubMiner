package GithubEntities;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sarra on 13/02/17.
 */
public class Repository {
    private String name;
    private Developer owner;
    private ArrayList<Developer> contributors;
    private ArrayList<Developer> collaborators;
    private int stargazersCount;
    private int watchersCount;
    private String description;
    private Date commitDate;
    private Date pushDate;
    private ArrayList<Commit> commits;




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


    public ArrayList<Developer> getContributers() {
        return contributors;
    }

    public void setContributers(ArrayList<Developer> contributers) {
        this.contributors = contributers;
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

     public static Repository createRepository(String name, Developer owner, String description, int stargazersCount,int watchersCount, Date commitDate,
                                       Date pushDate ){
        Repository repository=new Repository(name,owner,description,stargazersCount,watchersCount,commitDate,pushDate);
        owner.addRepository(repository);
        return repository;
    }

    private Repository(String name, Developer owner, String description, int stargazersCount,int watchersCount, Date commitDate,
                      Date pushDate ) {
        this.name = name;
        this.owner = owner;
        this.contributors=new ArrayList<>();
        this.collaborators=new ArrayList<>();
        this.stargazersCount=stargazersCount;
        this.watchersCount=watchersCount;
        this.commitDate=commitDate;
        this.pushDate=pushDate;
        this.description=description;
        this.commits=new ArrayList<>();

    }
    public void addCommit(Commit commit){
         this.commits.add(commit);
    }

    public void addContributor(Developer developer){
        this.contributors.add(developer);
    }

}
