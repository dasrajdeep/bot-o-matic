package impersonator;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * @author rajdeep
 *
 */
public class ServerHandler extends AbstractHandler {
    
    public ServerHandler() {}
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        if(request.getMethod().equals("GET")) {
            String command=request.getPathInfo().substring(1);
            String result=resolve(command,request.getParameterMap());
            response.getWriter().println(result);
        }
        else {}

        baseRequest.setHandled(true);
    }

    private String resolve(String command, Map params) {
        
        String[] uid=(String[])params.get("uid");
        String[] pass=(String[])params.get("pass");
        
        if(command.equals("login")) {
            return Main.loginUser(uid[0],pass[0]);
        } else if(command.equals("register")) {    
            return Main.registerUser(uid[0],pass[0]);
        } else if(command.equals("startbot")) {
            return Main.startBot(uid[0],pass[0]);
        } else if(command.equals("stopbot")) {
            return Main.stopBot(uid[0],pass[0]);
        } else if(command.equals("setreply")) {
            String[] id=(String[])params.get("id");
            String[] message=(String[])params.get("msg");
            return Main.setReply(uid[0],pass[0],id[0], message[0]);
        } else if(command.equals("setreplyall")){
            String[] message=(String[])params.get("msg");
            return Main.setReplyAll(uid[0],pass[0],message[0]);
        } else if(command.equals("getuserlist")) {
            String userlist=Main.getUserList(uid[0],pass[0]);
            return userlist;
        } else if(command.equals("setauto")) {
            return Main.setAutoMode(uid[0], pass[0]);
        } else if(command.equals("unsetauto")) {
            return Main.unsetAutoMode(uid[0], pass[0]);
        } else return "invalid command";
    }
	
}
