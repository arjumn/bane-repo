package com.csciDIW.threeprimeevils.Gaming.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class GameInfo {
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String name;
	
	@Persistent
	private String platform;

	@Persistent
    private int bestBuyId;
	
	@Persistent
	private String amazonId;
	
	@Persistent
	private int giantBombId;
	
	// constructor
	public GameInfo(String name,
					String platform,
					int bestBuyId,
					String amazonId,
					int giantBombId) {
		this.name = name;
		this.platform = platform;
		this.bestBuyId = bestBuyId;
		this.amazonId = amazonId;
		this.giantBombId = giantBombId;
	}
	
	public Key getKey() {
		return key;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPlatform() {
		return this.platform;
	}

	public int getBestBuyId() {
		return this.bestBuyId;
	}

	public String getAmazonId() {
		return this.amazonId;
	}
	
	public long getGiantBombId() {
		return giantBombId;
	}

	// setter methods
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPlaform(String platform) {
		this.platform = platform;
	}

	public void setBestBuyId(int bbId) {
		this.bestBuyId = bbId;
	}

	public void setAmazonId(String amazonId) {
		this.amazonId = amazonId;
	}
	
	public void setGiantBombId(int giantBombId) {
		this.giantBombId = giantBombId;
	}
};