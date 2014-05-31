package com.csciDIW.threeprimeevils.Gaming.shared;

import java.io.Serializable;
import java.util.Date;

public class GiantBombGame implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String deck;
	private String description;
	private String[] genres;
	private String image;
	private String smallImage;
	private String[] gameImages;
	private int id;
	private String name;
	private Date releaseDate;
	
	public GiantBombGame(){}
	
	public GiantBombGame(String deck, String description, String[] genres,
			String image, String smallImage, String[] gameImages, int id, String name, Date releaseDate) {
		this.deck = deck;
		this.description = description;
		this.genres = genres;
		this.image = image;
		this.smallImage = smallImage;
		this.gameImages = gameImages;
		this.id = id;
		this.name = name;
		this.releaseDate = releaseDate;
	}

	public String getDeck() {
		return deck;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String[] getGenres() {
		return genres;
	}
	
	public String getImage() {
		return image;
	}
	
	public String getSmallImage() {
		return smallImage;
	}
	
	public String[]  getGameImages() {
		return gameImages;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	
	public void printGameDetails(){
		System.out.println("id:" + id);
		System.out.println("name:" + name);
		System.out.println("deck:" + deck);
		System.out.println("description:" + description);
		System.out.println("image:" + image);
		if(releaseDate != null)
			System.out.println("release date:" + releaseDate.toString());
		else
			System.out.println("release date:" + null);
		System.out.print("genre:");
		for(int i=0; i<genres.length; i++)
			System.out.print(genres[i] +",");
		System.out.println("\n");
		System.out.print("gameImages:");
		for(int i=0; i<gameImages.length; i++)
			System.out.print(gameImages[i] +",");
		System.out.println("\n");
	}
}
