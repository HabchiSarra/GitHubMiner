import model.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by sarra on 17/02/17.
 */
public class Fetcher {

    public  String token ;


    public Fetcher(String token) {
        this.token=token;
    }

    public  Repository getRepository(String link){
        String result=getData(link);
        JSONObject jsonObject = new JSONObject(result);
        String name = jsonObject.getString("name");
        Long ID = jsonObject.getLong("id");
        String description = jsonObject.getString("description");
        String createdAt= jsonObject.getString("created_at");
        String pushed_at=jsonObject.getString("pushed_at");
        Date commitDate=Converter.stringToDate(createdAt);
        Date pushDate=Converter.stringToDate(pushed_at);
        int stargazersCount=jsonObject.getInt("stargazers_count");
        int watchersCount=jsonObject.getInt("watchers_count");
        JSONObject ownerJson= jsonObject.getJSONObject("owner");
        Developer owner=Developer.createDeveloper(ownerJson.getString("login"),
                ownerJson.getLong("id"));
        owner.setMail(ownerJson.getString("url"));
        Repository repository=Repository.createRepository(ID, name,owner,description,
                stargazersCount,watchersCount,commitDate,pushDate);
   //     getRepoCommits(repository);
     //   repository.setCollaborators(getCollaborators(repository));
        getIssues(repository);

        System.out.println("Hola !! ");
        return repository;
    }

