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
	
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException {
		
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		
		if(request.getMethod().equals("POST")) this.handleGet(request.getParameterMap());
		else {
			if(request.getParameter("code")!=null) {
				FQueue.fb.setAuthCode(request.getParameter("code"));
				response.getWriter().println("<h1>You are authenticated. You may now close this window.</h1>");
				FQueue.dequeue();
			}
		}
		
		baseRequest.setHandled(true);
	}
	
	private void handleGet(Map<String, String[]> map) {}
	
}
