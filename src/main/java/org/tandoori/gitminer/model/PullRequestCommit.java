package org.tandoori.gitminer.model;

import java.util.Date;

/**
 * Created by sarra on 19/04/17.
 */
public class PullRequestCommit extends Commit {
    private PullRequestCommit(String sha, Developer author, Developer committer, String message, Date authoringDate,
                              Date commitDate, Repository repository) {
        super(sha, author, committer, message, authoringDate, commitDate, repository);
    }


    public PullRequestCommit createPullRequestCommit(String sha, Developer author, Developer committer, String message,
                                                      Date authoringDate, Date commitDate, Repository repository, PullRequest pullRequest){
        PullRequestCommit pullRequestCommit =new PullRequestCommit(sha, author, committer, message, authoringDate,
                commitDate, repository);
        if(committer!=null){
            committer.addPullRequestCommit(pullRequestCommit);
        }
        pullRequest.addCommit(pullRequestCommit);
        return pullRequestCommit;
     }

}
