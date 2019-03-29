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
public class Milestone {

    long ID;
    long number;
    Repository repository;
    String title;
    State state;
    String description;
    Date createdAt;
    Date updatedAt;
    Date closedAT;
    Date dueOn;
    Developer creator;
    int openIssues;
    int closedIssues;
    ArrayList<Issue> issues;

    private Milestone(long ID, long number, Repository repository, Developer creator, String title, State state, String description,
                     Date createdAt, Date updatedAt, Date closedAT, Date dueOn, int openIssues, int closedIssues) {
        this.ID = ID;
        this.number = number;
        this.repository = repository;
        this.title = title;
        this.state = state;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.closedAT = closedAT;
        this.dueOn = dueOn;
        this.openIssues = openIssues;
        this.closedIssues = closedIssues;
        this.creator=creator;
        this.issues=new ArrayList<>();
    }

    public static Milestone createMilestone(long ID, long number, Repository repository, Developer creator, String title, State state,
                                            String description, Date createdAt, Date updatedAt, Date closedAT, Date dueOn,
                                            int openIssues, int closedIssues){
        Milestone milestone=new Milestone(ID,number,repository,creator,title,state,description,createdAt,updatedAt,closedAT,
                dueOn,openIssues,closedIssues);
        repository.getMilestones().put(ID,milestone);
        if(creator!=null){
            creator.getMilestones().put(ID, milestone);
        }
        return milestone;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getClosedAT() {
        return closedAT;
    }

    public void setClosedAT(Date closedAT) {
        this.closedAT = closedAT;
    }

    public Date getDueOn() {
        return dueOn;
    }

    public void setDueOn(Date dueOn) {
        this.dueOn = dueOn;
    }

    public Developer getCreator() {
        return creator;
    }

    public void setCreator(Developer creator) {
        this.creator = creator;
    }

    public int getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(int openIssues) {
        this.openIssues = openIssues;
    }

    public int getClosedIssues() {
        return closedIssues;
    }

    public void setClosedIssues(int closedIssues) {
        this.closedIssues = closedIssues;
    }

    public ArrayList<Issue> getIssues() {
        return issues;
    }

    public void setIssues(ArrayList<Issue> issues) {
        this.issues = issues;
    }

    public void addIssue(Issue issue){
        this.issues.add(issue);
    }

}
