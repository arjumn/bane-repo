package com.csciDIW.threeprimeevils.Gaming.client;

import com.csciDIW.threeprimeevils.Gaming.shared.AmazonData;
import com.csciDIW.threeprimeevils.Gaming.shared.DiggNews;
import com.csciDIW.threeprimeevils.Gaming.shared.GiantBombGame;
import com.csciDIW.threeprimeevils.Gaming.shared.LoginInfo;
import com.csciDIW.threeprimeevils.Gaming.shared.ServerData;
import com.csciDIW.threeprimeevils.Gaming.shared.UserComments;
import com.csciDIW.threeprimeevils.Gaming.shared.UserDeals;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GamingServiceAsync {
	void getData(String gameName, AsyncCallback<ServerData> callback)
		throws IllegalArgumentException;
	
	void getSimilarGames(String gameName, AsyncCallback<GiantBombGame[]> callback)
		throws IllegalArgumentException;
	
	void getDiggNews(String gameName, String sortOrder, AsyncCallback<DiggNews[]> callback)
		throws IllegalArgumentException;
	
	void insertInitialData(AsyncCallback<Boolean> callback)
		throws IllegalArgumentException;
	
	void addComment(String userName, String userEmail, int id, String gameName, String comment, AsyncCallback<Boolean> isInsertSuccess)
		throws IllegalArgumentException;
	
	void getComments(String gameNameInput, AsyncCallback<UserComments[]> userComments)
		throws IllegalArgumentException;
	
	void addDeal(String userName, String userEmail, int id, String gameName, String url, double price, AsyncCallback<Boolean> isInsertSuccess)
		throws IllegalArgumentException;
	
	void getDeals(String gameNameInput, AsyncCallback<UserDeals[]> userComments)
		throws IllegalArgumentException;
	
	void getAmazonData(String gameName, AsyncCallback<AmazonData> amazonData)
		throws IllegalArgumentException;
	
	void getGameNames(AsyncCallback<String[]> gameNames)
		throws IllegalArgumentException;
	
	void login(String requestUri, AsyncCallback<LoginInfo> loginInfo)
		throws IllegalArgumentException;
}
