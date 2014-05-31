package com.csciDIW.threeprimeevils.Gaming.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.csciDIW.threeprimeevils.Gaming.client.GamingService;
import com.csciDIW.threeprimeevils.Gaming.shared.AmazonData;
import com.csciDIW.threeprimeevils.Gaming.shared.BestBuyData;
import com.csciDIW.threeprimeevils.Gaming.shared.BestBuyReview;
import com.csciDIW.threeprimeevils.Gaming.shared.DiggNews;
import com.csciDIW.threeprimeevils.Gaming.shared.GiantBombGame;
import com.csciDIW.threeprimeevils.Gaming.shared.LoginInfo;
import com.csciDIW.threeprimeevils.Gaming.shared.ServerData;
import com.csciDIW.threeprimeevils.Gaming.shared.UserComments;
import com.csciDIW.threeprimeevils.Gaming.shared.UserDeals;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */

public class GamingServiceImpl extends RemoteServiceServlet implements
		GamingService {

	private static final long serialVersionUID = 1L;
	
	public ServerData getData(String game) {
		try{
			GameInfo gameInfo = getGameInfo(game);
			GiantBombGame giantBombGame =  getGiantBombData(gameInfo.getGiantBombId());
			BestBuyData bestBuyData = getBestBuyData(gameInfo.getBestBuyId());
			ServerData serverData = new ServerData(giantBombGame, bestBuyData);
			return serverData;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private GameInfo getGameInfo(String game){
		String gameName = game.split("-")[0].trim();
		String gamePlatform = game.split("-")[1].trim();
		return getGameInfoFromDataStore(gameName, gamePlatform);
	}
	
	@SuppressWarnings("unchecked")
	private GameInfo getGameInfoFromDataStore(String gameName, String gamePlatform){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GameInfo gameInfo;
		try{
			Query query = pm.newQuery(GameInfo.class);
			query.setFilter("name == nameParam && platform == platformParam");
			query.declareParameters("String nameParam, String platformParam");
			List<GameInfo> gameInfoList = (List<GameInfo>)query.execute(gameName, gamePlatform);
			System.out.println("Size:" + gameInfoList.size());
			if(gameInfoList.size() != 1){
				System.err.println("No or more than one value returned");
				gameInfo = null;
			}else{
				gameInfo = gameInfoList.get(0);
			}
		}finally{
			pm.close();
		}
		return gameInfo;
	}
	
	public GiantBombGame[] getSimilarGames(String game){
		try{
			final String apiUrl = "http://api.giantbomb.com";
			final String apiKey = "7b59ab667824050816d122b91e3976ee29ad28e0";
			String filter = "field_list=similar_games";
			long id = getGameInfo(game).getGiantBombId();
			String similarGameUrl = apiUrl + "/game/" + id + "/?api_key=" + apiKey + "&format=json&" + filter ;
			
			String similarGameString = getJSONData(similarGameUrl);
			JSONArray similarGamesJSON = new JSONObject(similarGameString).getJSONObject("results").getJSONArray("similar_games");
			GiantBombGame[] similarGames = new GiantBombGame[similarGamesJSON.length()]; 
			for(int i=0; i<similarGamesJSON.length(); i++){
				int gameId = similarGamesJSON.getJSONObject(i).getInt("id");
				similarGames[i] = getGiantBombData(gameId);
			}
			return similarGames;
		}catch(JSONException e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	public DiggNews[] getDiggNews(String gameName, String sortOrder){
		try{
			if(sortOrder.equalsIgnoreCase("diggs"))
				sortOrder = "digg_count-desc";
			else
				sortOrder = "submit_date-desc";
			final String apiUrl = "http://services.digg.com/2.0/search.search";
			String query = "?topic=gaming&sort=" + sortOrder + "&query=" + gameName;
			String diggUrl = apiUrl + query;
			String json = getJSONData(diggUrl);
			JSONArray diggNewsResult = new JSONObject(json).getJSONArray("stories");
			DiggNews[] diggNewsArray = new DiggNews[diggNewsResult.length()];
			System.out.println("Digg news size:" + diggNewsResult.length());
			for(int i=0; i<diggNewsResult.length(); i++){
				JSONObject diggNews = diggNewsResult.getJSONObject(i);
				int diggs = diggNews.getInt("diggs");
				String title = diggNews.getString("title");
				Date date = new Date((long)diggNews.getInt("submit_date")*1000);
				String image = diggNews.getJSONObject("thumbnail").getString("src");
				String url = diggNews.getString("link");
				String description = diggNews.getString("description");
				diggNewsArray[i] = new DiggNews(diggs, title, date, image, url, description);
			}
			return diggNewsArray;
			
			
		}catch(JSONException e){
			e.printStackTrace();
		}
		return null;
	}

	public AmazonData getAmazonData(String gameName) {
		String ITEM_ID = getGameInfo(gameName).getAmazonId();
		try{
			final String AWS_ACCESS_KEY_ID = "AKIAJBCQ34CPT5AD5QVA";
		   	final String AWS_SECRET_KEY = "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";
	    	final String ENDPOINT = "ecs.amazonaws.com";
					
	        SignedRequestsHelper helper;
	        
	    	helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
			
	        String requestUrl = null;
	
	
	        Map<String, String> params = new HashMap<String, String>();
	        params.put("Service", "AWSECommerceService");
	        params.put("Version", "2010-11-01");
	        params.put("Operation", "ItemLookup");
	        params.put("ItemId", ITEM_ID);
	        params.put("ResponseGroup", "Small,OfferFull,Similarities,Reviews,EditorialReview");
	
	        requestUrl = helper.sign(params);
	        //System.out.println("Signed Request is \"" + requestUrl + "\"");
	        
	    	String itemTitle = null;
	    	String itemAsin = null;
	        String itemLowestNewPrice = null;
	        String itemLowestUsedPrice = null;
	    	String itemLowestCollectiblePrice = null;
	    	String itemMerchantId = null;
	    	String itemMerchantName = null;
	    	String itemMerchantGlancePage = null;
	    	String itemIFrameURL = null;
	    	String itemPageURL = null;
	    	String itemeditorialReview = null;
	
		    String[] itemSimilarityASIN = new String[5];
		    String[] itemSimilarityTitle = new String[5];
	
	            
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document doc = db.parse(requestUrl);
	
	        /* Extracting the title of the Item */
	        Node titleNode = doc.getElementsByTagName("Title").item(0);
	        itemTitle = titleNode.getTextContent();
	
	        /* Extracting the Amazon Item Number of the Item */
	        Node asinNode = doc.getElementsByTagName("ASIN").item(0);
	        itemAsin = asinNode.getTextContent();
	
	        /* Extracting the Amazon Detailed page URL of the Item */
	        Node pageURLNode = doc.getElementsByTagName("DetailPageURL").item(0);
	        itemPageURL = pageURLNode.getTextContent();
	
	        /* Extracting the Amazon Editorial Review of the Item */
	        Node eReviewNode = doc.getElementsByTagName("Content").item(0);
	        itemeditorialReview = eReviewNode.getTextContent();
	
	        /* Extracting the customer review URL of the Item */
	        Node iFrameURLNode = doc.getElementsByTagName("IFrameURL").item(0);
	        itemIFrameURL = iFrameURLNode.getTextContent();  
	
	        /* Extracting the Lowest New Price of the Item */
	        Node newPriceNode = doc.getElementsByTagName("FormattedPrice").item(0);
	        itemLowestNewPrice = newPriceNode.getTextContent();  
	
	        /* Extracting the Lowest Used Price of the Item */
	        NodeList nodeLst = doc.getElementsByTagName("LowestUsedPrice");
	        for (int s = 0; s < nodeLst.getLength(); s++) {
	          Node fstNode = nodeLst.item(s);       
	          if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
	            Element fstElmnt = (Element) fstNode;
	            NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("FormattedPrice");
	            Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
	            NodeList fstNm = fstNmElmnt.getChildNodes();
	            itemLowestUsedPrice = ((Node) fstNm.item(0)).getNodeValue();
	          }
	        }
	        
	        /* Extracting the Lowest Collectible Price of the Item */
	        NodeList lcpnodeLst = doc.getElementsByTagName("LowestCollectiblePrice");
	        for (int s = 0; s < lcpnodeLst.getLength(); s++) {
	          Node fstNode = lcpnodeLst.item(s);       
	          if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
	            Element fstElmnt = (Element) fstNode;
	            NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("FormattedPrice");
	            Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
	            NodeList fstNm = fstNmElmnt.getChildNodes();
	            itemLowestCollectiblePrice = ((Node) fstNm.item(0)).getNodeValue();
	          }
	        }
	        
	        /* Extracting the Merchant Details of the Item's new price  */
	        NodeList mnodeLst = doc.getElementsByTagName("Merchant");
	        for (int s = 0; s < mnodeLst.getLength(); s++) {
	          Node fstNode = mnodeLst.item(s);       
	          if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
	            Element fstElmnt = (Element) fstNode;
	            NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("MerchantId");
	            Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
	            NodeList fstNm = fstNmElmnt.getChildNodes();
	            itemMerchantId = ((Node) fstNm.item(0)).getNodeValue();
	
	            NodeList secNmElmntLst = fstElmnt.getElementsByTagName("Name");
	            Element secNmElmnt = (Element) secNmElmntLst.item(0);
	            NodeList secNm = secNmElmnt.getChildNodes();
	            itemMerchantName = ((Node) secNm.item(0)).getNodeValue();
	
	            NodeList trdNmElmntLst = fstElmnt.getElementsByTagName("GlancePage");
	            Element trdNmElmnt = (Element) trdNmElmntLst.item(0);
	            NodeList trdNm = trdNmElmnt.getChildNodes();
	            itemMerchantGlancePage= ((Node) trdNm.item(0)).getNodeValue();
	          }
	        }
	        
	        /* Extracting the Similar products details of the Item  */
	        NodeList snodeLst = doc.getElementsByTagName("SimilarProduct");
	        for (int s = 0; s < snodeLst.getLength(); s++) {
	          Node fstNode = snodeLst.item(s);       
	          if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
	            Element fstElmnt = (Element) fstNode;
	            
	            NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("ASIN");
	            Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
	            NodeList fstNm = fstNmElmnt.getChildNodes();
	            itemSimilarityASIN[s] = ((Node) fstNm.item(0)).getNodeValue();
	
	            NodeList secNmElmntLst = fstElmnt.getElementsByTagName("Title");
	            Element secNmElmnt = (Element) secNmElmntLst.item(0);
	            NodeList secNm = secNmElmnt.getChildNodes();
	            itemSimilarityTitle[s] = ((Node) secNm.item(0)).getNodeValue();
	          }
	        }
	        
	        AmazonData amazonData = new AmazonData(	itemTitle,
													itemAsin,
													itemLowestNewPrice,
													itemLowestUsedPrice,
													itemLowestCollectiblePrice,
													itemMerchantId,
													itemMerchantName,
													itemMerchantGlancePage,
													itemIFrameURL,
													itemPageURL,
													itemeditorialReview,
													itemSimilarityASIN,
													itemSimilarityTitle
													) ;
	        //amazonData.printAmazonData();
	        return amazonData;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
        
    }	

	private GiantBombGame getGiantBombData(long id){
		try{
			final String apiUrl = "http://api.giantbomb.com";
			final String apiKey = "7b59ab667824050816d122b91e3976ee29ad28e0";
			String filter = "field_list=genres,id,image,images,name,original_release_date,deck,description";
			String gameInfoUrl = apiUrl + "/game/" + id + "/?api_key=" + apiKey + "&format=json&" + filter ;
			String json = getJSONData(gameInfoUrl);
			JSONObject giantBombResult = new JSONObject(json).getJSONObject("results");
			
			String deck = giantBombResult.getString("deck");
			String description = giantBombResult.getString("description");
			int gameId = giantBombResult.getInt("id");
			String releaseDateString = giantBombResult.getString("original_release_date");
			Date releaseDate = null;
			if(releaseDateString != null)
				releaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(releaseDateString);
			String image = giantBombResult.getJSONObject("image").getString("thumb_url");
			
			String smallImage = giantBombResult.getJSONObject("image").getString("icon_url");
			
			JSONArray imagesJSON = giantBombResult.getJSONArray("images");
			String[] images = new String[imagesJSON.length()];
			for(int i=0; i<imagesJSON.length(); i++){
				JSONObject imageJSON = imagesJSON.getJSONObject(i);
				images[i] = imageJSON.getString("thumb_url");
			}
			
			String name = giantBombResult.getString("name");
			
			JSONArray genresJSON = giantBombResult.getJSONArray("genres");
			String[] genres = new String[genresJSON.length()];
			for(int i=0; i<genresJSON.length(); i++){
				JSONObject genreJSON = genresJSON.getJSONObject(i);
				genres[i] = genreJSON.getString("name");
			}
			
			GiantBombGame giantBombGame = new GiantBombGame(deck, description, genres, image, smallImage, images, gameId, name, releaseDate);
			return giantBombGame;
			
		}catch(JSONException e){
			e.printStackTrace();
		
		}catch(ParseException e){
			e.printStackTrace();
		}
		
		return null;
	}

	private BestBuyData getBestBuyData(int skuId) {
		try{
			final String apiKey = "d6scjvmpupf82t7drswp885s";
			String productUrl = "http://api.remix.bestbuy.com/v1/products(sku=" + skuId + ")?apiKey=" + apiKey + "&show=name,regularPrice,url&format=json";
			String reviewUrl = "http://api.remix.bestbuy.com/v1/reviews(sku=" + skuId + ")?apiKey="  + apiKey + "&format=json";
			
			String productString = getJSONData(productUrl);
			String reviewString = getJSONData(reviewUrl);
			
			JSONObject product = new JSONObject(productString).getJSONArray("products").getJSONObject(0);
			String name = product.getString("name");
			double regularPrice = product.getDouble("regularPrice");
			String url = product.getString("url");
			
			JSONArray reviews = new JSONObject(reviewString).getJSONArray("reviews");
			BestBuyReview[] bestBuyReview = new BestBuyReview[reviews.length()];
			for(int i=0; i< reviews.length(); i++){
				JSONObject review = reviews.getJSONObject(i);
				int reviewId = review.getInt("id");
				int sku = review.getInt("sku");
				String reviewer = review.getJSONArray("reviewer").getJSONObject(0).getString("name");
				double rating = review.getDouble("rating");
				String title = review.getString("title");
				String comment = review.getString("comment");
				Date submission = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(review.getString("submissionTime"));
				
				bestBuyReview[i] = new BestBuyReview(reviewId, sku, reviewer, rating, title, comment, submission);
			}
			
			return new BestBuyData(name, regularPrice, url, skuId, bestBuyReview);
		
		}catch(JSONException e){
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

	private String getJSONData(String apiUrl) {
		StringBuilder json = new StringBuilder();
		try{
			URL url = new URL(apiUrl.replaceAll(" ", "%20"));
			URLConnection urlc = url.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                urlc.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null){ 
	            json.append(inputLine.trim());
	        }
	        in.close();
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return json.toString();
	}
	
	@SuppressWarnings("unchecked")
	private void printGameInfoDataStore(){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "SELECT FROM " + GameInfo.class.getName();
        System.out.println(query);
        List<GameInfo> gameInfoList = (List<GameInfo>) pm.newQuery(query).execute();
        System.out.println(gameInfoList.size());
        for(int i=0; i< gameInfoList.size(); i++){
        	System.out.println(gameInfoList.get(i).getName() + " : " + gameInfoList.get(i).getPlatform() + " : " + gameInfoList.get(i).getAmazonId());
        }
	}
	
	public Boolean insertInitialData() {
        GameInfo[] initialGames = new GameInfo[20];
        initialGames[0] = new GameInfo("FIFA Soccer 11", "PlayStation 3", 1011252, "B003KZJA9Y", 31651);
        initialGames[1] = new GameInfo("FIFA Soccer 11", "PlayStation 2", 1011446, "B003KZQSLW", 31651);
        initialGames[2] = new GameInfo("FIFA Soccer 11", "PSP", 1011243, "B003KZJ8BO", 31651);
        initialGames[3] = new GameInfo("FIFA Soccer 11", "Xbox 360", 1011491, "B003KZOXZ0", 31651);
        initialGames[4] = new GameInfo("Diablo II", "PC", 4307084, "B00002CF9M", 1119);
        initialGames[5] = new GameInfo("Diablo II: Lord of Destruction", "PC", 4307084, "B000098XJQ", 15589);
        initialGames[6] = new GameInfo("God of War III", "PlayStation 3", 9201106, "B000ZK9QCS", 20461);
        initialGames[7] = new GameInfo("Counter Strike: Source", "PC", 7453383, "B000AOJ7FK", 11339);
        initialGames[8] = new GameInfo("Halo: Reach", "Xbox 360", 9713872, "B002BSA20M", 26786);
        initialGames[9] = new GameInfo("Call of Duty: Black Ops", "Xbox 360", 9902347, "B003JVKHEQ", 26423);
        initialGames[10] = new GameInfo("Call of Duty: Black Ops", "PlayStation 3", 9902392, "B003JVCA9Q", 26423);
        initialGames[11] = new GameInfo("Guitar Hero II", "PlayStation 2", 118227, "B000I4JJRI", 14916);
        initialGames[12] = new GameInfo("WWE SmackDown vs. Raw 2011", "PlayStation 2", 1067205, "B003P9LECG", 31640);
        initialGames[13] = new GameInfo("Assassin's Creed Brotherhood", "PlayStation 3", 9984133, "B003L8DXOI", 31001);
        initialGames[14] = new GameInfo("Iron Man 2", "PlayStation 3", 9823714, "B002WDL516", 25247);
        initialGames[15] = new GameInfo("Need for Speed: Undercover", "PlayStation 3", 9092368, "B001AZFSEW", 20741);
        initialGames[16] = new GameInfo("Grand Theft Auto: Chinatown Wars", "PSP", 9563563, "B002F87WEW", 21100);
        initialGames[17] = new GameInfo("Prince of Persia: The Forgotten Sands", "PSP", 9832621, "B0030GBU2S", 29321);
        initialGames[18] = new GameInfo("Crysis 2", "PC", 1686167, "B002BS47YE", 26754);
        initialGames[19] = new GameInfo("Shift 2 Unleashed", "PC", 1814066, "B002LHSGSI", 32253);


        PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
		
		        for(int i=0; i< initialGames.length; i++)
		                pm.makePersistent(initialGames[i]);
		
		        printGameInfoDataStore();
		} finally {
		    pm.close();
		}

        return new Boolean(true);
}
	
	@SuppressWarnings("unchecked")
	public UserComments[] getComments(String gameNameInput){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			Query query = pm.newQuery(UserCommentsDataStore.class);
			query.setFilter("gameName == nameParam");
			query.declareParameters("String nameParam");
			List<UserCommentsDataStore> userCommentsDataStoreList = (List<UserCommentsDataStore>)query.execute(gameNameInput);
			UserComments[] userComments = new UserComments[userCommentsDataStoreList.size()];
			for(int i=0; i<userCommentsDataStoreList.size(); i++){
				String userName = userCommentsDataStoreList.get(i).getUserName();
				String userEmail = userCommentsDataStoreList.get(i).getUserEmail();
				int id = userCommentsDataStoreList.get(i).getId();
				String comment = userCommentsDataStoreList.get(i).getComment();
				String gameName = userCommentsDataStoreList.get(i).getGameName();
				Date date = userCommentsDataStoreList.get(i).getDate();
				userComments[i] = new UserComments(userName, userEmail, id, gameName, comment, date);
			}
			return userComments;
		}finally{
			pm.close();
		}
	}	
	
	public Boolean addComment(String userName, String userEmail, int id, String gameName, String comment){
		UserCommentsDataStore userCommentsDataStore = new UserCommentsDataStore(userName, userEmail, id, gameName, comment, new Date());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			pm.makePersistent(userCommentsDataStore);
			//UserComments[] userComments = getUserComments("FIFA Soccer 11", "PlayStation 3"); 
		}finally{
			pm.close();
		}
		return new Boolean(true);
	}
	
	@SuppressWarnings("unchecked")
	public UserDeals[] getDeals(String gameNameInput){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			Query query = pm.newQuery(UserDealsDataStore.class);
			query.setFilter("gameName == nameParam");
			query.declareParameters("String nameParam");
			List<UserDealsDataStore> userDealsDataStoreList = (List<UserDealsDataStore>)query.execute(gameNameInput);
			UserDeals[] userDeals = new UserDeals[userDealsDataStoreList.size()];
			System.out.println("UserDeals:" + userDealsDataStoreList.size());
			for(int i=0; i<userDealsDataStoreList.size(); i++){
				String userName = userDealsDataStoreList.get(i).getUserName();
				String userEmail = userDealsDataStoreList.get(i).getUserEmail();
				int id = userDealsDataStoreList.get(i).getId();
				String url = userDealsDataStoreList.get(i).getUrl();
				double price = userDealsDataStoreList.get(i).getPrice();
				String gameName = userDealsDataStoreList.get(i).getGameName();
				Date date = userDealsDataStoreList.get(i).getDate();
				userDeals[i] = new UserDeals(userName, userEmail, id, gameName, url, price, date);
			}
			return userDeals;
		}finally{
			pm.close();
		}
	}
	
	public Boolean addDeal(String userName, String userEmail, int id, String gameName, String url, double price){
		UserDealsDataStore userDealsDataStore = new UserDealsDataStore(userName, userEmail, id, gameName, url, price, new Date());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			pm.makePersistent(userDealsDataStore);
		}finally{
			pm.close();
		}
		return new Boolean(true);
	}
	
	@SuppressWarnings("unchecked")
	public String[] getGameNames(){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			Query query = pm.newQuery(GameInfo.class);
			List<GameInfo> gameInfoList = (List<GameInfo>)query.execute();
			String[] gameNames = new String[gameInfoList.size()];
			for(int i=0; i<gameInfoList.size(); i++)
				gameNames[i] = gameInfoList.get(i).getName() + " - " + gameInfoList.get(i).getPlatform();
			return gameNames;
		}finally{
			pm.close();
		}
	}
	
	public LoginInfo login(String requestUri) {
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    LoginInfo loginInfo = new LoginInfo();

	    if (user != null) {
	      loginInfo.setLoggedIn(true);
	      loginInfo.setEmailAddress(user.getEmail());
	      loginInfo.setNickname(user.getNickname());
	      loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
	    } else {
	      loginInfo.setLoggedIn(false);
	      loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
	    }
	    return loginInfo;
	  }
}