package model;

import java.util.HashMap;

/**
 * Created by sarra on 27/03/17.
 */
public class IssueLabel {
    String name;
    Long ID;
    boolean isDefault;
    Issue issue;
    private static HashMap<Long,IssueLabel> issueLabels =new HashMap<>();
    private IssueLabel(String name, Long ID, boolean isDefault, Issue issue) {
        this.name = name;
        this.ID = ID;
        this.isDefault = isDefault;
        this.issue=issue;
    }


    public static IssueLabel createIssueLabel(String name, Long ID, boolean isDefault, Issue issue){
        IssueLabel issueLabel;
        if((issueLabel =issueLabels.get(ID))!=null){
            return issueLabel;
        }
        issueLabel=new IssueLabel(name,ID,isDefault,issue);
        issue.addLabel(issueLabel);
        return issueLabel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
