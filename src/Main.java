/**
 * Created by sarra on 03/02/17.
 */

import GithubEntities.Commit;
import GithubEntities.Developer;
import GithubEntities.FileModification;
import GithubEntities.Repository;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.URIParameter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


import org.json.JSONArray;
import org.json.JSONObject;



public class Main {

    public static String token = ":token";

    public static void main(String[] args){
//        JSONObject jsonObject=doInBackground("https://api.github.com/users/technoweenie/repos");
//
//        System.out.println("JSON :: "+ jsonObject.toString());
        Repository repository =getRepository("https://api.github.com/repos/SOMCA/ObjCParser");
        //getDevelopers("https://api.github.com/repos/SOMCA/ObjCParser/contributors");
        //getRepoCommits("SOMCA","ObjCParser");
        getCommit(repository,"9d8a65bd677712e197935e3304b26f898270d744");

    }




    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static Repository getRepository(String link){
        String result=getData(link);
        JSONObject jsonObject = new JSONObject(result);
        System.out.println(jsonObject.toString());
        String name = jsonObject.getString("name");
        String linkApi= jsonObject.getString("url");
        String description = jsonObject.getString("description");

        String createdAt= jsonObject.getString("created_at");
        String pushed_at=jsonObject.getString("pushed_at");
        Date commitDate=getDate(createdAt);
        Date pushDate=getDate(pushed_at);
        int stargazersCount=jsonObject.getInt("stargazers_count");
        int watchersCount=jsonObject.getInt("watchers_count");
        JSONObject ownerJson= jsonObject.getJSONObject("owner");
        Developer owner=Developer.createDeveloper(ownerJson.getString("login"),ownerJson.getLong("id"),ownerJson.getString("url"));
        Repository repository=Repository.createRepository(name,owner,description,stargazersCount,watchersCount,commitDate,pushDate);
        return repository;
    }

    public static String getData(String link){
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
                result = convertStreamToString(instream);
                System.out.println("RESULT:: "+ result);
                instream.close();
            }
            // Headers
            org.apache.http.Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println(headers[i]);
            }
        } catch (ClientProtocolException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Developer> getDevelopers(String link){
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
    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Main.token = token;
    }

    public static ArrayList<Commit> getRepoCommits(Repository repository){
        String link = "https://api.github.com/repos/"+repository.getOwner().getLogin()+"/"+repository.getName()+"/commits";
        String result=getData(link);
        JSONArray jsonArray=new JSONArray(result);
        JSONObject jsonObject;
        String sha;
        ArrayList<Commit> commitList =new ArrayList<>();
        //int i= 0;
        Iterator<Object> iterator=jsonArray.iterator();
        while(iterator.hasNext()){
            jsonObject=(JSONObject) iterator.next();
            //System.out.println("Object "+i+" : "+jsonObject.toString());
            //shaList.add( jsonObject.getString("sha"));
            sha=jsonObject.getString("sha");
            commitList.add(getCommit(repository,sha));
        }

        return commitList;
    }

    public static Commit getCommit(Repository repository, String sha){
        String link = "https://api.github.com/repos/"+repository.getOwner().getLogin()+"/"+repository.getName()+"/commits"+"/"+sha;
        String result=getData(link);
        JSONObject jsonObject = new JSONObject(result);
        String message =jsonObject.getString("message");
        JSONObject jsonAuthor=jsonObject.getJSONObject("author");
        JSONObject jsonCommitter=jsonObject.getJSONObject("committer");
        Developer author=Developer.createDeveloper(jsonAuthor.getString("login"),jsonAuthor.getLong("id"), jsonAuthor.getString("mail"));
        Developer committer= Developer.createDeveloper(jsonCommitter.getString("login"),jsonCommitter.getLong("id"), jsonCommitter.getString("mail"));
        String d1 =jsonAuthor.getString("date");
        String d2 =jsonCommitter.getString("date");
        Date authoringDate =getDate(d1);
        Date commitDate=getDate(d2);
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

    //public ArrayList<FileModification> getFileModifications(Commit)


    public static Date getDate(String dateString){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        Date date =new Date();
        try {
            date=sdf.parse(dateString);

        }catch (ParseException pe){
            System.out.println(pe.getMessage());
        }
        return date;
    }

}
