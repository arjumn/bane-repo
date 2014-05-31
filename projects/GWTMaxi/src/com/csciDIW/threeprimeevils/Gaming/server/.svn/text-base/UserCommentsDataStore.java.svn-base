package com.csciDIW.threeprimeevils.Gaming.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class UserCommentsDataStore {
	
	public UserCommentsDataStore(String userName, String userEmail, int id,
			String gameName, String comment, Date date) {
		this.userName = userName;
		this.userEmail = userEmail;
		this.id = id;
		this.gameName = gameName;
		this.comment = comment;
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
	private String comment;
	
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

	public String getComment() {
		return comment;
	}

	public Date getDate() {
		return date;
	}
	
}
