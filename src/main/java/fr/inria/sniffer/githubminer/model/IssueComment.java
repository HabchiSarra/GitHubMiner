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
