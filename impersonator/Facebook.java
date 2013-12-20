package impersonator;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONObject;

/**
 * @author rajdeep
 *
 */
public class Facebook {
	
	private String app_id;
	
	private String app_secret;
	
	private String redirectURI;
	
	private String permissions;
	
	private String dialogURL="https://www.facebook.com/dialog/";
	
	private String authCode="";
	
	private String authToken="";
	
	private long tokenExpiry=0;
	
	public Facebook(String app_id, String app_secret, String redirectURI) {
		this.app_id=app_id;
		this.app_secret=app_secret;
		this.redirectURI=redirectURI;
	}
	
	public void setPermissions(String permissions) {
		this.permissions=permissions;
	}
	
	public void addPermissions(String permissions) {
		if(this.permissions.isEmpty()) this.permissions=permissions;
		else this.permissions+=","+permissions;
	}
	
	public void setAuthCode(String code) {
		this.authCode=code;
	}
	
	public void setAuthToken(String token) {
		this.authToken=token;
	}
	
	public void showAuthDialog() {
		
		try {
			Map<String, String> params=new HashMap<String, String>();
			
			params.put("client_id", this.app_id);
			params.put("redirect_uri", this.redirectURI);
			params.put("scope", this.permissions);
			
			URI uri=new URI(this.dialogURL+"oauth"+this.constructParameters(params));
			
			Desktop.getDesktop().browse(uri);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getAuthToken() {
		
		if(this.authCode.isEmpty()) {
			this.showAuthDialog();
			return;
		}
		
		String tokenURL="https://graph.facebook.com/oauth/access_token";
		
		Map<String, String> params=new HashMap<String,String>();
		
		params.put("client_id", this.app_id);
		params.put("redirect_uri", this.redirectURI);
		params.put("client_secret", this.app_secret);
		params.put("code", this.authCode);
		
		try {
			URL url=new URL(tokenURL+this.constructParameters(params));
			
			URLConnection con=url.openConnection();
			
			Scanner in=new Scanner(con.getInputStream());
			
			while(in.hasNextLine()) {
				String line=in.nextLine();
				
				if(line.isEmpty()) continue;
				
				String token_data[]=line.split("&");
				
				this.authToken=token_data[0].substring(token_data[0].indexOf('=')+1);
				this.tokenExpiry=Long.parseLong(token_data[1].substring(token_data[1].indexOf('=')+1));
				
				System.out.println(this.authToken);
			}
			
			in.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject fql(String query) throws IOException {
		
		if(this.authToken.isEmpty()) {
			this.getAuthToken();
			return null;
		}
		
		String fqlURL="https://graph.facebook.com/fql";
		
		query=URLEncoder.encode(query, "utf-8");
		
		Map<String, String> params=new HashMap<String,String>();
		
		params.put("q", query);
		params.put("access_token", authToken);
		
		URL url=new URL(fqlURL+this.constructParameters(params));
		
		URLConnection con=url.openConnection();
		
		Scanner in=new Scanner(con.getInputStream());
		
		String response="";
		
		while(in.hasNextLine()) response+=in.nextLine();
		
		return new JSONObject(response);
	}
	
	public String fetchInfo(String uid, String field) throws Exception {
		
		if(this.authToken.isEmpty()) {
			this.getAuthToken();
			return null;
		}
		
		String url="https://graph.facebook.com/"+uid;
		
		URL u=new URL(url+"?access_token="+this.authToken);
		
		URLConnection con=u.openConnection();
		
		Scanner in=new Scanner(con.getInputStream());
		
		String response="";
		
		while(in.hasNextLine()) response+=in.nextLine();
		
		JSONObject json=new JSONObject(response);
		
		return json.getString(field);
	}
	
	private String constructParameters(Map<String, String> params) {
		String parameters="";
		
		for(Iterator<String> i=params.keySet().iterator();i.hasNext();) {
			String param=i.next();
			if(parameters.isEmpty()) parameters+="?"+param+"="+params.get(param);
			else parameters+="&"+param+"="+params.get(param);
		}
		
		return parameters;
	}
	
}
