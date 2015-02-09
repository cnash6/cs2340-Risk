package edu.gatech.cs2340.risk.model;
import java.util.*;

public class GameEnding {
	
	Territory[] territories;
	
	public GameEnding(Territory[] territories){
		this.territories = territories;
	}
		
		
	public String hasGameEnded(){
		Territory sample = territories[0];
		Player sampleOwner = sample.getTerritoryOwner();
		String endOfGame = "yes";
		for(int i = 0; i < territories.length; i++){
			if(territories[i].getTerritoryOwner() != sampleOwner){
				endOfGame = "no";
			}
		}
		
		return endOfGame;
	}
}