package com.csciDIW.threeprimeevils.Gaming.shared;

import java.io.Serializable;
import java.util.Date;

public class UserComments implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public UserComments(){}
	
	public UserComments(String userName, String userEmail, int id,
			String gameName, String comment, Date date) {
		this.userName = userName;
		this.userEmail = userEmail;
		this.id = id;
		this.gameName = gameName;
		this.comment = comment;
		this.date = date;
	}

	private String userName;
	
	private String userEmail;
	
	private int id;
	
	private String gameName;
	
	private String comment;
	
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

	public String getComment() {
		return comment;
	}

	public Date getDate() {
		return date;
	}
	
	public void printUserComments(){
		System.out.println("UserName:" + userName);
		System.out.println("UserEmail:" + userEmail);
		System.out.println("Id:" + id);
		System.out.println("GameName:" + gameName);
		System.out.println("Comment:" + comment);
		System.out.println("Date:" + date.toString());
		System.out.println("\n");
	}
}
