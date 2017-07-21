/*
 * Paprika - Detection of code smells in Android application
 *     Copyright (C)  2016  Geoffrey Hecht - INRIA - UQAM - University of Lille
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.tandoori.gitminer.neo4j;

import org.neo4j.cypher.CypherException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class QueryEngine {

    protected GraphDatabaseService graphDatabaseService;
    protected DatabaseManager databaseManager;

    protected String csvPrefix;

    public String getCsvPrefix() {
        return csvPrefix;
    }

    public void setCsvPrefix(String csvPrefix) {
        this.csvPrefix = csvPrefix;
    }

    public GraphDatabaseService getGraphDatabaseService() {
        return graphDatabaseService;
    }


    public QueryEngine(String DatabasePath){
        this.databaseManager = new DatabaseManager(DatabasePath);
        databaseManager.start();
        graphDatabaseService = databaseManager.getGraphDatabaseService();
        csvPrefix = "";
    }

    public void shutDown(){
        databaseManager.shutDown();
    }

    public void AnalyzedAppQuery() throws CypherException, IOException {
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            result = graphDatabaseService.execute("MATCH (a:App) RETURN  a.app_key as app_key, a.category as category,a.package as package, a.version_code as version_code, a.date_analysis as date_analysis,a.number_of_classes as number_of_classes,a.size as size,a.rating as rating,a.nb_download as nb_download, a.number_of_methods as number_of_methods, a.number_of_activities as number_of_activities,a.number_of_services as number_of_services,a.number_of_interfaces as number_of_interfaces,a.number_of_abstract_classes as number_of_abstract_classes,a.number_of_broadcast_receivers as number_of_broadcast_receivers,a.number_of_content_providers as number_of_content_providers, a.number_of_variables as number_of_variables, a.number_of_views as number_of_views, a.number_of_inner_classes as number_of_inner_classes, a.number_of_async_tasks as number_of_async_tasks");
            resultToCSV(result,"_ANALYZED.csv");
        }
    }

    public void getPropertyForAllApk(String nodeType, String property,String suffix) throws IOException {
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = "MATCH (n:" + nodeType + ") RETURN n.app_key as app_key, n.name as name, n."+property+" as "+property;
            result = graphDatabaseService.execute(query);
            resultToCSV(result, suffix);
        }
    }

    public void getAllLCOM() throws IOException {
        getPropertyForAllApk("Class", "lack_of_cohesion_in_methods","_ALL_LCOM.csv");
    }

    public void getAllClassComplexity() throws IOException {
        getPropertyForAllApk("Class", "class_complexity","_ALL_CLASS_COMPLEXITY.csv");
    }

    public void getAllNumberOfMethods() throws IOException {
        getPropertyForAllApk("Class", "number_of_methods","_ALL_NUMBER_OF_METHODS.csv");
    }

    public void getAllCyclomaticComplexity() throws IOException {
        getPropertyForAllApk("Method", "cyclomatic_complexity","_ALL_CYCLOMATIC_COMPLEXITY.csv");
    }


    public void resultToCSV(Result result, String csvSuffix) throws IOException {
        String name = csvPrefix+csvSuffix;
        FileWriter fw = new FileWriter(name);
        BufferedWriter writer = new BufferedWriter( fw );
        List<String> columns = result.columns();
        Object val;
        int i;
        int columns_size = columns.size()-1;
        for(i=0;i<columns_size;i++){
            writer.write(columns.get(i));
            writer.write(',');
        }
        writer.write(columns.get(i));
        writer.newLine();
        while ( result.hasNext()){
            Map<String,Object> row = result.next();
            for(i=0;i<columns_size;i++){
                val = row.get(columns.get(i));
                if(val != null){
                    writer.write(val.toString());
                    writer.write(',');
                }
            }
            val = row.get(columns.get(i));
            if(val != null){
                writer.write(val.toString());
            }
            writer.newLine();
        }
        writer.close();
        fw.close();
    }

    public void resultToCSV(List<Map> rows,List<String> columns, String csvSuffix) throws IOException {
        String name = csvPrefix+csvSuffix;
        FileWriter fw = new FileWriter(name);
        BufferedWriter writer = new BufferedWriter( fw );
        Object val;
        int i;
        int columns_size = columns.size()-1;
        for(i=0;i<columns_size;i++){
            writer.write(columns.get(i));
            writer.write(',');
        }
        writer.write(columns.get(i));
        writer.newLine();
        for(Map<String,Object> row : rows){
            for(i=0;i<columns_size;i++){
                val = row.get(columns.get(i));
                if(val != null){
                    writer.write(val.toString());
                    writer.write(',');
                }
            }
            val = row.get(columns.get(i));
            if(val != null){
                writer.write(val.toString());
            }
            writer.newLine();
        }
        writer.close();
        fw.close();
    }
    public void statsToCSV(Map<String,Double> stats, String csvSuffix) throws IOException {
        String name = csvPrefix+csvSuffix;
        FileWriter fw = new FileWriter(name);
        BufferedWriter writer = new BufferedWriter( fw );
        Set<String> keys = stats.keySet();
        for(String key : keys){
            writer.write(key);
            writer.write(',');
        }
        writer.newLine();
        for(String key : keys){
            writer.write(String.valueOf(stats.get(key)));
            writer.write(',');
        }
        writer.close();
        fw.close();
    }

    public void deleteQuery(String appKey) throws CypherException, IOException {
        Result result;
        try (Transaction tx = graphDatabaseService.beginTx()) {
            result = graphDatabaseService.execute("MATCH (n {app_key: '"+appKey+"'})-[r]-() DELETE n,r");
            System.out.println(result.resultAsString());
            tx.success();
        }
    }





    public void executeRequest(String request)  throws CypherException, IOException {
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            result = graphDatabaseService.execute(request);
            resultToCSV(result,"_CUSTOM.csv");
        }
    }
}
