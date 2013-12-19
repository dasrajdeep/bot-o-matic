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
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
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
    }
    
    public Collection getRoster() {
        Collection<RosterEntry> entries=this.connection.getRoster().getEntries();
        return entries;
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
                out.println(message);
            }
        });
        
        return chat;
    }
    
    public void sendMessage(Chat chat, String receiverID, String messageText) throws XMPPException{
        Message message = new Message(receiverID, Message.Type.chat);
        message.setBody(messageText);

        chat.sendMessage(message);
    }
}
