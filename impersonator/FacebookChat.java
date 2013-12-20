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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

/**
 *
 * @author Rajdeep Das <rajdeepd@iitk.ac.in>
 */
public class FacebookChat {
    
    public static final String XMPP_HOST="chat.facebook.com";
    public static final int XMPP_PORT=5222;
    
    private String userID=null;
    private String password=null;
    
    private Connection connection=null;
    private ConnectionConfiguration config=null;
    private ChatManager chatManager=null;
    private Roster roster=null;
    
    public FacebookChat(String userID, String password) {
        
        this.userID=userID;
        this.password=password;
        
        SASLAuthentication.registerSASLMechanism("DIGEST-MD5", CustomSASLDigestMD5Mechanism.class);
        SASLAuthentication.supportSASLMechanism("DIGEST-MD5", 0);
        
        this.config=new ConnectionConfiguration(XMPP_HOST, XMPP_PORT);
        this.config.setSASLAuthenticationEnabled(true);
    }
    
    public void connect() throws XMPPException {
        this.connection=new XMPPConnection(config);
        this.connection.connect();
        this.connection.login(this.userID, this.password, "impersonator");
        this.chatManager=connection.getChatManager();
        this.chatManager.addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                if(!createdLocally) chat.addMessageListener(new MessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) {
                        handleIncomingMessage(chat, message);
                    }
                });
            }
        });
        
        this.roster=connection.getRoster();
        this.roster.addRosterListener(new RosterListener() {

            @Override
            public void entriesAdded(Collection<String> addresses) {}

            @Override
            public void entriesUpdated(Collection<String> addresses) {}

            @Override
            public void entriesDeleted(Collection<String> addresses) {}

            @Override
            public void presenceChanged(Presence presence) {
                handlePresenceChange(presence);
            }
        });
    }
    
    public Collection getRoster() {
        return this.roster.getEntries();
    }
    
    public Collection getOnlineFriends() {
        Roster roster=this.connection.getRoster();
        Collection<RosterEntry> entries=roster.getEntries();
        Collection<RosterEntry> onlineUsers=new ArrayList<RosterEntry>();
        
        for(RosterEntry user:entries) {
            Presence presence=roster.getPresence(user.getUser());
            if(presence.isAvailable()) onlineUsers.add(user);
        }
        
        return onlineUsers;
    }
    
    public Chat startChat(String jabberID, final PrintWriter out) {
        Chat chat=this.connection.getChatManager().createChat(jabberID, new MessageListener() {

            @Override
            public void processMessage(Chat chat, Message message) {
                handleIncomingMessage(chat, message);
            }
        });
        
        return chat;
    }
     
    public void sendMessage(Chat chat, String receiverID, String messageText) throws XMPPException{
        Message message = new Message(receiverID, Message.Type.chat);
        message.setBody(messageText);

        chat.sendMessage(message);
    }
    
    private void handleIncomingMessage(Chat chat, Message message) {
        String userName=this.roster.getEntry(chat.getParticipant()).getName();
        
        System.out.println(userName+" says: "+message.getBody());
        
        try {
            this.sendMessage(chat, chat.getParticipant(), "Hello! This is virtual me in place of me! If you need to talk to the real me, you should ping sometime later. Cheers! :)");
        } catch(XMPPException e) {
            e.printStackTrace();
        }
    }
    
    private void handlePresenceChange(Presence presence) {
        String userName=this.roster.getEntry(presence.getFrom()).getName();
        
        System.out.println(userName+" "+presence.toString());
    }
    
}
