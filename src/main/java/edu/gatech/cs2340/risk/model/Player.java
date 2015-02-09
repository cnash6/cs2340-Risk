package edu.gatech.cs2340.risk.model;
import java.util.ArrayList;

public class Player {
	
	private boolean isTurn, isAlive;
	private int armiesToPlace, turnOrderNumber;
	private String name;
	private boolean[] ownedTerritoriesBoolean;
	
	public Player(String name, int armiesToPlace){
		this.name = name;
		this.armiesToPlace = armiesToPlace;
		isAlive = true;
		ownedTerritoriesBoolean = new boolean[20];

		for(int i = 0; i < 20; i++)
		{
		    ownedTerritoriesBoolean[i] = false;
		}
	}
	
	public void setTurnOrderNumber(int turnOrderNumber){
	    this.turnOrderNumber = turnOrderNumber;
	}
	
	public int getTurnOrderNumber(){
        return turnOrderNumber;
    }
	
	public void takeoverTerritory(int territory){
	    ownedTerritoriesBoolean[territory] = true;
	}
	
	public void loseTerritory(int territory){
	    ownedTerritoriesBoolean[territory] = false;
	}
	
	public boolean[] getOwnedTerritoriesBoolean(){
	    return ownedTerritoriesBoolean;
	}
	
	public ArrayList<Integer> getOwnedTerritoriesInt(){
	    ArrayList<Integer> ret = new ArrayList<Integer>(0);
	    for(int i = 0; i < 20; i++)
	    {
	        if(ownedTerritoriesBoolean[i])
	        {
	            ret.add(i);
	        }
	    }

	    return ret;
	}
	
	public void addArmy(){
		armiesToPlace++;
	}
	
	public void subtractArmy(){
	    armiesToPlace--;
	}
	
	public void setArmiesToPlace(int armiesToPlace){
		this.armiesToPlace = armiesToPlace;
	}
	
	public int getArmiesToPlace(){
		return armiesToPlace;
	}
	
	//public void setColor(String color){
		//this.color = color;
	//}
	
	//public String getColor(){
		//return color;
	//}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setTurn(boolean isTurn){
		this.isTurn = isTurn;
	}
	
	public boolean getTurn(){
		return isTurn;
	}
	
	public boolean checkAlive(){
		boolean alive = false;
		for(int i = 0; i < 20; i++){
			if(ownedTerritoriesBoolean[i]){
				alive = true;
			}
		}
		isAlive = alive;
		return alive;
	}
	
	public void setAlive(boolean isAlive){
		this.isAlive = isAlive;
	}
	
	public boolean isAlive(){
		return isAlive;
	}
}