    public  String getData(String link){
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(link);
        request.addHeader("Authorization"," token "+token);
        HttpResponse response;
        int statusCode;
        String result = null;
        try {
            response = client.execute(request);
            statusCode =response.getStatusLine().getStatusCode();
            if(statusCode !=200){
                return null;
            }
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                result = Converter.streamToString(instream);
                //System.out.println("RESULT:: "+ result);
                instream.close();
            }
            // Headers
            org.apache.http.Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                //System.out.println(headers[i]);
            }
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    public void  getRepoCommits(Repository repository){
        String link;
        int numberOfPages =1;
        int actualPage = 0;
        while(actualPage<numberOfPages) {
            actualPage++;
            link = "https://api.github.com/repos/" + repository.getOwner().getLogin() + "/" + repository.getName() + "/commits";
            link = link + "?page=" + (actualPage);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(link);
            request.addHeader("Authorization", " token " + token);
            HttpResponse response;
            int statusCode;
            String result = null;
            try {
                response = client.execute(request);
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    return ;
                }
                numberOfPages = Integer.valueOf(response.getAllHeaders()[15].getElements()[1].getValue().split(">")[0]);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    result = Converter.streamToString(instream);
                    //System.out.println("RESULT:: "+ result);
                    instream.close();
                }

                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject;
                String sha;
                Iterator<Object> iterator = jsonArray.iterator();
                while (iterator.hasNext()) {
                    jsonObject = (JSONObject) iterator.next();
                    sha = jsonObject.getString("sha");
                    getCommit(repository, sha);
                }
                // Headers
//            org.apache.http.Header[] headers = response.getAllHeaders();
//            for (int i = 0; i < headers.length; i++) {
//                //System.out.println(headers[i]);
//            }
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public  Commit getCommit(Repository repository, String sha){
        String link = "https://api.github.com/repos/"+repository.getOwner().getLogin()+"/"+repository.getName()+"/commits"+"/"+sha;
        String result=getData(link);
        JSONObject jsonObject = new JSONObject(result);
        String message =jsonObject.getJSONObject("commit").getString("message");
        JSONObject jsonAuthor=null;
        JSONObject jsonCommitter = null ;
        String d1; String d2;
        Date authoringDate=null;Date commitDate=null;
        try {
            jsonAuthor=jsonObject.getJSONObject("author");
            d1 =jsonObject.getJSONObject("commit").getJSONObject("author").getString("date");
            authoringDate =Converter.stringToDate(d1);

        }catch (JSONException e ){
            e.printStackTrace();
        }
        try {
            jsonCommitter=jsonObject.getJSONObject("committer");
            d2 =jsonObject.getJSONObject("commit").getJSONObject("committer").getString("date");
            commitDate=Converter.stringToDate(d2);
        }catch (JSONException e ){
            e.printStackTrace();
        }
        Developer author=null;
        Developer committer=null;
        if(jsonAuthor!=null)
        {
            author=Developer.createDeveloper(jsonAuthor.getString("login"),jsonAuthor.getLong("id"));
            if(committer !=null){
                if(jsonAuthor.getLong("id")!= jsonCommitter.getLong("id")){
                    committer= Developer.createDeveloper(jsonCommitter.getString("login"),jsonCommitter.getLong("id"));
                }else{
                    committer=author;
                }
            }
        }else{
            if(committer!=null){
                committer= Developer.createDeveloper(jsonCommitter.getString("login"),jsonCommitter.getLong("id"));
            }
        }

        Commit commit=Commit.createCommit(sha,author,committer,message,authoringDate,commitDate, repository);
        //Getting files
        JSONArray jsonFiles =jsonObject.getJSONArray("files");
        int additions, deletions; String patch, fileName, status;
        JSONObject jsonFile;
        Iterator<Object> iterator=jsonFiles.iterator();
        while(iterator.hasNext()){
            jsonFile=(JSONObject) iterator.next();
            fileName=jsonFile.getString("filename");
            additions=jsonFile.getInt("additions");
            deletions=jsonFile.getInt("deletions");
            status=jsonFile.getString("status");
            FileStatus fileStatus= Converter.stringToFileStatus(status);
//            try{
//                patch = jsonFile.getString("patch");
//            }catch (JSONException jsonException){
//                jsonException.printStackTrace();
//            }
            String shaFile =jsonFile.getString("sha");

            commit.addFileModification(new FileModification(additions,deletions,fileName,fileStatus,shaFile));

        }
        return commit;
    }


    public  ArrayList<Developer> getCollaborators(Repository repository){
        ArrayList<Developer> collaborators =new ArrayList<>();
        String link ="https://api.github.com/repos/"+repository.getOwner().getLogin()+"/"+repository.getName()+"/collaborators";
        String result =getData(link);
        if(result == null){
            return collaborators;
        }
        JSONArray jsonArray= new JSONArray(result);
        JSONObject jsonObject;
        Iterator<Object> iterator=jsonArray.iterator();
        while(iterator.hasNext()){
            jsonObject=(JSONObject) iterator.next();
            collaborators.add(Developer.createDeveloper(jsonObject.getString("login"),
                    jsonObject.getLong("id")));
        }
        return collaborators;
    }

    public  void getIssues(Repository repository){
        Issue issue; Milestone milestone;
        JSONArray jsonArray;
        JSONObject jsonObject;
        JSONObject creatorJson;
        JSONObject assigneeJson;
        Long creatorID;
        Long assigneeID;
        Developer creator;
        Developer assignee;
        Long number; Long ID;
        String title; String body;
        State state;
        Date createdAt; Date updatedAt; Date closedAt;
        Iterator<Object> iterator;
        String link;
        int numberOfPages =1;
        int actualPage = 0;
        while(actualPage<numberOfPages) {
            actualPage++;
            link ="https://api.github.com/repos/"+repository.getOwner().getLogin()+"/"+repository.getName()+"/issues";
            link = link + "?page=" + (actualPage)+"&state=all";
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(link);
            request.addHeader("Authorization", " token " + token);
            HttpResponse response;
            int statusCode;
            String result = null;
            try {
                response = client.execute(request);
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    return ;
                }
                numberOfPages = Integer.valueOf(response.getAllHeaders()[14].getElements()[1].getValue().split("&")[0]);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    result = Converter.streamToString(instream);
                    //System.out.println("RESULT:: "+ result);
                    instream.close();
                }

                jsonArray = new JSONArray(result);
                iterator=jsonArray.iterator();
                while(iterator.hasNext()){
                    jsonObject=(JSONObject) iterator.next();
                    creatorJson =jsonObject.getJSONObject("user");
                    creatorID=creatorJson.getLong("id");
                    creator=Developer.developersMap.get(creatorID);
                    if(creator == null){
                        creator =Developer.createDeveloper(creatorJson.getString("login"),creatorID);
                    }
                    if(jsonObject.get("assignee") ==  JSONObject.NULL){
                        assignee=null;
                    }else {
                        assigneeJson = jsonObject.getJSONObject("assignee");
                        assigneeID = assigneeJson.getLong("id");
                        assignee = Developer.developersMap.get(assigneeID);
                        if(assignee == null){
                            assignee=Developer.createDeveloper(assigneeJson.getString("login"), assigneeID);
                        }
                    }

                    number=jsonObject.getLong("number");
                    ID =jsonObject.getLong("id");
                    title = jsonObject.getString("title");
                    body = jsonObject.getString("body");
                    state =Converter.stringToState(jsonObject.getString("state"));
                    createdAt =Converter.stringToDate(jsonObject.getString("created_at"));
                    updatedAt=Converter.stringToDate(jsonObject.getString("updated_at"));
                    if(jsonObject.get("closed_at") == JSONObject.NULL){
                        closedAt=null;
                    }else {
                        closedAt=Converter.stringToDate(jsonObject.getString("closed_at"));
                    }

                    issue =Issue.createIssue(repository,creator,assignee,number,ID,title,body,state,createdAt,updatedAt,closedAt);
                    milestone = getMilestone(repository, jsonObject);
                    if(milestone!=null){
                        issue.setMilestone(milestone);
                        milestone.addIssue(issue);
                    }
                    if(jsonObject.has("pull_request") )//&& jsonObject.get("pull_request")!= JSONObject.NULL)
                    {
                        getPullRequest(repository,issue);
                    }


                }
                // Headers
//            org.apache.http.Header[] headers = response.getAllHeaders();
//            for (int i = 0; i < headers.length; i++) {
//                //System.out.println(headers[i]);
//            }
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private Milestone getMilestone(Repository repository, JSONObject issueObject){
        if(!issueObject.has("milestone") || issueObject.get("milestone") ==JSONObject.NULL ){
            return null;
        }
        JSONObject jsonObject=issueObject.getJSONObject("milestone");
        Milestone milestone;
        Long ID = jsonObject.getLong("id");
        if((milestone =repository.getMilestones().get(ID))!=null){
            return milestone;
        }
        State state=Converter.stringToState(jsonObject.getString("state"));
        Date createdAt =Converter.stringToDate(jsonObject.getString("created_at"));
        Date updatedAt = Converter.stringToDate(jsonObject.getString("updated_at"));
        Date closedAt = Converter.stringToDate(jsonObject.getString("closed_at"));
        Date dueOn = Converter.stringToDate(jsonObject.getString("due_on"));
        //Get the creator
        Developer creator;
        if(!jsonObject.has("creator")){
            creator =null;
        }else {
            JSONObject creatorObject = jsonObject.getJSONObject("creator");
            Long creatorId = creatorObject.getLong("id");
            creator = Developer.createDeveloper(creatorObject.getString("login"), creatorId);
            if (creatorObject.has("mail")) {
                creator.setMail(creatorObject.getString("mail"));
            }
        }

        milestone =Milestone.createMilestone (ID,jsonObject.getLong("number"),repository,creator,jsonObject.getString("title"),
                state,jsonObject.getString("description"),createdAt,updatedAt,closedAt,dueOn,jsonObject.getInt("open_issues"),
                jsonObject.getInt("closed_issues"));

        return milestone;
    }

    private PullRequest getPullRequest(Repository repository, Issue issue){
        PullRequest pullRequest;
        String link = "https://api.github.com/repos/"+repository.getOwner().getLogin()+"/"
                +repository.getName()+"/pulls/"+issue.getNumber();

        String result =getData(link);
        JSONObject pullRequestObject = new JSONObject(result);
        Long ID = pullRequestObject.getLong("id");
        Date createdAt =Converter.stringToDate(pullRequestObject.getString("created_at"));
        Date updatedAt = Converter.stringToDate(pullRequestObject.getString("updated_at"));
        Date closedAt = null;
        if(pullRequestObject.has("closed_at") && pullRequestObject.get("closed_at") != JSONObject.NULL)
        {
            closedAt =Converter.stringToDate(pullRequestObject.getString("closed_at"));
        }

        Boolean mergeable =null;
        if(pullRequestObject.get("mergeable")!= JSONObject.NULL)
        {
            mergeable= pullRequestObject.getBoolean("mergeable");
        }
        pullRequest=PullRequest.createPullRequest(repository,ID,createdAt,updatedAt,closedAt, pullRequestObject.getInt("additions"),
                mergeable,
                pullRequestObject.getInt("deletions"),
                pullRequestObject.getInt("changed_files"), pullRequestObject.getBoolean("maintainer_can_modify"),
                 issue, pullRequestObject.getBoolean("merged"));

        //TODO add merge data
        return pullRequest;
    }

}
