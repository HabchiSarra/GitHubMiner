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

import fr.inria.sniffer.githubminer.model.FileStatus;
import fr.inria.sniffer.githubminer.model.State;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sarra on 17/02/17.
 */
public class Converter {

    public static Date stringToDate(String dateString){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        Date date =new Date();
        try {
            date=sdf.parse(dateString);

        }catch (ParseException pe){
            System.out.println(pe.getMessage());
        }
        return date;
    }


    public static String streamToString(InputStream is) {

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

    public static FileStatus stringToFileStatus(String statusString){

        if(statusString.toLowerCase().equals("removed")){
            return FileStatus.REMOVED;
        }else if(statusString.toLowerCase().equals("modified")){
            return FileStatus.MODIFIED;
        }else{
            return FileStatus.ADDED;
        }
    }

    public static State stringToState(String stateString){
        if(stateString.toLowerCase().equals("all")){
            return State.ALL;
        }else if(stateString.toLowerCase().equals("closed")){
            return State.CLOSED;
        }else{
            return State.OPEN;
        }
    }
}
