package edu.gatech.cs2340.risk.model;

public class Territory{
    
    Player territoryOwner;
    int armies;
    
    public Territory(Player territoryOwner){
        this.territoryOwner = territoryOwner;
        this.armies = 0;
    }

    public void setTerritoryOwner(Player territoryOwner){
        this.territoryOwner = territoryOwner;
    }

    public Player getTerritoryOwner(){
        return territoryOwner;
    }

    public void setArmies(int armies){
        this.armies = armies;
    }

    public int getArmies(){
        return armies;
    }

    public void addArmies(int addArmies){
        armies = armies + addArmies;
    }

    public void subtractArmies(int removeArmies){
        armies = armies - removeArmies;

        if(armies <= -1)
        {
            armies = 0;
        }
    }

}