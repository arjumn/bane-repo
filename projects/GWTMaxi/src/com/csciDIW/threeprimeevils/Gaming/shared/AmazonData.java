package com.csciDIW.threeprimeevils.Gaming.shared;

import java.io.Serializable;

public class AmazonData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String itemTitle;
	private String itemAsin;
    private String itemLowestNewPrice;
    private String itemLowestUsedPrice;
	private String itemLowestCollectiblePrice;
	private String itemMerchantId;
	private String itemMerchantName;
	private String itemMerchantGlancePage;
	private String itemIFrameURL;
	private String itemPageURL;
	private String itemeditorialReview;

	private String[] itemSimilarityASIN = new String[5];
	private String[] itemSimilarityTitle = new String[5];
	
	public AmazonData(){}
	
	public AmazonData(String itemTitle,
			  String itemAsin,
        	  String itemLowestNewPrice,
              String itemLowestUsedPrice,
			  String itemLowestCollectiblePrice,
			  String itemMerchantId,
			  String itemMerchantName,
			  String itemMerchantGlancePage,
			  String itemIFrameURL,
			  String itemPageURL,
			  String itemeditorialReview,
			  String[] itemSimilarityASIN,
			  String[] itemSimilarityTitle
				 ) {
		super();
		this.itemTitle = itemTitle;
		this.itemAsin = itemAsin;
        this.itemLowestNewPrice = itemLowestNewPrice;
        this.itemLowestUsedPrice = itemLowestUsedPrice;
		this.itemLowestCollectiblePrice = itemLowestCollectiblePrice;
		this.itemMerchantId = itemMerchantId;
		this.itemMerchantName = itemMerchantName;
		this.itemMerchantGlancePage = itemMerchantGlancePage;
		this.itemIFrameURL = itemIFrameURL;
		this.itemPageURL = itemPageURL;
		this.itemeditorialReview = itemeditorialReview;
		this.itemSimilarityASIN = itemSimilarityASIN;
		this.itemSimilarityTitle = itemSimilarityTitle;
	}
	
	public String getitem_Title() {
		return itemTitle;
	}
	
	public String item_Asin() {
		return itemAsin;
	}
	
	public String getitem_LowestNewPrice() {
		return itemLowestNewPrice;
	}
	
	public String getitem_LowestUsedPrice() {
		return itemLowestUsedPrice;
	}
	
	public String getitem_LowestCollectiblePrice() {
		return itemLowestCollectiblePrice;
	}
	
	public String getitem_MerchantId() {
		return itemMerchantId;
	}
	
	public String getitem_MerchantName() {
		return itemMerchantName;
	}
	
	public String getitem_MerchantGlancePage () {
		return itemMerchantGlancePage ;
	}
	
	public String getitem_IFrameURL() {
		return itemIFrameURL;
	}
	
	public String getitem_PageURL() {
		return itemPageURL;
	}
	
	public String getitem_editorialReview() {
		return itemeditorialReview;
	}
	
	public String[] getitem_Similarity_ASIN() {
		return itemSimilarityASIN;
	}
	
	public String[] getitem_Similarity_Title() {
		return itemSimilarityTitle;
	}


	public void printAmazonData(){
		System.out.println("Title:" + itemTitle);
		System.out.println("ASIN:" + itemAsin);
		System.out.println("LowestNewPrice:" + itemLowestNewPrice);
		System.out.println("LowestUsedPrice:" + itemLowestUsedPrice);
		System.out.println("LowestCollectiblePrice:" + itemLowestCollectiblePrice);
		System.out.println("MerchantId:" + itemMerchantId);
		System.out.println("MerchantName:" + itemMerchantName);
		System.out.println("MerchantGlancePage:" + itemMerchantGlancePage);
		System.out.println("IFrameURL:" + itemIFrameURL);
		System.out.println("PageURL:" + itemPageURL);
		System.out.println("editorialReview:" + itemeditorialReview);

		for(int i=0; i<5; i++){
        	System.out.print("Similar_product_ASIN: " + itemSimilarityASIN[i]);
			System.out.println("    Similar_product_Title: " + itemSimilarityTitle[i]);
        	}
  
		System.out.println("\n");
	}
}
