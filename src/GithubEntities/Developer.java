package GithubEntities;

import java.util.ArrayList;

/**
 * Created by sarra on 13/02/17.
 */
public class Developer {
    private String login;
    private Long ID;
    private ArrayList<Repository> repositories;
    private ArrayList<Commit> commits;
    private ArrayList<Commit> authoredCommits;
    private String mail;



    private Developer(String login, Long ID, String mail) {
        this.login = login;
        this.ID = ID;
        this.mail=mail;
        this.repositories=new ArrayList<>();
        this.commits=new ArrayList<>();
        this.authoredCommits=new ArrayList<>();
    }

    public static Developer createDeveloper(String login, Long ID, String mail){
        return new Developer(login,ID,mail);
    }
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }


    public ArrayList<Repository> getRepositories() {
        return repositories;
    }

    public void setRepositories(ArrayList<Repository> repositories) {
        this.repositories = repositories;
    }

    public void addRepository(Repository repository){
        this.repositories.add(repository);
    }

    public void addCommit(Commit commit){
        this.commits.add(commit);
    }

    public void addAuthoredCommit(Commit commit){
        this.authoredCommits.add(commit);
    }
}
