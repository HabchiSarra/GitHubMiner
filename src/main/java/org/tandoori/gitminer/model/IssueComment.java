package org.tandoori.gitminer.model;

import java.util.Date;

/**
 * Created by sarra on 27/03/17.
 */
public class IssueComment {
    Developer commenter;
    Issue issue;
    String body;
    Long id;
    Date createdAt;
    Date updateAt;

    private IssueComment(Developer commenter, Issue issue, String body, Long id, Date createdAt, Date updateAt) {
        this.commenter = commenter;
        this.issue = issue;
        this.body = body;
        this.id = id;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public static IssueComment createComment(Developer commenter, Issue issue, String body, Long id, Date createdAt, Date updateAt){
        IssueComment issueComment=new IssueComment(commenter,issue,body,id,createdAt,updateAt);
        issue.addComment(issueComment);
        if(commenter !=null)
        {
            commenter.addIssueComment(issueComment);
        }
        return issueComment;
    }

    public Developer getCommenter() {
        return commenter;
    }

    public void setCommenter(Developer commenter) {
        this.commenter = commenter;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
