package model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sarra on 27/03/17.
 */
public class Milestone {

    long ID;
    long number;
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

}
