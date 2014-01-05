/*
 * Copyright (C) 2014 Rajdeep Das <rajdeepd@iitk.ac.in>
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;

/**
 *
 * @author Rajdeep Das <rajdeepd@iitk.ac.in>
 */
public class BOT {
    
    private String userName;
    private String password;
    private boolean botRunning=false;
    
    public FacebookChat fbchat;

    public BOT(String userName, String password) {
        this.userName=userName;
        this.password=password;
        
        fbchat=new FacebookChat(userName, password);
    }
    
    public String getUser() {
        return this.userName;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String startBot() {
        if(botRunning) return "bot already running.";
        
        if(fbchat!=null) try {
            fbchat.connect();
            fbchat.messageHandler.knowledgeBase.loadKnowledgeBase();
            System.out.println("Bot Started.");
            botRunning=true;
        } catch (XMPPException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "bot started";
    }
    
    public String stopBot() {
        if(!botRunning) return"bot not running";
        
        fbchat.disconnect();
        fbchat.messageHandler.knowledgeBase.saveKnowledgeBase();
        System.out.println("Bot Stopped.");
        botRunning=false;
        
        return "bot stopped";
    }
    
    public String setReply(String uid, String replyMessage) {
        if(!botRunning) return "bot not running";
        
        fbchat.messageHandler.knowledgeBase.setReply(uid, replyMessage);
        
        System.out.println("reply for "+uid+" set to "+replyMessage);
        
        return "reply for "+uid+" set to "+replyMessage;
    }
    
    public String setReplyAll(String replyMessage) {
        if(!botRunning) return "bot not running";
        
        fbchat.messageHandler.knowledgeBase.setDefaultReply(replyMessage);
        
        System.out.println("reply to all set to "+replyMessage);
        
        return "reply to all set to "+replyMessage;
    }
    
    public String getUserList() {
        if(!botRunning) return "bot not running";
        
        Collection<RosterEntry> friendList=fbchat.getRoster();
        
        String userlist="";
        
        for(Iterator friend=friendList.iterator();friend.hasNext();) {
            RosterEntry entry=(RosterEntry)friend.next();
            userlist+=entry.getUser()+":"+entry.getName()+",";
        }
        
        return userlist+";";
    }
    
    public String[] getUserIDS() {
        Collection<RosterEntry> friendList=fbchat.getRoster();
        
        String[] userlist=new String[friendList.size()];
        
        int i=0;
        
        for(Iterator friend=friendList.iterator();friend.hasNext();) {
            RosterEntry entry=(RosterEntry)friend.next();
            userlist[i++]=entry.getUser();
        }
        
        return userlist;
    }
    
    public String setAutoMode() {
        fbchat.messageHandler.knowledgeBase.setAutoMode(true);
        return "auto mode set";
    }
    
    public String unsetAutoMode() {
        fbchat.messageHandler.knowledgeBase.setAutoMode(false);
        return "manual mode set";
    }
    
}
