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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rajdeep Das <rajdeepd@iitk.ac.in>
 */
public class KnowledgeBase {
    
    private AutoBOT autoBot;
    
    private Map<String, String[]> messageSequences;
    
    private Map<String, String> autoReplies;
    private String defaultReply;
    private String userName;
    
    private boolean autoMode=false;

    public KnowledgeBase(String userName) {
        this.autoBot=new AutoBOT();
        this.autoReplies=new HashMap<>();
        this.messageSequences=new HashMap<>();
        this.userName=userName;
    }
    
    public void setUserEntries(String[] entries) {
        for(String s : entries) this.autoReplies.put(s, null);
    }
    
    public void setDefaultReply(String replyMessage) {
        this.defaultReply=replyMessage;
        
        for(Iterator i=autoReplies.keySet().iterator();i.hasNext();) {
            String uid=(String)i.next();
            this.autoReplies.put(uid, replyMessage);
        }
    } 
    
    public String getDefaultReply() {
        return this.defaultReply;
    }
    
    public void setReply(String userID,String replyMessage) {
        this.autoReplies.put(userID, replyMessage);
    }
    
    public String getReply(String userID, String message, int state) {
        
        if(this.autoMode) {
            if(state==0) return "Hi! I am not here. So you'll be talking to my bot instead. :)";
            else return this.autoBot.getAutoReply(message);
        } else {
            if(this.messageSequences.containsKey(userID)) {
                String[] seqList=this.messageSequences.get(userID);
                if(state>seqList.length-1) return null;
                return seqList[state];
            }

            String reply=this.autoReplies.get(userID);
            if(reply==null) return this.defaultReply;
            else return reply;
        }
    }
    
    public void setMessageSequence(String participant, String[] messages) {
        this.messageSequences.put(participant, messages);
    }
    
    public void saveKnowledgeBase() {
        try {
            ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(this.userName+"_kb.dat"));
            out.writeObject(this.autoReplies);
            out.flush();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(KnowledgeBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadKnowledgeBase() {
        if(new File(this.userName+"_kb.dat").exists()) {
            try {
                ObjectInputStream in=new ObjectInputStream(new FileInputStream(this.userName+"_kb.dat"));
                this.autoReplies=(Map)in.readObject();
            } catch (IOException ex) {
                Logger.getLogger(KnowledgeBase.class.getName()).log(Level.SEVERE, null, ex);
            } catch(ClassNotFoundException ce) {
                ce.printStackTrace();
            }
        } else this.setUserEntries(Main.getUserIDS(userName));
        
        File current=new File(".");
        File[] files=current.listFiles();
        for(File file: files) {
            if(file.isFile() && file.getName().endsWith(".seq")) {
                ArrayList<String> msglist=new ArrayList<>();
                try {
                    Scanner in=new Scanner(new FileInputStream(file));
                    while(in.hasNextLine()) msglist.add(in.nextLine());
                } catch(IOException e) {
                    e.printStackTrace();
                }
                this.messageSequences.put("-"+file.getName().substring(0, file.getName().length()-4)+"@chat.facebook.com", msglist.toArray(new String[0]));
                System.out.println("Loaded kb for "+file.getName());
            }
        }
    }
    
    public void setAutoMode(boolean autoMode) {
        this.autoMode=autoMode;
    }
    
}
