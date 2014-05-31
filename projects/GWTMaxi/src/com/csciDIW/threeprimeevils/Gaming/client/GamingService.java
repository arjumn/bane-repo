package com.csciDIW.threeprimeevils.Gaming.client;

import com.csciDIW.threeprimeevils.Gaming.shared.AmazonData;
import com.csciDIW.threeprimeevils.Gaming.shared.DiggNews;
import com.csciDIW.threeprimeevils.Gaming.shared.GiantBombGame;
import com.csciDIW.threeprimeevils.Gaming.shared.LoginInfo;
import com.csciDIW.threeprimeevils.Gaming.shared.ServerData;
import com.csciDIW.threeprimeevils.Gaming.shared.UserComments;
import com.csciDIW.threeprimeevils.Gaming.shared.UserDeals;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("gamedetails")
public interface GamingService extends RemoteService {
	ServerData getData(String gameName);
	
	GiantBombGame[] getSimilarGames(String gameName);
	
	DiggNews[] getDiggNews(String gameName, String sortOrder);
	
	Boolean insertInitialData();
	
	Boolean addComment(String userName, String userEmail, int id, String gameName, String comment);
	
	UserComments[] getComments(String gameNameInput);
	
	Boolean addDeal(String userName, String userEmail, int id, String gameName, String url, double price);
	
	UserDeals[] getDeals(String gameNameInput);
	
	AmazonData getAmazonData(String gameName);
	
	String[] getGameNames();
	
	LoginInfo login(String requestUri);
}
