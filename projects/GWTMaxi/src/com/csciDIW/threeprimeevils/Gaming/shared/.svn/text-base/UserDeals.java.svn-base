package com.csciDIW.threeprimeevils.Gaming.shared;

import java.io.Serializable;
import java.util.Date;

public class UserDeals implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public UserDeals(){}
	
	public UserDeals(String userName, String userEmail, int id,
			String gameName, String url, double price,  Date date) {
		this.userName = userName;
		this.userEmail = userEmail;
		this.id = id;
		this.gameName = gameName;
		this.url = url;
		this.price = price;
		this.date = date;
	}

	private String userName;
	
	private String userEmail;
	
	private int id;
	
	private String gameName;
	
	private String url;
	
	private double price;
	
	private Date date;

	public String getUserName() {
		return userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public int getId() {
		return id;
	}

	public String getGameName() {
		return gameName;
	}

	public String getUrl() {
		return url;
	}
	
	public double getPrice() {
		return price;
	}

	public Date getDate() {
		return date;
	}
	
	public void printUserComments(){
		System.out.println("UserName:" + userName);
		System.out.println("UserEmail:" + userEmail);
		System.out.println("Id:" + id);
		System.out.println("GameName:" + gameName);
		System.out.println("Url:" + url);
		System.out.println("Price:" + price);
		System.out.println("Date:" + date.toString());
		System.out.println("\n");
	}
}
