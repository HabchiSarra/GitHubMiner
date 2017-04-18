
import model.Commit;
import model.FileModification;

/**
 * Created by sarra on 24/02/17.
 */
public class ContentFetcher extends Fetcher {

    public ContentFetcher(String token) {
        super(token);
    }

//    public void getFileContent(Commit commit, FileModification fileModification){
//        String link= "https://api.github.com/repos/"+commit.getRepository().getOwner().getLogin()+"/"+commit.getRepository().getName()+
//                "/contents/"+commit.getSha()+"/"
//    }



    public void getFileContent(Commit commit, FileModification fileModification){
        String link= "https://raw.githubusercontent.com/"+commit.getRepository().getOwner().getLogin()+"/"
                +commit.getRepository().getName()+"/"
                +commit.getSha()+"/"+ fileModification.getFileName();
        String result =getData(link);
        System.out.println("Résultat : "+ result);
    }
    ///repos/:owner/:repo/contents/:path
}
