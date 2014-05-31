package com.csci5980.primeevils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.AdapterView.OnItemClickListener;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class SwissRoll extends TabActivity {

	// The Application id in facebook. 
	public static final String APP_ID = "118362694909509";
	
	String texts[] = new String[] {
	"This is gonna be a big string. this is just to text how it works out. one-line or two-lines or three-lines",
	"This is gonna be a big string. this is just to text how it works out. one-line or two-lines or three-lines",
	"This is gonna be a big string. this is just to text how it works out. one-line or two-lines or three-lines",
	"This is gonna be a big string. this is just to text how it works out. one-line or two-lines or three-lines",
	"This is gonna be a big string. this is just to text how it works out. one-line or two-lines or three-lines"
	};

	// facebook related variables
	private Facebook mFacebook;
	private long facebookId;
	private ArrayList<Long> friends;
	private String currentUserName;
	private Handler mHandler;
	
	//Proximity alert variable
	private LocationManager locationManager;
	private ArrayList<PendingIntent> pendingIntents;
	
	// Variables related to Async calls to the database and facebook
	private int asyncCount = 0;
	private AsyncFacebookRunner mAsyncRunner;
	private ProgressDialog progressDialog;
	private ProgressDialog logoutDialog;
	
	private TabHost mTabHost; // The tab host variable
	
	/*
	 *  These are the widgets in the add item page. This has to be called over and over again
	 *  So it is better to keep that as a global variable
	 */
	EditText addItemTv;
	EditText hiddenTv;
	Spinner addItemQty;
	Spinner addItemShop;
	ImageButton addItemButton;

	/*
	 *  The list of the objects with which the home page list is populated.
	 *  This maps the actual objects to the strings in the list view. 
	 */	
	JSONArray homeListItems;
	
	/*
	 * The list of the objects with which the profile page list is populated.
	 * This maps the actual objects to the strings in the list view.
	 */
	JSONArray profileListItems;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        authFacebook();
        
        mTabHost = getTabHost();
        
        mTabHost.addTab(mTabHost.newTabSpec("tab_home").setIndicator("Home").setContent(R.id.homeLayout));
        mTabHost.addTab(mTabHost.newTabSpec("tab_profile").setIndicator("Profile").setContent(R.id.profileListView));
        mTabHost.addTab(mTabHost.newTabSpec("tab_addItem").setIndicator("Add Item").setContent(R.id.addItemLayout));
        
        mTabHost.setCurrentTab(0);
        addItemTv = (EditText) findViewById(R.id.itemName);
        hiddenTv = (EditText) findViewById(R.id.hiddenText);
        addItemQty = (Spinner) findViewById(R.id.quantity);
        addItemShop = (Spinner) findViewById(R.id.shop);
        addItemButton = (ImageButton) findViewById(R.id.deleteItem);
        
        
        /*
         * Tab widget onclick events
         */
        TabWidget tw = getTabWidget();
        
        tw.getChildAt(0).setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getItemsByUsers(friends);
				mTabHost.setCurrentTab(0);
			}
		});
        tw.getChildAt(1).setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getItemsByUser(facebookId);
				mTabHost.setCurrentTab(1);
			}
		});
        tw.getChildAt(2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	addItemTv.setText("");
            	hiddenTv.setText(Integer.toString(-1));
            	addItemQty.setSelection(0);
            	addItemShop.setSelection(0);
            	addItemButton.setVisibility(ImageButton.INVISIBLE);
            	mTabHost.setCurrentTab(2);
            }
        });
		
        /********************************************************************/
        
		getFriends();	 // Get the ids of the friends to view their updates
		getFacebookId(); // Get the facebook Id of the user

		setAddItemView();
		
		initProximityAlert();
    }

	private void setAddItemView() {
	    Spinner spinner1 = (Spinner) findViewById(R.id.shop);
	    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
	            this, R.array.Shops, android.R.layout.simple_spinner_item);
	    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner1.setAdapter(adapter1);
	    
	    Spinner spinner2 = (Spinner) findViewById(R.id.quantity);
	    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
	            this, R.array.Quantity, android.R.layout.simple_spinner_item);
	    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner2.setAdapter(adapter2);
	}


	private void SetHomePageListView() {
        final ListView lv = (ListView) findViewById(R.id.homeListView);
        lv.setTextFilterEnabled(true);
        String itemStrArray[] = new String[homeListItems.length()];
        
        try {
	        for(int i = 0; i < homeListItems.length(); i++) {
	        	JSONObject json_data = homeListItems.getJSONObject(i);
	        	itemStrArray[i] = json_data.getString("item_name") + " by " + 
	        					  json_data.getString("user_name") + " from " + 
	        					  json_data.getString("shop_name") + ".\nQuantity: " + 
	        					  json_data.getInt("item_quantity");
	        }
        }
        catch(JSONException e) {
        	Log.e("log_tag", "Error parsing data "+e.toString());
        }

        ListAdapter la = new ArrayAdapter<String>(this, R.layout.home_list_layout, itemStrArray);	        
        //ListAdapter la = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, itemStrArray);
        lv.setAdapter(la);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	//selectedItems += lv.getItemAtPosition(position).toString();
            }	
        });	
    }
	
	private void SetProfilePageListView() {
	    final ListView lv = (ListView) findViewById(R.id.profileListView);
        lv.setTextFilterEnabled(true);
        ArrayList<ListItem> list_items = new ArrayList<ListItem>();
        try {
	        for(int i = 0; i < profileListItems.length(); i++) {
	        	JSONObject json_data = profileListItems.getJSONObject(i);
	        	ListItem item = new ListItem();
	        	item.setId( json_data.getInt("id") );
	        	item.setItemName( json_data.getString("item_name") );
	        	item.setShopName( json_data.getString("shop_name") );
	        	item.setQuantity( json_data.getInt("item_quantity") );
	        	if( !("null".equals(json_data.getString("bought_by"))) )
	        		item.setBought( json_data.getString("bought_by") );
	        	list_items.add(item);
	        }
        }
        catch(JSONException e) {
        	Log.e("log_tag", "Error parsing data "+e.toString());
        }

        lv.setAdapter(new customAdapter(this, list_items));

        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	editItem(position);
            }

            private void editItem(int pos) {
				try {
					JSONObject listObj = profileListItems.getJSONObject(pos);
					if( "null".equals(listObj.getString("bought_by").toString()) ) {
						addItemTv.setText( listObj.getString("item_name").toString() );
						hiddenTv.setText(listObj.getInt("id")+"");
						addItemQty.setSelection( listObj.getInt("item_quantity") - 1 );
						addItemShop.setSelection( getPosition(listObj.getString("shop_name").toString()) );
						addItemButton.setVisibility(ImageButton.VISIBLE);
						
						mTabHost.setCurrentTab(2);
					}
				}
		        catch(JSONException e) {
		        	Log.e("log_tag", "Error parsing data "+e.toString());
		        }
			}

			private int getPosition(String string) {
				if( "Cub Foods".equals(string) )
					return 0;
				else if( "Rainbow".equals(string) )
					return 1;
				else if( "Target".equals(string) )
					return 2;
				else
					return 3;
			}
        });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.logout_menu, menu);
	    return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.logout:
	    	logOut();
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	/******************** Button onClick events ********************************************/

	// This function is for the button on the additem page. It saves to database a particular item
	public void saveItem(View view) {
		if( addItemTv.getText().toString().length() > 0 )
		{
			Log.d("SaveItem", "SaveItem to database gonna be called");
			Log.d("SaveItem", "FacebookId:"+facebookId);
			Log.d("SaveItem","itemName:" + addItemTv.getText().toString());
			Log.d("SaveItem","itemQuantity: " + Integer.parseInt(addItemQty.getSelectedItem().toString()));
			Log.d("SaveItem","shopName: " + addItemShop.getSelectedItem().toString());
			Log.d("SaveItem","userName: " + currentUserName);
			
			if( Integer.parseInt(hiddenTv.getText().toString()) == -1 )
				saveItem(facebookId, addItemTv.getText().toString(), 
						 Integer.parseInt(addItemQty.getSelectedItem().toString()), 
						 addItemShop.getSelectedItem().toString(), currentUserName);
			else
				updateItem(Integer.parseInt(hiddenTv.getText().toString()),
						   addItemTv.getText().toString(), 
						   Integer.parseInt(addItemQty.getSelectedItem().toString()), 
						   addItemShop.getSelectedItem().toString());
		}
	}

	// This function is for the button in the home page. It saves to database the current
	// transactions that the user made
	public void itemsBought(View view) {
        final ListView lv = (ListView) findViewById(R.id.homeListView);
        SparseBooleanArray checkedPositions = lv.getCheckedItemPositions();
        String commaSeparatedIds = new String();
		if (checkedPositions != null)
		{
		    int count = lv.getCount();
		    for ( int i=0;i<count;i++ )
		    {
		        if( checkedPositions.get(i) == true ) {
		        	try {
			        	JSONObject obj = homeListItems.getJSONObject(i);
		        		commaSeparatedIds += obj.getInt("id")+",";
		        	}
			        catch(JSONException e) {
			        	Log.e("log_tag", "Error parsing data "+e.toString());
			        }
		        }
		    }
		}
		if( !("".equals(commaSeparatedIds)) )
			itemsBought( commaSeparatedIds.substring(0, commaSeparatedIds.length()-1), currentUserName );
	}
	
	// This function is for the delete button in the Add item page. It deletes an 
	// item from the database
	public void deleteItem(View view) {
		int id = Integer.parseInt(hiddenTv.getText().toString());
		deleteItem(id);		
	}
	
    /*********************************************************************************************************************
     **													Sairam Functions												**
     *********************************************************************************************************************/
	
	/*************************************            Facebook Function                      ***********************/
	
	private void showProgressDialog(){
    	asyncCount++;
    	if(asyncCount == 1){
    		progressDialog = ProgressDialog.show(
                    SwissRoll.this,
                    "Please wait...contacting Facebook!", // title
                    "Requesting your information", // message
                    true // indeterminate
            );
    	}
    }
    
    private void closeProgressDialog(){
    	asyncCount--;
    	if(asyncCount == 0){
    		progressDialog.cancel();
    	}
    }

    private void authFacebook(){
    	mHandler = new Handler();
    	mFacebook = new Facebook(APP_ID);
       	SessionStore.restore(mFacebook, this);
       	mAsyncRunner = new AsyncFacebookRunner(mFacebook);
    }
    
    private void logOut(){
    	logoutDialog = ProgressDialog.show(
                SwissRoll.this,
                "Please wait...",
                "Contacting Facebook", // message
                true // indeterminate
        );
    	logoutDialog.show();
    	SessionEvents.onLogoutBegin();
        AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFacebook);
        asyncRunner.logout(SwissRoll.this, new LogoutRequestListener());
    }
    
    private void getFriends() {
    	showProgressDialog();
    	mAsyncRunner.request("me/friends", new GetFriendsListener());
    }
    
    private void getFacebookId(){
    	showProgressDialog();
    	mAsyncRunner.request("me", new GetFacebookIdListener());
    }
    
    private class LogoutRequestListener extends BaseRequestListener {
        public void onComplete(String response, final Object state) {
            // callback should be run in the original thread, 
            // not the background thread
            mHandler.post(new Runnable() {
                public void run() {
                	SessionStore.clear(SwissRoll.this);
                	removeProximityAlerts();
                	logoutDialog.cancel();
                	gotoLoginPage();
                }
            });
        }
    }
    
    public void gotoLoginPage(){
    	Intent loginIntent = new Intent(SwissRoll.this, LoginScreen.class);
        startActivityForResult(loginIntent, 0);
    }

	
	public class GetFriendsListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
        	closeProgressDialog();
            try {
                // process the response here: executed in background thread
                Log.d("Get friends listener", response.toString());
                JSONObject jsonObject = Util.parseJson(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                friends = new ArrayList<Long>(jsonArray.length());
                for(int i=0; i<jsonArray.length(); i++)
                	friends.add(jsonArray.getJSONObject(i).getLong("id"));

                SwissRoll.this.runOnUiThread(new Runnable() {
                    public void run() {
                    	getItemsByUsers(friends);
                    }
                });
            	
            } catch (JSONException e) {
                Log.w("Get Friends Listener", "JSON Error in response: " + e.getMessage());
            } catch (FacebookError e) {
                Log.w("Get Friends Listener", "Facebook Error: " + e.getMessage());
            }
        }
    }
	
	public class GetFacebookIdListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
        	closeProgressDialog();
            try {
                JSONObject jsonObject = Util.parseJson(response);
                facebookId = jsonObject.getLong("id");
                currentUserName = jsonObject.getString("name");
                Log.d("Get Facebook Id listener", facebookId +"");
                SwissRoll.this.runOnUiThread(new Runnable() {
                    public void run() {
                    	getItemsByUser(facebookId);
                    }
                });
            } catch (JSONException e) {
                Log.w("Get Friends Listener", "JSON Error in response: " + e.getMessage());
            } catch (FacebookError e) {
                Log.w("Get Friends Listener", "Facebook Error: " + e.getMessage());
            }
        }
    }
	
	/*******************                Database Functions         **********************************/
	
	@SuppressWarnings("unchecked")
	public void getItemsByUsers(ArrayList<Long> userIDs){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("function","getItemsByUsers"));
		nameValuePairs.add(new BasicNameValuePair("userIds", arrayToString(userIDs)));
		new DatabaseRelated().execute(nameValuePairs);
	}
	
	@SuppressWarnings("unchecked")
	public void getItemsByUser(long userID){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("function","getItemsByUser"));
		nameValuePairs.add(new BasicNameValuePair("userId", userID+""));
		new DatabaseRelated().execute(nameValuePairs);
	}
	
	@SuppressWarnings("unchecked")
	public void saveItem(long userID, String itemName, int itemQuantity, String shopName, String userName){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("function","saveItem"));
		nameValuePairs.add(new BasicNameValuePair("userID", userID+""));
		nameValuePairs.add(new BasicNameValuePair("itemName", itemName));
		nameValuePairs.add(new BasicNameValuePair("itemQuantity", itemQuantity+""));
		nameValuePairs.add(new BasicNameValuePair("shopName", shopName));
		nameValuePairs.add(new BasicNameValuePair("userName", userName));
		Log.d("SaveItem", "DatabaseRelated.execute() to be called");
		new DatabaseRelated().execute(nameValuePairs);
	}

	@SuppressWarnings("unchecked")
	public void updateItem(int id, String itemName, int itemQuantity, String shopName){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("function","updateItem"));
		nameValuePairs.add(new BasicNameValuePair("id", id+""));
		nameValuePairs.add(new BasicNameValuePair("itemName", itemName));
		nameValuePairs.add(new BasicNameValuePair("itemQuantity", itemQuantity+""));
		nameValuePairs.add(new BasicNameValuePair("shopName", shopName));
		Log.d("SaveItem", "DatabaseRelated.execute() to be called");
		new DatabaseRelated().execute(nameValuePairs);
	}
	
	@SuppressWarnings("unchecked")
	public void deleteItem(int id){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("function","deleteItem"));
		nameValuePairs.add(new BasicNameValuePair("id", id+""));
		Log.d("SaveItem", "DatabaseRelated.execute() to be called");
		new DatabaseRelated().execute(nameValuePairs);
	}

	@SuppressWarnings("unchecked")
	public void itemsBought(String Ids, String userName){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("function","itemsBought"));
		nameValuePairs.add(new BasicNameValuePair("ids", Ids));
		nameValuePairs.add(new BasicNameValuePair("userName", userName));
		Log.d("SaveItem", "DatabaseRelated.execute() to be called");
		new DatabaseRelated().execute(nameValuePairs);
	}
	
	@SuppressWarnings("unchecked")
	public void getLocations(){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("function","getLocations"));
		new DatabaseRelatedBackGround().execute(nameValuePairs);
	}
	
	public static String arrayToString(ArrayList<Long> array) {
	      int len  = array.size();
	      int last = len - 1;
	      StringBuffer sb   = new StringBuffer(2 * (len + 1));
	      sb.append('(');
	      for (int i = 0; i < len; i++) {
	          sb.append(array.get(i));
	          if (i != last) {
	              sb.append(',');
	          }
	      }
	      sb.append(')');
	      return sb.toString();
	  }
	
	public class DatabaseRelated extends AsyncTask <ArrayList<NameValuePair>, Void, String>{
		
		private static final String connectionURL = "http://ec2-50-16-95-225.compute-1.amazonaws.com/csci5980-evils/AndroidMaxi/dataConnect.php";
		private InputStream inputStream = null;
		private ProgressDialog progressDialog; 
		
		@Override
	    protected void onPreExecute() {
			Log.d("Database", "Database request sent");
	        this.progressDialog = ProgressDialog.show(
	                SwissRoll.this,
	                "Please wait...",
	                "Contacting the database",
	                true
	        );
	    }
		
		@Override
	    protected String doInBackground(ArrayList<NameValuePair>... params) {
			String result = "";
			ArrayList<NameValuePair> nameValuePair = params[0];
			try{
				HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost(connectionURL);
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        inputStream = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		        }
		        inputStream.close();
		        result=sb.toString();
			}catch(Exception e){
				Log.d("Database related", "Exception: " + e.getMessage());
			}
			return result;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	Log.d("Database Returned1", "Result: " + result);
	        this.progressDialog.cancel();
	        try {
	        	JSONObject jsonObject = new JSONObject(result);
	        	String functionCalled = jsonObject.getString("function");
	        	
	        	if(functionCalled.equalsIgnoreCase("saveItem") || 
	        	   functionCalled.equalsIgnoreCase("updateItem") || 
	        	   functionCalled.equalsIgnoreCase("deleteItem")) {
	        		SwissRoll.this.runOnUiThread(new Runnable() {
	                    public void run() {
	                    	getItemsByUser(facebookId);
	                    }
	                });
	        		mTabHost.setCurrentTab(1);
	        	}
	        	else if(functionCalled.equalsIgnoreCase("itemsBought")) {
	        		SwissRoll.this.runOnUiThread(new Runnable() {
	                    public void run() {
	                    	getItemsByUsers(friends);
	                    }
	                });
	        	}
	        	else if(functionCalled.equalsIgnoreCase("getItemsByUsers")) {
	        		if( homeListItems != null )
	        			homeListItems = null;
        			homeListItems = jsonObject.getJSONArray("data");
	        		/*
			        for(int i=0;i<jArray.length();i++) {
			                JSONObject json_data = jArray.getJSONObject(i);
			                Log.d("Database Returned2", "id:"+json_data.getInt("id")+ 
			                		"userid: "+json_data.getInt("userid")+
			                        ", item_name: "+json_data.getString("item_name")+
			                        ", item_quantity: "+json_data.getInt("item_quantity")+
			                        ", shop_name: "+json_data.getString("shop_name"));
			        }
			        */
			        SetHomePageListView();

	        	}
	        	else if(functionCalled.equalsIgnoreCase("getItemsByUser")) {
	        		if( profileListItems != null )
	        			profileListItems = null;
	        		profileListItems = jsonObject.getJSONArray("data");
	        		/*
			        for(int i=0;i<jArray.length();i++) {
			                JSONObject json_data = jArray.getJSONObject(i);
			                Log.d("Database Returned3", "userid: "+json_data.getInt("userid")+
			                        ", item_name: "+json_data.getString("item_name")+
			                        ", item_quantity: "+json_data.getInt("item_quantity")+
			                        ", shop_name: "+json_data.getString("shop_name"));
			        }
			        */

			        SetProfilePageListView();
	        	}
	        	else if(functionCalled.equalsIgnoreCase("getLocations")){
	        		JSONArray data = jsonObject.getJSONArray("data");
	        		if(pendingIntents.size() != 0){
		        		for(int i=0; i< data.length(); i++){
		        			Location location = new Location("POINT_LOCATION");
		        			JSONObject jsonLocation = data.getJSONObject(i);
		        			double lat = (double)jsonLocation.getDouble("lat");
		        			double lon = (double)jsonLocation.getDouble("lon");
		        			String shopName = jsonLocation.getString("name");
		        			location.setLatitude(lat);
		        			location.setLongitude(lon);
		        			addLocationProximityAlert(location, new Long(i), shopName, i+1);
		        		}
	        		}
	        	}
	        }
	        catch(JSONException e){
	        	Log.e("Database Returned4", "Error parsing data "+e.toString());
			}        
	    }
	}
	
