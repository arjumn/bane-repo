package com.csciDIW.threeprimeevils.GWTMini.client;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("diggNews")
public interface DiggService extends RemoteService {
  String getNews();
}