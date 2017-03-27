/**
 * Created by sarra on 03/02/17.
 */

import model.Repository;
import java.util.Scanner;



public class Main {


    public static void main(String[] args){

        Scanner scanner=new Scanner(System.in);
        System.out.println("The token: ");
        String token = "";
                //scanner.next();
        System.out.println("The repository link: ");

        String link ="https://api.github.com/repos/INRIA/spoon";
                //scanner.next();
        ContentFetcher fetcher=new ContentFetcher(token);
        Repository repository=fetcher.getRepository(link);
        repository.print();



    }





}
