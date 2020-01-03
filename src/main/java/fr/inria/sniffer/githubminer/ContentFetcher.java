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

import fr.inria.sniffer.githubminer.model.Commit;
import fr.inria.sniffer.githubminer.model.FileModification;

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
