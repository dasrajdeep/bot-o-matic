/*
 * Copyright (C) 2013 Rajdeep Das <rajdeepd@iitk.ac.in>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package impersonator;

import java.util.Collection;
import java.util.Iterator;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;

/**
 *
 * @author Rajdeep Das <rajdeepd@iitk.ac.in>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            FacebookChat fbchat=new FacebookChat("das.rajdeep","@ddedOctan3");
            
            fbchat.connect();
            
//            Collection<RosterEntry> friendList=fbchat.getOnlineFriends();
//            
//            System.out.println(friendList.size()+" friends are online.");
//            
//            for(Iterator friend=friendList.iterator();friend.hasNext();) {
//                RosterEntry entry=(RosterEntry)friend.next();
//                System.out.println(entry.getName());
//            }
            
            while(true) {}
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
