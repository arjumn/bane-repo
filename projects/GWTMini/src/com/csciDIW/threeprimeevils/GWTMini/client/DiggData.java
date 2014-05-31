package com.csciDIW.threeprimeevils.GWTMini.client;
import com.google.gwt.core.client.JavaScriptObject;

public class DiggData extends JavaScriptObject{
	protected DiggData(){}
	
	public final native String getTitle() /*-{ return this.title }-*/;
	public final native String getDescription() /*-{ return this.description }-*/;
	public final native int getDiggs() /*-{ return this.diggs }-*/;
	public final native String getURL() /*-{ return this.url }-*/;
};