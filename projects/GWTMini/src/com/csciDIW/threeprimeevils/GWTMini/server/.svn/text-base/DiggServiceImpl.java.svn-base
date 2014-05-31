package com.csciDIW.threeprimeevils.GWTMini.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.csciDIW.threeprimeevils.GWTMini.client.DiggService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DiggServiceImpl extends RemoteServiceServlet implements
		DiggService {

	private static final long serialVersionUID = 1L;

	@Override
	public String getNews() {
		String json = new String("");
		try{
			URL url = new URL("http://services.digg.com/2.0/story.getTopNews");
	        URLConnection urlc = url.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                urlc.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null){ 
	            json+=inputLine.trim();
	        }
	        in.close();
		}catch(MalformedURLException e){
			System.err.println(e.getMessage());
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		return "[" + json + "]";
	}
}
