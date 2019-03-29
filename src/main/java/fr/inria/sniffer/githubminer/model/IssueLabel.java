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
