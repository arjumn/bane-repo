package com.csciDIW.threeprimeevils.Gaming.shared;

import java.io.Serializable;
import java.util.Date;

public class DiggNews implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int diggs;
	private String title;
	private Date date;
	private String image;
	private String url;
	private String description;
	
	public DiggNews(){};
	
	public DiggNews(int diggs, String title, Date date, String image,
			String url, String description) {
		super();
		this.diggs = diggs;
		this.title = title;
		this.date = date;
		this.image = image;
		this.url = url;
		this.description = description;
	}
	
	public int getDiggs() {
		return diggs;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getImage() {
		return image;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void printDiggNews(){
		System.out.println("Diggs:" + diggs);
		System.out.println("Title:" + title);
		System.out.println("Date:" + date);
		System.out.println("Image:" + image);
		System.out.println("Url:" + url);
		System.out.println("Description:" + description);
		System.out.println("\n");
	}

}
