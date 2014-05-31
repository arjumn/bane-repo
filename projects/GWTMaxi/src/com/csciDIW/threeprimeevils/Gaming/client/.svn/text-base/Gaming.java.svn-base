package com.csciDIW.threeprimeevils.Gaming.client;

import com.csciDIW.threeprimeevils.Gaming.shared.AmazonData;
import com.csciDIW.threeprimeevils.Gaming.shared.BestBuyData;
import com.csciDIW.threeprimeevils.Gaming.shared.BestBuyReview;
import com.csciDIW.threeprimeevils.Gaming.shared.DiggNews;
import com.csciDIW.threeprimeevils.Gaming.shared.GiantBombGame;
import com.csciDIW.threeprimeevils.Gaming.shared.LoginInfo;
import com.csciDIW.threeprimeevils.Gaming.shared.ServerData;
import com.csciDIW.threeprimeevils.Gaming.shared.UserComments;
import com.csciDIW.threeprimeevils.Gaming.shared.UserDeals;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Gaming implements EntryPoint {
	
	private GamingServiceAsync gamingService = GWT.create(GamingService.class);
	private String gameName;
	private int accSelectedIndex;
	private int tabSelectedIndex;

	private HorizontalPanel shortInfoPanel;
	private HorizontalPanel contentPanel;
	private SimplePanel loadingPanel; 

	private StackPanel accordionPanel;
	private ScrollPanel accordionSubPanel1;
	private ScrollPanel accordionSubPanel2;
	private ScrollPanel accordionSubPanel3;
	private VerticalPanel accordionSubPanel4;
	
	private TabPanel tabPanel;
	private FlowPanel tabSubPanel1;
	private FlowPanel tabSubPanel2;
	private FlowPanel tabSubPanel3;
	
	private static LoginInfo loginInfo;
	private static String[] gameNames;
	
	public void onModuleLoad() {
		//insertInitialData();
		login();
	}
	
	private void loadPage() 
	{
		// Horizontal panel to add the text box and the button
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setStyleName("halfWidth");
		
		SimplePanel labelPanel = new SimplePanel();
		HorizontalPanel topPanel = new HorizontalPanel();
		
		Label label = new Label("Enter the game name you want to search");
		label.setWidth("850px");
		
		HTML signOut = new HTML("<a href=\"" + loginInfo.getLogoutUrl() + "\">Sign out</a>");
		
		topPanel.add(label);
		topPanel.add(signOut);
		
		labelPanel.add(topPanel);
		RootPanel.get("content").add(labelPanel);
		
		final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		final SuggestBox searchText = new SuggestBox(oracle);
		for(String gameName: gameNames){
			oracle.add(gameName);
		}
		
		//final TextBox searchText = new TextBox();
		searchText.setStyleName("searchText");
		//searchText.setText("FIFA Soccer 11 - PlayStation 3");
		
		Button searchButton = new Button("Get Info");
		searchButton.setStyleName("searchButton");
		
		// Create a handler for the sendButton and nameField
		class ButtonHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				if("".equals(searchText.getText()) && "".equals(searchText.getText().trim())){
					return;
				}
				if( !searchText.getText().equalsIgnoreCase(gameName) )
				{
					accSelectedIndex = 0;
					tabSelectedIndex = 0;
					clearPanels();
					
					loadingPanel = new SimplePanel();
					loadingPanel.add(new Image("http://imaps.indygov.org/publicsafetymaps/images/loading_blue.gif"));
					RootPanel.get("content").add(loadingPanel);
					
					gameName = searchText.getText().trim();
					//displayDialog("Deal");
					getServerData(gameName);
				}
			}
		}
		
		// add the handler to the button click
		ButtonHandler handler = new ButtonHandler();
		searchButton.addClickHandler(handler);

		buttonPanel.add(searchText);
		buttonPanel.add(searchButton);
		RootPanel.get("content").add(buttonPanel);
		
		/*************** Display the Game short description *****************************/
		shortInfoPanel = new HorizontalPanel();
		
		/*************** Game information accordion and tab panels *********************/ 
		//HorizontalPanel contentPanel = new HorizontalPanel();
		contentPanel = new HorizontalPanel();
		contentPanel.setStyleName("contentPanel");
		
		/****** Left hand side panel ******/
		// ACCORDION PANEL //
		accordionPanel = new StackPanel();
		accordionPanel.addStyleName("halfWidth halfHeight");
		
		/*** Game Information - 1st accordion panel ***/
		{
			accordionSubPanel1 = new ScrollPanel();
			accordionSubPanel1.setStyleName("halfWidth halfHeight");
			
			// add the game information to the accordion
			accordionPanel.add(accordionSubPanel1, "Game Information", false);
		}
	
		/*** Game News - 2nd accordion panel ***/
		{
			accordionSubPanel2 = new ScrollPanel();
			accordionSubPanel2.setStyleName("halfWidth halfHeight");
	
			// add the gaming news to the accordion
			accordionPanel.add(accordionSubPanel2, "Game News", false);
		}
		
		/*** Related Games - 3rd accordion panel ***/
		{
			accordionSubPanel3 = new ScrollPanel();
			accordionSubPanel3.setStyleName("halfWidth halfHeight");
	
			// add the related games to the accordion
			accordionPanel.add(accordionSubPanel3, "Related Games", false);
		}
		
		/*** User Comments - 4th accordion panel ***/
		{
			accordionSubPanel4 = new VerticalPanel();
			
			// add the related games to the accordion
			accordionPanel.add(accordionSubPanel4, "User Comments", false);
		}
		
        accordionPanel.addHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                if (accordionPanel.getSelectedIndex() != accSelectedIndex) {
                    accSelectedIndex = accordionPanel.getSelectedIndex();
                    
                    switch(accSelectedIndex)
                    {
                    case 0:
                    	break;
                    	
                    case 1:
                		accordionSubPanel2.add(new Image("http://www.duravit.com/duravit/file/all/ani_loader_big2.gif"));
                    	getDiggNews(gameName, "diggs");
                    	break;
                    	
                    case 2:
                    	accordionSubPanel3.add(new Image("http://www.duravit.com/duravit/file/all/ani_loader_big2.gif"));
                    	getSimilarGames(gameName);
                    	break;
                    	
                    case 3:
                    	accordionSubPanel4.add(new Image("http://www.duravit.com/duravit/file/all/ani_loader_big2.gif"));
                    	getUserComments(gameName);
                    	break;                    	
                    }
                }
            }
        },ClickEvent.getType());
  
		contentPanel.add(accordionPanel);
		
		
		/****** Right hand side panel ******/
	    // Create a new tabbed Panel
		tabPanel = new TabPanel();
		tabPanel.setStyleName("halfWidth halfHeight tabPanel");
		
		// Best Buy Panel
		{
			tabSubPanel1 = new FlowPanel();
			tabSubPanel1.setHeight("550px");
			tabPanel.add(tabSubPanel1, "BestBuy Info");
		}
	
		// Amazon Panel
		{
			tabSubPanel2 = new FlowPanel();
			tabSubPanel2.setHeight("550px");
			tabPanel.add(tabSubPanel2, "Amazon Info");
		}
	
		// User submitted deals
		{
			tabSubPanel3 = new FlowPanel();
			tabSubPanel3.setHeight("550px");
			tabPanel.add(tabSubPanel3, "User Submitted Deals");
		}

        tabPanel.addSelectionHandler(new SelectionHandler<Integer>(){ 
            public void onSelection(SelectionEvent<Integer> event) {
                if (event.getSelectedItem() != tabSelectedIndex) {
                	tabSelectedIndex = event.getSelectedItem();
                    
                    switch(tabSelectedIndex)
                    {
                    case 0:
                    	break;
                    	
                    case 1:
                    	tabSubPanel2.add(new Image("http://www.duravit.com/duravit/file/all/ani_loader_big2.gif"));
                    	getAmazonData(gameName);
                    	break;
                    	
                    case 2:
                    	tabSubPanel3.add(new Image("http://www.duravit.com/duravit/file/all/ani_loader_big2.gif"));
                    	getUserDeals(gameName);
                    	break;
                    }
                }
          }});

        // add the tabbed panel to the horizontal panel
		contentPanel.add(tabPanel);
	}
	
	private void login()
	{
        try {
            if(gamingService == null)
                    gamingService = GWT.create(GamingService.class);
            AsyncCallback<LoginInfo> callback = new AsyncCallback<LoginInfo>(){
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess(LoginInfo loginInfo) {
                            try{
                            	if(loginInfo.isLoggedIn()) {
                            		Gaming.loginInfo = loginInfo;
                            		getGameNames();
                            	} else {
                                    loadLogin(loginInfo);
                                }
                            }catch(Exception e){
                            }
                    }
            };
            gamingService.login(GWT.getHostPageBaseURL(), callback);
        }catch(Exception e){
        }
	}
	
	private void loadLogin(LoginInfo loginInfo) {
		Anchor loginLink = new Anchor("Click here to login to the page using Google Accounts", loginInfo.getLoginUrl());
	    RootPanel.get("content").add(loginLink);
	}
	
	private void getGameNames()
	{
        try {
            if(gamingService == null)
                    gamingService = GWT.create(GamingService.class);
            AsyncCallback<String[]> callback = new AsyncCallback<String[]>(){
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess(String[] gameNames) {
                            try{
                            	Gaming.gameNames = gameNames;
                            	loadPage();
                            }catch(Exception e){
                            }
                    }
            };
            gamingService.getGameNames(callback);
        }catch(Exception e){
        }
	}

	private void getServerData(String gameName) {
		if(gamingService == null)
			gamingService = GWT.create(GamingService.class);
		AsyncCallback<ServerData> callback = new AsyncCallback<ServerData>(){
			public void onFailure(Throwable caught) {
				displayErrorMessage(loadingPanel, "There was an error in retreiving game information");
			}

			public void onSuccess(ServerData gameData) {
				try{
					if( gameData == null ){
						displayErrorMessage(loadingPanel, "No gaming data corresponding to the search query");
					}
	
					else
					{
						loadingPanel.clear();
						
						GiantBombGame giantBombData = gameData.getGiantBombGame();
						BestBuyData bestBuyData = gameData.getBestBuyData();
		
						displayGameShortDescription(giantBombData);
						displayGameDescription(giantBombData.getDescription());
						displayBestBuyData(bestBuyData);
					}
				}catch(Exception e){
					displayErrorMessage(loadingPanel, "No gaming data corresponding to the search query");
				}
			}
		};
		gamingService.getData(gameName, callback);
	}

	private void getDiggNews(String gameName, String sortOrder){
		//sortOrder = date or diggs
		if(gamingService == null)
			gamingService = GWT.create(GamingService.class);
		AsyncCallback<DiggNews[]> callback = new AsyncCallback<DiggNews[]>(){
			public void onFailure(Throwable caught) {
				displayErrorMessage(accordionSubPanel2, "There was an error in retreiving Digg Information");
			}

			public void onSuccess(DiggNews[] diggNews) {
				try{
					accordionSubPanel2.clear();
					displayDiggNewsData(diggNews);
				}catch(Exception e){
					displayErrorMessage(accordionSubPanel2, "There was an error in retreiving Digg Information");
				}
			}
		};
		gamingService.getDiggNews(gameName, sortOrder, callback);
	}

	private void getSimilarGames(String gameName){
		if(gamingService == null)
			gamingService = GWT.create(GamingService.class);
		AsyncCallback<GiantBombGame[]> callback = new AsyncCallback<GiantBombGame[]>(){
			public void onFailure(Throwable caught) {
				displayErrorMessage(accordionSubPanel3, "There was an error in retreiving similar games");
			}

			public void onSuccess(GiantBombGame[] similarGames) {
				try{
					accordionSubPanel3.clear();
					displayRelatedGames(similarGames);
				}catch(Exception e){
					displayErrorMessage(accordionSubPanel3, "There was an error in retreiving similar games");
				}
			}
		};
		gamingService.getSimilarGames(gameName, callback);
	}

	
	private void getUserComments(String gameName){
		if(gamingService == null)
		gamingService = GWT.create(GamingService.class);
		AsyncCallback<UserComments[]> callback = new AsyncCallback<UserComments[]>(){
			public void onFailure(Throwable caught) {
				displayErrorMessage(accordionSubPanel4, "There was an error in retreiving user comments");
			}
	
			public void onSuccess(UserComments[] userComments) {
				try{
					accordionSubPanel3.clear();
					displayUserComments(userComments);
				}catch(Exception e){
					displayErrorMessage(accordionSubPanel4, "There was an error in retreiving user comments");
				}
			}
		};
		gamingService.getComments(gameName, callback);
	}
		
	private void getAmazonData(String gameName){
		if(gamingService == null)
			gamingService = GWT.create(GamingService.class);
		AsyncCallback<AmazonData> callback = new AsyncCallback<AmazonData>(){
			public void onFailure(Throwable caught) {
				displayErrorMessage(tabSubPanel2, "There was an error in retreiving Amazon data");
			}

			public void onSuccess(AmazonData amazonData) {
				try{
					tabSubPanel2.clear();
					displayAmazonData(amazonData);
				}catch(Exception e){
					displayErrorMessage(tabSubPanel2, "There was an error in retreiving Amazon data");
				}
			}
		};
		gamingService.getAmazonData(gameName, callback);
	}

	private void getUserDeals(String gameName){
		if(gamingService == null)
		gamingService = GWT.create(GamingService.class);
		AsyncCallback<UserDeals[]> callback = new AsyncCallback<UserDeals[]>(){
			public void onFailure(Throwable caught) {
				displayErrorMessage(tabSubPanel3, "There was an error in retreiving user deals information");
			}
	
			public void onSuccess(UserDeals[] userdeals) {
				try{
					tabSubPanel3.clear();
					displayUserDeals(userdeals);
				}catch(Exception e){
					displayErrorMessage(tabSubPanel3, "There was an error in retreiving user deals information");
				}
				
			}
		};
		gamingService.getDeals(gameName, callback);
	}
	
	
	private void insertInitialData() {
		try{
			if(gamingService == null)
				gamingService = GWT.create(GamingService.class);
			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
				public void onFailure(Throwable caught) {
				}
	
				public void onSuccess(Boolean isSuccess) {
					try{
						System.out.println("Success in inserting the value?:" + isSuccess);
					}catch(Exception e){
					}
				}
			};
			gamingService.insertInitialData(callback);
		}catch(Exception e){
		}
	}
	
	private void addComment(String userName, String userEmail, int id, final String gameName, String comment){
		try{
			if(gamingService == null)
				gamingService = GWT.create(GamingService.class);
			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
				public void onFailure(Throwable caught) {
				}
	
				public void onSuccess(Boolean isSuccess) {
					try{
						getUserComments(gameName);
					}catch(Exception e){
					}
				}
			};
			gamingService.addComment(userName, userEmail, id, gameName, comment, callback);
			
		}catch(Exception e){
		}
	}

	private void addDeal(String userName, String userEmail, int id, final String gameName, String url, double price){
		try{
			if(gamingService == null)
				gamingService = GWT.create(GamingService.class);
			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
				public void onFailure(Throwable caught) {
				}
	
				public void onSuccess(Boolean isSuccess) {
					try{
						getUserDeals(gameName);
					}catch(Exception e){
					}
				}
			};
			gamingService.addDeal(userName, userEmail, id, gameName, url, price, callback);
			
		}catch(Exception e){
		}
	}
	
	
	private void displayDialog(final String dialogType)
	{		
		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Add a " + dialogType);

		final TextBox urlTextBox = new TextBox();
		urlTextBox.setStyleName("popupTextBox");

		final TextBox priceTextBox = new TextBox();
		priceTextBox.setWidth("50px");

		final TextArea textArea = new TextArea();
		textArea.setStyleName("popupTextArea");

		
		//dialogBox.setAnimationEnabled(true);
		final Button submitButton = new Button("Submit " + dialogType);
		submitButton.setStyleName("popupPanelSubmitButton");
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if( dialogType.equals("Deal")){
					addDeal(loginInfo.getNickname(), loginInfo.getEmailAddress(), 1, gameName, urlTextBox.getText(), Double.parseDouble(priceTextBox.getText()));
				}else{
					addComment(loginInfo.getNickname(), loginInfo.getEmailAddress(), 1, gameName, textArea.getText());
				}
				dialogBox.hide();
			}
		});

		final Button closeButton = new Button("Close");
		closeButton.setStyleName("popupPanelCancelButton");
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

		
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setStyleName("halfWidth popupVerticalPanel");
		
		if( dialogType == "Deal" )
		{
			Label label1 = new Label("Have a good deal? Please let others know...");
			vPanel.add(label1);
			vPanel.add(urlTextBox);
			
			Label label2 = new Label("Price");
			vPanel.add(label2);
			vPanel.add(priceTextBox);
		}
		else
		{
			Label label = new Label("Please add you comments about the game");
			vPanel.add(label);
			vPanel.add(textArea);
		}
		
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setStyleName("popupHorizontalPanel");
		
		hPanel.add(submitButton);
		hPanel.add(closeButton);
		
		vPanel.add(hPanel);
		dialogBox.add(vPanel);
		dialogBox.center();
	}

	// Display the Game short description
	private void displayGameShortDescription(GiantBombGame giantBombData)
	{
		if(shortInfoPanel != null)
		{
			shortInfoPanel.clear();
			shortInfoPanel.setVisible(true);
		}
		
		// left hand side of the game short description
		{
			// create a simple Panel to hold the data and the image			
			SimplePanel lGameShortDescriptionSimplePanel = new SimplePanel();
			lGameShortDescriptionSimplePanel.setStyleName("halfWidth lGameShortDescriptionSimplePanel block");
			
			HorizontalPanel lgameShortDescHorizontalPanel = new HorizontalPanel();
			lgameShortDescHorizontalPanel.setStyleName("halfWidth lgameShortDescHorizontalPanel");
			
			String[] genresList = giantBombData.getGenres();
			String genres = "";
			for(int i = 0; i < genresList.length; i++)
			{
				genres +=  genresList[i];
				if(i != genresList.length - 1)
					genres += ',';
			}
			
			String deck = giantBombData.getDeck();
			if(deck.length() > 150)
			{
				deck = deck.substring(0, 150);
				deck += "...";
			}
			
			HTML gameInfo = new HTML("<h3 class=\"black underline\">" + gameName + "</h3>" + 
									 "<div><strong>Release Date: </strong>" + giantBombData.getReleaseDate().toString() + "<br/>" +
									 	  "<strong>Genre: </strong>" + genres + "<br/>" +
									 	  "<strong>Description: </strong>" + deck + "</div>");
			
			
			gameInfo.setStyleName("gameName");
			
			Image gameImage = new Image(giantBombData.getImage());
			gameImage.setStyleName("gameImage");
			
			lgameShortDescHorizontalPanel.add(gameInfo);
			lgameShortDescHorizontalPanel.add(gameImage);

			lGameShortDescriptionSimplePanel.add(lgameShortDescHorizontalPanel);

			//lGameShortDescriptionHorizontalPanel.add(lGameShortDescriptionSimplePanel);
			shortInfoPanel.add(lGameShortDescriptionSimplePanel);
		}
		
		// game play images
		{
			ScrollPanel scPanel = new ScrollPanel();
			scPanel.setStyleName("halfWidth rHorizontalScrollingImagesPanel block");
			
			HorizontalPanel hPanel = new HorizontalPanel();
			
			String[] gameplayImages = giantBombData.getGameImages();
			for(int i = 0; i < gameplayImages.length; i++)
			{
				Image image = new Image(gameplayImages[i]);
				image.setStyleName("gameplayImage");
				hPanel.add(image);
			}
			
			scPanel.add(hPanel);
			
			//lGameShortDescriptionHorizontalPanel.add(scPanel);
			shortInfoPanel.add(scPanel);
		}
		contentPanel.setVisible(true);
		RootPanel.get("content").add(shortInfoPanel);
	}
	
	// Display the Game short description
	private void displayGameDescription(String description)
	{
		accordionSubPanel1.clear();
		
		// set up a scrolling panel in the accordion which contains a vertical panel inside it
		HTML gameDescHTML = new HTML(description);
		gameDescHTML.addStyleName("textHTML");
		
		// add the HTML description widget to the scrolling panel
		accordionSubPanel1.add(gameDescHTML);

		accordionPanel.showStack(0);
		// Add the accordion panel to the root panel
		RootPanel.get("content").add(contentPanel);
	}
	
	// Display game news data
	private void displayDiggNewsData(DiggNews[] dgNews)
	{
		accordionSubPanel2.clear();

		// The vertical panel to insert the game news data one by one
		VerticalPanel lVerticalPanel = new VerticalPanel();
		lVerticalPanel.setStyleName("lVerticalPanel");
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setStyleName("textHTML");

		HTML diggs = new HTML("<h4>Diggs</h4>");
		diggs.setWidth("50px");
		diggs.setStyleName("underline");
		
		HTML news = new HTML("<h4>News</h4>");
		news.setStyleName("underline");
		hPanel.add(diggs);
		hPanel.add(news);

		lVerticalPanel.add(hPanel);

		for(int i = 0; i < dgNews.length; i++)
		{
			hPanel = new HorizontalPanel();
			hPanel.setStyleName("textHTML");
 
			diggs = new HTML("<h5>" + dgNews[i].getDiggs() + "</h5>");
			diggs.setWidth("50px");

			String desc = dgNews[i].getDescription();
			if(desc.length() > 200)
			{
				desc = desc.substring(0, 200);
				desc += "...";
			}
			
			news = new HTML("<h6><a href=" + dgNews[i].getUrl() + " target=\"_blank\">" + dgNews[i].getTitle() + "</a></h6>" + desc);
			
			hPanel.add(diggs);
			hPanel.add(news);
			
			lVerticalPanel.add(hPanel);
		}
		
		// add the vertical panel to the Scrolling panel
		accordionSubPanel2.add(lVerticalPanel);
	}
		
	// Display related games data
	private void displayRelatedGames(GiantBombGame[] gbData)
	{
		accordionSubPanel3.clear();
		
		// The vertical panel to insert the game news data one by one
		VerticalPanel lVerticalPanel = new VerticalPanel();
		lVerticalPanel.setStyleName("lVerticalPanel");
		
		for(GiantBombGame gData:gbData)
		{
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setStyleName("textHTML");

			String[] genresList = gData.getGenres();
			String genres = "";
			for(int k = 0; k < genresList.length; k++)
			{
				genres +=  genresList[k];
				if(k != genresList.length - 1)
					genres += ',';
			}

			String date = "Not Available"; 
			if(gData.getReleaseDate() != null)
				date = gData.getReleaseDate().toString();
			
			HTML gameInfo = new HTML("<h5 class=\"black\">" + gData.getName() + "</h5><strong>Released Date: </strong>" + date + "<br/><strong>Genres: </strong>" + genres + "<br/>");
			gameInfo.setStyleName("relatedGameName");
			
			Image image = new Image(gData.getSmallImage());
			image.setStyleName("relatedGameImage");
			
			hPanel.add(gameInfo);
			hPanel.add(image);
			
			lVerticalPanel.add(hPanel);
		}

		// add the vertical panel to the Scrolling panel
		accordionSubPanel3.add(lVerticalPanel);
	}	
	
	
	// Display user comments
	private void displayUserComments(UserComments[] comments)
	{
		accordionSubPanel4.clear();
		
		ScrollPanel scLastAccordionPanel = new ScrollPanel();
		scLastAccordionPanel.setStyleName("scLastAccordionPanel");

		// The vertical panel to insert the related games data one by one
		VerticalPanel lVerticalPanel = new VerticalPanel();
		lVerticalPanel.setStyleName("lVerticalPanel");
		
		for(UserComments comment:comments)
		{
			HTML gameNewsHTML = new HTML("Posted by <span class=\"username\">" + comment.getUserName() + "</span> on " + comment.getDate().toString() + "<br/><br/>" + comment.getComment());
			gameNewsHTML.addStyleName("block textHTML");
			lVerticalPanel.add(gameNewsHTML);
		}

		// add the vertical panel to the Scrolling panel
		scLastAccordionPanel.add(lVerticalPanel);
		
		SimplePanel writeCommentPanel = new SimplePanel();
		writeCommentPanel.setStyleName("halfWidth");
		
		Anchor writeComment = new Anchor("Write a comment");
		writeComment.setStyleName("writeLink");
		  writeComment.addClickHandler(new ClickHandler(){
              public void onClick(ClickEvent event) {
                      displayDialog("Comment");
              }
		  });
		  
		writeCommentPanel.add(writeComment);
		
		// add the scroll panel and the simple panel to the main vertical panel
		accordionSubPanel4.add(scLastAccordionPanel);
		accordionSubPanel4.add(writeCommentPanel);
	}
	
	
	// Display Best Buy data
	private void displayBestBuyData(BestBuyData bestBuyData)
	{
		tabSubPanel1.clear();
		
		ScrollPanel scPanel = new ScrollPanel();
		scPanel.setStyleName("halfWidth");
		scPanel.setHeight("550px");

		// The vertical panel to insert the data
		VerticalPanel lVerticalPanel = new VerticalPanel();
		lVerticalPanel.setStyleName("lVerticalPanel");
		
		HorizontalPanel hPanel = new HorizontalPanel();
		HTML priceInfo = new HTML("<div class=\"textHTML\"><h4><a href=\"" + bestBuyData.getUrl() + "\" target=\"blank\">" + bestBuyData.getName() + "</a></h4>Price: <span class=\"username\">" + getPrecisionFormat(bestBuyData.getPrice()) + "</span></div><br/><h5>Reviews</h5>");
		priceInfo.setWidth("330px");
		
		Image bbImage = new Image("http://www.wikitude.org/wp-content/uploads/2010/01/bestbuy.png");
		bbImage.setStyleName("gameImage");
		
		hPanel.add(priceInfo);
		hPanel.add(bbImage);
		
		lVerticalPanel.add(hPanel);

		BestBuyReview[] reviews = bestBuyData.getReviews();
		for(BestBuyReview review: reviews)
		{
			HTML html = new HTML("<h5>Rating: " + review.getRating() + "</h5>" + review.getComment() + "<div align=\"right\">Review by <span class=\"username\">" + review.getReviewer() + "</span></div>");
			html.setStyleName("block textHTML");
			lVerticalPanel.add(html);
		}
		// add the vertical panel to the Scrolling panel
		scPanel.add(lVerticalPanel);
		
		tabSubPanel1.add(scPanel);
		tabPanel.selectTab(0);
	}
	
	
	private void displayAmazonData(AmazonData amazonData)
	{
		tabSubPanel2.clear();
		
		// The vertical panel to insert the related games data one by one
		VerticalPanel lVerticalPanel = new VerticalPanel();
		lVerticalPanel.setStyleName("halfWidth lVerticalPanel");
		
		HorizontalPanel hPanel = new HorizontalPanel();
		HTML priceInfo = new HTML("<div class=\"textHTML\"><h4><a href=\"" + amazonData.getitem_PageURL() + "\" target=\"_blank\">" + amazonData.getitem_Title() + "</a></h4>Price: <span class=\"username\">US" + amazonData.getitem_LowestNewPrice() + "</span></div>");
		priceInfo.setWidth("330px");
		
		Image bbImage = new Image("http://www.suburbanjournals.com/Amazon-small-logo.jpg");
		bbImage.setStyleName("gameImage");
		
		hPanel.add(priceInfo);
		hPanel.add(bbImage);
		
		lVerticalPanel.add(hPanel);

		HTML iframeUrl = new HTML("<iframe width=\"450px\" height=\"520px\" src=\"" + amazonData.getitem_IFrameURL() + "\"></iframe>");
		iframeUrl.setStyleName("iframe");
		
		lVerticalPanel.add(iframeUrl);
		
		tabSubPanel2.add(lVerticalPanel);
	}
	

	// Display User Deals
	private void displayUserDeals(UserDeals[] deals)
	{
		tabSubPanel3.clear();
		
		ScrollPanel scPanel = new ScrollPanel();
		scPanel.setStyleName("halfWidth");
		scPanel.setHeight("530px");

		// The vertical panel to insert the related games data one by one
		VerticalPanel lVerticalPanel = new VerticalPanel();
		lVerticalPanel.setStyleName("lVerticalPanel");
		
		for(UserDeals deal:deals)
		{
			HTML html = new HTML("<h5>Price: " + getPrecisionFormat(deal.getPrice()) + "</h5><a href=\"" + deal.getUrl() + "\" target=\"_blank\">" + deal.getUrl() + "</a><br/><div align=\"right\">Deal posted by <span class=\"username\">" + deal.getUserName() + "</span></div>");
			html.addStyleName("block textHTML");
			lVerticalPanel.add(html);
		}
		scPanel.add(lVerticalPanel);

		SimplePanel writeCommentPanel = new SimplePanel();
		writeCommentPanel.setStyleName("halfWidth");
		
		Anchor writeDeal = new Anchor("Write a Deal");
		writeDeal.setStyleName("writeLink");
		writeDeal.addClickHandler(new ClickHandler(){
              public void onClick(ClickEvent event) {
                      displayDialog("Deal");
              }
		  });
		
		writeCommentPanel.add(writeDeal);
		
		tabSubPanel3.add(scPanel);
		tabSubPanel3.add(writeCommentPanel);
	}
	
	private void clearPanels()
	{
		// set its visibility to false
		if(shortInfoPanel != null)
		{
			shortInfoPanel.setVisible(false);
			contentPanel.setVisible(false);
		}
		
		// clear the accordion panels
		accordionSubPanel1.clear();
		accordionSubPanel2.clear();
		accordionSubPanel3.clear();
		accordionSubPanel4.clear();
		
		// clear the tab panels
		tabSubPanel1.clear();
		tabSubPanel2.clear();
		tabSubPanel3.clear();		
	}

	private String getPrecisionFormat(double number)
	{
		NumberFormat numberFormat = NumberFormat.getCurrencyFormat();
		return numberFormat.format(number);
		//return Double.valueOf(df.format(number)).doubleValue();
	}	
	
	private void displayErrorMessage(Panel panel, String errorMsg)
	{
		panel.clear();
		panel.add(new HTML("<font size=\"5px\" color=\"red\">" + errorMsg + "</font>"));
	}
}











