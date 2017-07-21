package org.tandoori.gitminer;

import org.tandoori.gitminer.model.Repository;
import org.tandoori.gitminer.neo4j.CommitsQuery;
import org.tandoori.gitminer.neo4j.ModelToGraph;
import org.tandoori.gitminer.neo4j.QueryEngine;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args){

        ArgumentParser parser = ArgumentParsers.newArgumentParser("GitMiner");
        Subparsers subparsers = parser.addSubparsers().dest("sub_command");
        Subparser analyseParser = subparsers.addParser("getCommits").help("get commits");

        analyseParser.addArgument("-k", "--token").required(true).help("Github token");
        analyseParser.addArgument("-l", "--link").required(true).help("repository link in the api");
        analyseParser.addArgument("-d", "--database").required(true).help("the database path");
        try {
            Namespace res = parser.parseArgs(args);
            if(res.getString("sub_command").equals("getCommits")){
                getCommits(res);
            }

        } catch (ArgumentParserException e) {
            analyseParser.handleError(e);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void getCommits(Namespace arg){
        String link = arg.getString("link");
        String databasePath = arg.getString("database");
        String token = arg.getString("token");
        ContentFetcher fetcher=new ContentFetcher(token);
        Repository repository=fetcher.getRepository(link);
        ModelToGraph modelToGraph =new ModelToGraph(databasePath);
        modelToGraph.insertRepository(repository);
        QueryEngine queryEngine=new QueryEngine(databasePath);
        CommitsQuery commitsQuery = CommitsQuery.createCommitsQuery(queryEngine);
        try {
            commitsQuery.execute();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }





}
