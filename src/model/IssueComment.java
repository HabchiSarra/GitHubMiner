package model;

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
}
