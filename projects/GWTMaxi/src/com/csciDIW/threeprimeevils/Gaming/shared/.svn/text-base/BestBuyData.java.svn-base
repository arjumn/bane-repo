package com.csciDIW.threeprimeevils.Gaming.shared;

import java.io.Serializable;

public class BestBuyData implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int skuId;
	private String name;
	private double price;
	private String url;
	private BestBuyReview[] reviews;
	
	public BestBuyData(){};
	
	public BestBuyData(String name, double price, String url, int id, BestBuyReview[] reviews) {
		this.name = name;
		this.price = price;
		this.url = url;
		this.skuId = id;
		this.reviews = reviews;
	}
	
	public int getSkuId(){
		return skuId;
	}
	
	public String getName() {
		return name;
	}
	
	public double getPrice() {
		return price;
	}
	
	public String getUrl() {
		return url;
	}
	
	public BestBuyReview[] getReviews() {
		return reviews;
	}
	
	public void printProductDetails(){
		System.out.println("skuId:" + skuId);
		System.out.println("Name:" + name);
		System.out.println("Price:" + price);
		System.out.println("Url:" + url);
		for(int i=0; i<reviews.length; i++){
			reviews[i].printReviewDetails();
		}
	}

}
