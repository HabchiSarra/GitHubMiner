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
package fr.inria.sniffer.githubminer.model;

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
