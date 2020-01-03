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
package fr.inria.sniffer.githubminer;

import fr.inria.sniffer.githubminer.model.Repository;
import fr.inria.sniffer.githubminer.neo4j.CommitsQuery;
import fr.inria.sniffer.githubminer.neo4j.ModelToGraph;
import fr.inria.sniffer.githubminer.neo4j.QueryEngine;
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
        modelToGraph.closeDB();

        // Retrieve the developers and save them as CSV
        QueryEngine queryEngine=new QueryEngine(databasePath);
        CommitsQuery commitsQuery = CommitsQuery.createCommitsQuery(queryEngine);
        try {
            commitsQuery.execute();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }





}
