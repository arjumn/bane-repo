package com.csciDIW.threeprimeevils.Gaming.shared;

import java.io.Serializable;

public class ServerData implements Serializable{
	private static final long serialVersionUID = 1L;
	
	GiantBombGame giantBombGame;
	BestBuyData bestBuyData;
	
	public ServerData(){}
	
	public ServerData(GiantBombGame giantBombGame, BestBuyData bestBuyData){
		this.giantBombGame = giantBombGame;
		this.bestBuyData = bestBuyData;
	}

	public GiantBombGame getGiantBombGame() {
		return giantBombGame;
	}

	public BestBuyData getBestBuyData() {
		return bestBuyData;
	}
}
