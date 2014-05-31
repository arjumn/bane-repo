package com.csciDIW.threeprimeevils.Gaming.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class UserDealsDataStore {
	
	public UserDealsDataStore(String userName, String userEmail, int id,
			String gameName, String url, double price, Date date) {
		this.userName = userName;
		this.userEmail = userEmail;
		this.id = id;
		this.gameName = gameName;
		this.url = url;
		this.price = price;
		this.date = date;
	}

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String userName;
	
	@Persistent
	private String userEmail;
	
	@Persistent
	private int id;
	
	@Persistent
	private String gameName;
	
	@Persistent
	private String url;
	
	@Persistent
	private double price;
	
	@Persistent
	private Date date;

	public Key getKey() {
		return key;
	}

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

	public Date getDate() {
		return date;
	}
	
	public double getPrice() {
		return price;
	}

	
}
