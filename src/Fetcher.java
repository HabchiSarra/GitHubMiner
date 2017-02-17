import model.Commit;
import model.Developer;
import model.FileModification;
import model.Repository;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
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
        System.out.println(jsonObject.toString());
        String name = jsonObject.getString("name");
        String description = jsonObject.getString("description");

        String createdAt= jsonObject.getString("created_at");
        String pushed_at=jsonObject.getString("pushed_at");
        Date commitDate=Converter.getDate(createdAt);
        Date pushDate=Converter.getDate(pushed_at);
        int stargazersCount=jsonObject.getInt("stargazers_count");
        int watchersCount=jsonObject.getInt("watchers_count");
        JSONObject ownerJson= jsonObject.getJSONObject("owner");
        Developer owner=Developer.createDeveloper(ownerJson.getString("login"),ownerJson.getLong("id"),ownerJson.getString("url"));
        Repository repository=Repository.createRepository(name,owner,description,stargazersCount,watchersCount,commitDate,pushDate);
        return repository;
    }

    public  String getData(String link){
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(link);
        request.addHeader("Authorization"," token "+token);
        HttpResponse response;

        String result = null;
        try {
            response = client.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                result = Converter.convertStreamToString(instream);
                System.out.println("RESULT:: "+ result);
                instream.close();
            }
            // Headers
            org.apache.http.Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println(headers[i]);
            }
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    public  ArrayList<Developer> getDevelopers(String link){
        ArrayList<Developer> developers=new ArrayList<>();
        String result =getData(link);
        JSONArray jsonArray= new JSONArray(result);
        JSONObject jsonObject;
        int i= 0;
        Iterator<Object> iterator=jsonArray.iterator();
        while(iterator.hasNext()){
            jsonObject=(JSONObject) iterator.next();
            System.out.println("Object "+i+" : "+jsonObject.toString());
            i++;
        }
        return developers;
    }


    public  ArrayList<Commit> getRepoCommits(Repository repository){
        String link = "https://api.github.com/repos/"+repository.getOwner().getLogin()+"/"+repository.getName()+"/commits";
        String result=getData(link);
        JSONArray jsonArray=new JSONArray(result);
        JSONObject jsonObject;
        String sha;
        ArrayList<Commit> commitList =new ArrayList<>();
        Iterator<Object> iterator=jsonArray.iterator();
        while(iterator.hasNext()){
            jsonObject=(JSONObject) iterator.next();
            sha=jsonObject.getString("sha");
            commitList.add(getCommit(repository,sha));
        }

        return commitList;
    }

    public  Commit getCommit(Repository repository, String sha){
        String link = "https://api.github.com/repos/"+repository.getOwner().getLogin()+"/"+repository.getName()+"/commits"+"/"+sha;
        String result=getData(link);
        JSONObject jsonObject = new JSONObject(result);
        String message =jsonObject.getString("message");
        JSONObject jsonAuthor=jsonObject.getJSONObject("author");
        JSONObject jsonCommitter=jsonObject.getJSONObject("committer");
        Developer author;
        Developer committer;
        author=Developer.createDeveloper(jsonAuthor.getString("login"),jsonAuthor.getLong("id"), jsonAuthor.getString("mail"));
        if(!(jsonAuthor.getLong("id")== jsonCommitter.getLong("id"))){
            committer= Developer.createDeveloper(jsonCommitter.getString("login"),jsonCommitter.getLong("id"), jsonCommitter.getString("mail"));
        }else{
            committer=author;
        }
        String d1 =jsonAuthor.getString("date");
        String d2 =jsonCommitter.getString("date");
        Date authoringDate =Converter.getDate(d1);
        Date commitDate=Converter.getDate(d2);
        Commit commit=Commit.createCommit(sha,author,committer,message,authoringDate,commitDate, repository);
        //Getting files
        JSONArray jsonFiles =jsonObject.getJSONArray("files");
        int additions, deletions; String patch, fileName, status;
        JSONObject jsonFile;
        Iterator<Object> iterator=jsonFiles.iterator();
        while(iterator.hasNext()){
            jsonFile=(JSONObject) iterator.next();
            additions=jsonFile.getInt("additions");
            deletions=jsonFile.getInt("deletions");
            patch = jsonFile.getString("patch");
            fileName=jsonFile.getString("filename");
            status=jsonFile.getString("status");
            commit.addFileModification(new FileModification(additions,deletions,patch,fileName,status));
        }
        return commit;
    }


    public  ArrayList<Developer> getCollaborators(Repository repository){
        ArrayList<Developer> collaborators =new ArrayList<>();
        String link ="https://api.github.com/repos/"+repository.getOwner().getLogin()+"/"+repository.getName()+"/collaborators";
        String result =getData(link);
        JSONArray jsonArray= new JSONArray(result);
        JSONObject jsonObject;
        Iterator<Object> iterator=jsonArray.iterator();
        while(iterator.hasNext()){
            jsonObject=(JSONObject) iterator.next();
            collaborators.add(Developer.createDeveloper(jsonObject.getString("login"),
                    jsonObject.getLong("id"),jsonObject.getString("mail")));
        }
        return collaborators;
    }

}
