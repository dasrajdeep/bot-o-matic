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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;

/**
 *
 * @author Rajdeep Das <rajdeepd@iitk.ac.in>
 */
public class Main {
    
    public static ServerHandler serverHandler;
    public static Map<String,BOT> bots;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        bots=new HashMap<>();
        
        try {
            Scanner in=new Scanner(new FileInputStream("user.config"));
            while(in.hasNextLine()) {
                String user=in.nextLine().trim();
                if(user.isEmpty()) continue;
                String[] creds=user.split(":");
                BOT bot=new BOT(creds[0], creds[1]);
                bots.put(creds[0], bot);
            }
            
            serverHandler=new ServerHandler();
            Jetty server=new Jetty(serverHandler);
            
            server.startServer(8080);
            
            System.out.println("Server started.");
            
            while(true) {}
        } catch(Exception e) {
            e.printStackTrace();
        }
    }   
    
    public static String startBot(String userName,String password) {
        BOT bot=bots.get(userName);
        if(bot.getPassword().equals(password)) return bot.startBot();
        else return "not authorized";
    }
    
    public static String stopBot(String userName,String password) {
        BOT bot=bots.get(userName);
        if(bot.getPassword().equals(password)) return bot.stopBot();
        else return "not authorized";
    }
    
    public static String setReply(String userName,String password, String id, String message) {
        BOT bot=bots.get(userName);
        if(bot.getPassword().equals(password)) return bot.setReply(id,message);
        else return "not authorized";
    }
    
    public static String setReplyAll(String userName,String password,String message) {
        BOT bot=bots.get(userName);
        if(bot.getPassword().equals(password)) return bot.setReplyAll(message);
        else return "not authorized";
    }
    
    public static String getUserList(String userName,String password) {
        BOT bot=bots.get(userName);
        if(bot.getPassword().equals(password)) return bot.getUserList();
        else return "not authorized";
    }
    
    public static String setAutoMode(String userName,String password) {
        BOT bot=bots.get(userName);
        if(bot.getPassword().equals(password)) return bot.setAutoMode();
        else return "not authorized";
    }
    
    public static String unsetAutoMode(String userName,String password) {
        BOT bot=bots.get(userName);
        if(bot.getPassword().equals(password)) return bot.unsetAutoMode();
        else return "not authorized";
    }
    
    public static String loginUser(String userName,String password) {
        BOT bot=bots.get(userName);
        if(bot!=null && bot.getPassword().equals(password)) return "true";
        else if(bot!=null && !bot.getPassword().equals(password)) return "false";
        else {
            registerUser(userName, password);
            return "true";
        }
    }
    
    public static String registerUser(String userName, String password) {
        try {
            PrintWriter out=new PrintWriter(new FileWriter("user.config",true));
            out.println(userName+":"+password);
            out.flush();
            out.close();
            System.out.println("User registered.");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        BOT bot=new BOT(userName, password);
        bots.put(userName,bot);
        
        return "BOT registered.";
    }
    
    public static String[] getUserIDS(String userName) {
        BOT bot=bots.get(userName);
        return bot.getUserIDS();
    }
    
}
