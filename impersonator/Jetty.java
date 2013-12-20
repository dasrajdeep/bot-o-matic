package impersonator;

/**
 * @author rajdeep
 *
 */

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class Jetty {
	
	private Server server=null;
	
	public Jetty() {}
	
	public void startServer(int port) throws Exception {
		
		server=new Server(port);
		
		HandlerList handlers=new HandlerList();
		handlers.addHandler(new ServerHandler());
		handlers.addHandler(this.configureDocumentRoot(".", "index.html"));
		
		server.setHandler(handlers);
		
		server.start();
		//server.join();
	}
	
	public void stopServer() throws Exception {
		this.server.stop();
	}
	
	public Handler configureDocumentRoot(String path, String default_file) {
		ResourceHandler handler=new ResourceHandler();
		handler.setDirectoriesListed(true);
		handler.setWelcomeFiles(new String[] {default_file});
		handler.setResourceBase(path);
		
		return handler;
	}
	
}
