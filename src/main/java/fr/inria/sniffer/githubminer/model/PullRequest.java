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

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sarra on 27/03/17.
 */
public class PullRequest {
    Repository repository;
    Long ID;
    Date createdAT;
    Date updatedAt;
    Date closedAt;
    Date mergedAt;
    Commit mergeCommit;
    Boolean merged;
    Boolean mergeable;
    Developer mergedBy;
    int additions;
    int deletions;
    int changedFiles;
    boolean maintainerCanModify;
    Issue issue;
    ArrayList<PullRequestCommit> commits;
    ArrayList<IssueComment> ReviewComments;

    private PullRequest(Repository repository, Long ID, Date createdAT, Date updatedAt, Date closedAT, int additions,Boolean mergeable,
                       int deletions, int changedFiles, boolean maintainerCanModify, Issue issue, Boolean merged) {
        this.repository = repository;
        this.ID = ID;
        this.createdAT = createdAT;
        this.updatedAt = updatedAt;
        this.closedAt = closedAT;
        this.additions = additions;
        this.deletions = deletions;
        this.changedFiles = changedFiles;
        this.maintainerCanModify = maintainerCanModify;
        this.issue = issue;
        this.mergeable =mergeable;
        this.merged=merged;
        this.commits=new ArrayList<>();
        this.ReviewComments=new ArrayList<>();
        this.mergedBy =null;
        this.mergedAt=null;
        this.mergeCommit=null;

    }

    public static PullRequest createPullRequest(Repository repository, Long ID, Date createdAT, Date updatedAt, Date closedAT,
                                         int additions, Boolean mergeable, int deletions, int changedFiles, boolean maintainerCanModify,
                                         Issue issue,Boolean merged){
        PullRequest pullRequest=new PullRequest(repository,ID,createdAT,updatedAt,closedAT,additions,mergeable, deletions,
                changedFiles, maintainerCanModify, issue, merged);
        issue.setPullRequest(pullRequest);
        repository.addPullRequest(pullRequest);
        return pullRequest;

    }
    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Date getCreatedAT() {
        return createdAT;
    }

    public void setCreatedAT(Date createdAT) {
        this.createdAT = createdAT;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    public Date getMergedAt() {
        return mergedAt;
    }

    public void setMergedAt(Date mergedAt) {
        this.mergedAt = mergedAt;
    }

    public Commit getMergeCommit() {
        return mergeCommit;
    }

    public void setMergeCommit(Commit mergeCommit) {
        this.mergeCommit = mergeCommit;
    }

    public Boolean isMerged() {
        return merged;
    }

    public void setMerged(Boolean merged) {
        this.merged = merged;
    }

    public Boolean isMergeable() {
        return mergeable;
    }

    public void setMergeable(Boolean mergeable) {
        this.mergeable = mergeable;
    }

    public Developer getMergedBy() {
        return mergedBy;
    }

    public void setMergedBy(Developer mergedBy) {
        this.mergedBy = mergedBy;
    }

    public int getAdditions() {
        return additions;
    }

    public void setAdditions(int additions) {
        this.additions = additions;
    }

    public int getDeletions() {
        return deletions;
    }

    public void setDeletions(int deletions) {
        this.deletions = deletions;
    }

    public int getChangedFiles() {
        return changedFiles;
    }

    public void setChangedFiles(int changedFiles) {
        this.changedFiles = changedFiles;
    }

    public boolean isMaintainerCanModify() {
        return maintainerCanModify;
    }

    public void setMaintainerCanModify(boolean maintainerCanModify) {
        this.maintainerCanModify = maintainerCanModify;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public ArrayList<PullRequestCommit> getCommits() {
        return commits;
    }

    public void addCommit(PullRequestCommit commit) {
        this.commits.add(commit);
    }

    public ArrayList<IssueComment> getReviewComments() {
        return ReviewComments;
    }

    public void setReviewComments(ArrayList<IssueComment> reviewComments) {
        ReviewComments = reviewComments;
    }

}
