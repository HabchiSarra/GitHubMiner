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
public class Issue {

    Repository repository;
    Developer creator;
    Developer assignee;
    Long number;
    Long id;
    String title;
    String body;
    State state;
    Date createdAt;
    Date updatedAt;
    Date closedAt;
    PullRequest pullRequest;
    Milestone milestone;
    ArrayList<IssueLabel> labels;
    ArrayList<IssueComment> comments;
    ArrayList<IssueEvent> events;

    private Issue(Repository repository, Developer user, Developer assignee, Long number, Long id, String title, String body,
                 State state, Date createdAt, Date updatedAt, Date closedAt) {
        this.repository = repository;
        this.creator = user;
        this.assignee = assignee;
        this.number = number;
        this.id = id;
        this.title = title;
        this.body = body;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.closedAt = closedAt;
        this.pullRequest = null;
        this.milestone = null;
        this.comments=new ArrayList<>();
        this.labels=new ArrayList<>();
        this.events=new ArrayList<>();
    }

    public static Issue createIssue(Repository repository, Developer creator, Developer assignee, Long number, Long id, String title, String body,
                               State state, Date createdAt, Date updatedAt, Date closedAt){

        Issue issue = new Issue(repository,creator,assignee,number,id,title,body,state,createdAt,updatedAt,closedAt);
        if(creator!=null){
            creator.addCreatedIssue(issue);
        }
        if(assignee!=null){
           assignee.addAssignedIssue(issue);
         }
        repository.addIssue(issue);
        return issue;
    }

    public Repository getRepository() {
        return repository;
    }

    public Developer getCreator() {
        return creator;
    }

    public Developer getAssignee() {
        return assignee;
    }

    public Long getNumber() {
        return number;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public State getState() {
        return state;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public PullRequest getPullRequest() {
        return pullRequest;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public ArrayList<IssueLabel> getLabels() {
        return labels;
    }

    public void addLabel(IssueLabel issueLabel){
        this.labels.add(issueLabel);
    }

    public ArrayList<IssueComment> getComments() {
        return comments;
    }

    public void addComment(IssueComment issueComment){
        this.comments.add(issueComment);
    }

    public ArrayList<IssueEvent> getEvents() {
        return events;
    }

    public void addEvent(IssueEvent issueEvent){
        this.events.add(issueEvent);
    }

    public void setPullRequest(PullRequest pullRequest) {
        this.pullRequest = pullRequest;
    }
}
