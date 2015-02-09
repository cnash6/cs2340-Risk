package edu.gatech.cs2340.risk.model;
import java.util.*;

public class FortifyTerritory {
	
	Territory originalTerritory, territoryToFortify;
	int armiesToMove;
	
	public FortifyTerritory(Territory originalTerritory, Territory territoryToFortify, int armiesToMove){
		this.originalTerritory = originalTerritory;
		this.territoryToFortify = territoryToFortify;
		this.armiesToMove = armiesToMove;
	}
		
		
	public void fortify(){
		int armiesInFromTerr = originalTerritory.getArmies();
		int remainingArmies = armiesInFromTerr - armiesToMove;
		int armiesBeforeFortify = territoryToFortify.getArmies();
		if(remainingArmies >= 1){
			originalTerritory.setArmies(remainingArmies);
			territoryToFortify.setArmies(armiesBeforeFortify + armiesToMove);
		}
	}
}
