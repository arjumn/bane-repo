package com.csciDIW.threeprimeevils.GWTMini.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DiggServiceAsync {

	void getNews(AsyncCallback<String> callback);

}