public class DatabaseRelatedBackGround extends AsyncTask <ArrayList<NameValuePair>, Void, String>{
		
		private static final String connectionURL = "http://ec2-50-16-95-225.compute-1.amazonaws.com/csci5980-evils/AndroidMaxi/dataConnect.php";
		private InputStream inputStream = null;
		
		@Override
	    protected void onPreExecute() {
		}
		
		@Override
	    protected String doInBackground(ArrayList<NameValuePair>... params) {
			String result = "";
			ArrayList<NameValuePair> nameValuePair = params[0];
			try{
				HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost(connectionURL);
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        inputStream = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		        }
		        inputStream.close();
		        result=sb.toString();
			}catch(Exception e){
				Log.d("Database related", "Exception: " + e.getMessage());
			}
			return result;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	Log.d("Database Returned1", "Result: " + result);
	        try {
	        	JSONObject jsonObject = new JSONObject(result);
	        	String functionCalled = jsonObject.getString("function");
	        	
	        	if(functionCalled.equalsIgnoreCase("getLocations")){
	        		JSONArray data = jsonObject.getJSONArray("data");
	        		if(pendingIntents.size() == 0){
		        		for(int i=0; i< data.length(); i++){
		        			Location location = new Location("POINT_LOCATION");
		        			JSONObject jsonLocation = data.getJSONObject(i);
		        			double lat = (double)jsonLocation.getDouble("lat");
		        			double lon = (double)jsonLocation.getDouble("lon");
		        			String shopName = jsonLocation.getString("name");
		        			location.setLatitude(lat);
		        			location.setLongitude(lon);
		        			addLocationProximityAlert(location, new Long(i), shopName, i+1);
		        		}
	        		}
	        	}
	        }
	        catch(JSONException e){
	        	Log.e("Database Returned4", "Error parsing data "+e.toString());
			}        
	    }
	}
	
	/*************************************** Proximity Functions ******************************************/

	public String removeSpaces(String input){
		return input.replace(" ", "");
	}
	
	public void addLocationProximityAlert(Location location, Long eventID, String shopName, int requestCode){
    	locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    	
    	Log.d("Adding Prox Alert", location.getLatitude() + ":" + location.getLongitude() + ":" + eventID);
    	final long POINT_RADIUS = 100; // in Meters
        final long PROX_ALERT_EXPIRATION = -1;
        String PROX_ALERT_INTENT = "com.csci5980.primeevils." + removeSpaces(shopName);
        Intent intent = new Intent(PROX_ALERT_INTENT);
        intent.putExtra(ProximityIntentReceiver.EVENT_ID_INTENT_EXTRA, eventID);
        intent.putExtra(ProximityIntentReceiver.SHOP_NAME_INTENT_EXTRA, shopName);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        
        pendingIntents.add(proximityIntent);
        
        locationManager.addProximityAlert(
            location.getLatitude(), // the latitude of the central point of the alert region
            location.getLongitude(), // the longitude of the central point of the alert region
            POINT_RADIUS, // the radius of the central point of the alert region, in meters
            PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration 
            proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
       );
        
       IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);  
       registerReceiver(new ProximityIntentReceiver(), filter);
    }
	
	public void removeProximityAlerts(){
		for(PendingIntent intent : pendingIntents)
			locationManager.removeProximityAlert(intent);
		pendingIntents.clear();
	}
	
	public void initProximityAlert(){
		if(pendingIntents == null || pendingIntents.size() <= 0){
			pendingIntents = new ArrayList<PendingIntent>();
			getLocations();
		}
    }
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	public class ProximityIntentReceiver extends BroadcastReceiver {
	    
	    private static final int NOTIFICATION_ID = 1000;
		public static final String EVENT_ID_INTENT_EXTRA = "EventIDIntentExtraKey";
		public static final String SHOP_NAME_INTENT_EXTRA = "ShopNameIntentExtraKey";
		private Context context;

	    @Override
	    public void onReceive(Context context, Intent intent) {
	        this.context = context;
	        String key = LocationManager.KEY_PROXIMITY_ENTERING;
	        Boolean entering = intent.getBooleanExtra(key, false);
	        String shopName = intent.getStringExtra(SHOP_NAME_INTENT_EXTRA);
	        if (entering) {
	            Log.d(getClass().getSimpleName(), "entering");
	            Log.d("Friends in proximity receiver", friends.size()+"");
	            getItemsByUsersAndLocation(friends, shopName);
	        }
	    }
	    
	    public void showNotification(String shopName){
	    	Log.d(getClass().getSimpleName(), "ShopName" + shopName);
	    	NotificationManager notificationManager = 
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = createNotification();
            Intent notifyIntent = new Intent(Intent.ACTION_MAIN);
            notifyIntent.setClass(context, SwissRoll.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);        
            notification.setLatestEventInfo(context, "You are near " + shopName, "Your friend(s) need items from there", pendingIntent);
            notificationManager.notify(NOTIFICATION_ID, notification);
	    }
	    
	    @SuppressWarnings("unchecked")
		public void getItemsByUsersAndLocation(ArrayList<Long> userIDs, String location){
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("userIds", arrayToString(userIDs)));
			nameValuePairs.add(new BasicNameValuePair("shop_name", location));
			nameValuePairs.add(new BasicNameValuePair("function","getItemsByUsersAndLocation"));
			new DatabaseRelatedBackGround().execute(nameValuePairs);
		}

	    private Notification createNotification() {
	        Notification notification = new Notification();
	        
	        notification.icon = R.drawable.ic_menu_notifications;
	        notification.when = System.currentTimeMillis();
	        
	        notification.flags |= Notification.FLAG_AUTO_CANCEL;
	        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
	        
	        notification.defaults |= Notification.DEFAULT_VIBRATE;
	        notification.defaults |= Notification.DEFAULT_LIGHTS;
	        
	        notification.ledARGB = Color.WHITE;
	        notification.ledOnMS = 1500;
	        notification.ledOffMS = 1500;
	        
	        return notification;
	    }
	    
	    public class DatabaseRelatedBackGround extends AsyncTask <ArrayList<NameValuePair>, Void, String>{
			
			private static final String connectionURL = "http://ec2-50-16-95-225.compute-1.amazonaws.com/csci5980-evils/AndroidMaxi/dataConnect.php";
			private InputStream inputStream = null;
			
			@Override
		    protected void onPreExecute() {
			}
			
			@Override
		    protected String doInBackground(ArrayList<NameValuePair>... params) {
				String result = "";
				ArrayList<NameValuePair> nameValuePair = params[0];
				try{
					HttpClient httpclient = new DefaultHttpClient();
			        HttpPost httppost = new HttpPost(connectionURL);
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			        HttpResponse response = httpclient.execute(httppost);
			        HttpEntity entity = response.getEntity();
			        inputStream = entity.getContent();
			        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
			        StringBuilder sb = new StringBuilder();
			        String line = null;
			        while ((line = reader.readLine()) != null) {
			                sb.append(line + "\n");
			        }
			        inputStream.close();
			        result=sb.toString();
				}catch(Exception e){
					Log.d("Database related", "Exception: " + e.getMessage());
				}
				return result;
		    }

		    @Override
		    protected void onPostExecute(String result) {
		    	Log.d("Database Returned1", "Result: " + result);
		        try {
		        	JSONObject jsonObject = new JSONObject(result);
		        	String functionCalled = jsonObject.getString("function");
		        	if(functionCalled.equalsIgnoreCase("getItemsByUsersAndLocation")){
		        		int count = jsonObject.getInt("count");
		        		if(count > 0){
		        			Log.d(DatabaseRelatedBackGround.class.getName(), "Count:" + count);
		        			showNotification(jsonObject.getString("location"));
		        		}
		        	}
		        }
		        catch(JSONException e){
		        	Log.e("Database Returned4", "Error parsing data "+e.toString());
				}        
		    }
		}
	    
	}
}