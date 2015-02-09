package edu.gatech.cs2340.risk.model;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;
public class Game extends HttpServlet{

    private Random rand = new Random();
    int numOfPlayers, startingArmies;
	int turn = 0;
    String[] init;
    Player[] players;
    Territory[] territories = new Territory[20];
    ArrayList<Integer> unownedTerritories;
    ArrayList<Integer> playerInd;
    boolean autoAssigned = false;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }
    public void doPost(){
        
    }
    
    public Game(String[] initArray){
         turn         = 0;
         numOfPlayers = 0;
         init         = initArray;

        for(int i = 0; i < initArray.length; i++)
        {
            if( initArray[i] != "" )
            {
                numOfPlayers++;
            }
        }

        initArmies();

        players = new Player[numOfPlayers];

        for(int i = 0; i < numOfPlayers; i++)
        {
            String playerName = initArray[i];
            Player player = new Player(playerName, startingArmies);
            players[i] = player;
        }

        randomizeTurns();
		Player emptyPlayer = new Player( "", 0 );
		  
		for(int i = 0; i < 20; i++)
        {
            territories[i] = new Territory(emptyPlayer);
        }
    }
    
    public void autoAssign(){
        unownedTerritories = new ArrayList<Integer>(20);
        playerInd = new ArrayList<Integer>(numOfPlayers);

        for(int i = 0; i < 20; i++)
        {
            unownedTerritories.add(i);
        }
        for(int i  = 0; i < numOfPlayers; i++)
        {
            playerInd.add(i);
        }

        autoAssignFirst();
        autoAssignRemainingTerritories();
        autoAssignRemainingArmies();
        for(int i  = 0; i < numOfPlayers; i++)
        {
            players[i].addArmy();
        }
    }
    
    public void autoAssignFirst(){
         while(unownedTerritories.size() >= numOfPlayers)
         {
             for(int i = 0; i < numOfPlayers; i++)
             {
                 int x = rand.nextInt(unownedTerritories.size());
                 int j = unownedTerritories.get(x);

                 territories[j].setTerritoryOwner(players[i]);
                 territories[j].setArmies(1);

                 unownedTerritories.remove(x);

                 players[i].subtractArmy();
                 players[i].takeoverTerritory(j);
             }
         }
    }
    
    public void autoAssignRemainingTerritories()
    {
        for(int i = 0; i < unownedTerritories.size(); i ++)
        {
            int x = rand.nextInt(playerInd.size());
            int j = playerInd.get(x);

            territories[unownedTerritories.get(i)].setTerritoryOwner(players[j]);
            territories[unownedTerritories.get(i)].setArmies(1);

            playerInd.remove(x);

            players[j].subtractArmy();
            players[i].takeoverTerritory(unownedTerritories.get(i));
        }
    }
    
    public void autoAssignRemainingArmies(){
        for(int i = 0; i < numOfPlayers; i++)
        {
            while(players[i].getArmiesToPlace() != 0)
            {
                int x = rand.nextInt(players[i].getOwnedTerritoriesInt().size());

                int j = players[i].getOwnedTerritoriesInt().get(x);

                territories[j].addArmies(1);
                players[i].subtractArmy();
            }
        }
    }
    
    public boolean getAutoAssigned(){
    	return autoAssigned;
    }
    
    public void setAutoAssigned(boolean autoAssigned){
    	this.autoAssigned = autoAssigned;
    }
    
    public int getNumOfPlayers(){
    	return numOfPlayers;
    }
    
    public int nextTurn(){
    	if(turn == numOfPlayers-1)
        {
    		turn = 0;
    	}
    	else
        {
    		turn++;
    	}

    	return turn;
    }
    
    String getOwner(int territory){
        return territories[territory-1].getTerritoryOwner().getName();
    }
    
    public String getArmies(int territory){
        int theArmies = territories[territory-1].getArmies();
		return Integer.toString(theArmies);
    }
    
    public Territory[] getTerritories(){
        return territories;
    }
    
    public String[] getOwnedTerritories(int player){
        boolean[] inBoolForm = players[player-1].getOwnedTerritoriesBoolean();

		String[] toReturn = new String[inBoolForm.length];

		for(int i = 0; i < inBoolForm.length; i++)
        {
			toReturn[i] = inBoolForm[i] + "";
		}

		return toReturn;
    }
    
	 
    public String getTurns(){
        String ret = "Turns: (1)- " + players[0].getName() + " (2)- " + players[1].getName() +" (3)- " + players[1].getName();

        for(int i = 3; i < numOfPlayers; i++)
        {
            ret = ret + " (" + (i+1) + ")- " + players[i].getName();
        }

        return ret;
    }
    
    public int getTurn(){
    	return turn;
    }
    
    public Player getPlayerTurnPlayer(){
        Player currentPlayer = players[getTurn()];
        return currentPlayer;
    }
    
    public String getPlayerTurn(){
        Player currentPlayer = players[getTurn()];
        String toReturn = currentPlayer.getName();
        return toReturn;
    }
	 
	public String getPlayerTurnArmies(){
	 	Player currentPlayer = players[getTurn()];
		int playerArmies = currentPlayer.getArmiesToPlace();
		String toReturn = Integer.toString(playerArmies);
		return toReturn;
	}
    
	public Player[] getPlayers(){
		return players;
	}
	
    public Player getPlayer(int number){
    	if(number > numOfPlayers)
        {
    		return new Player("null", 0);
    	}

    	return players[number-1];
    }
    
    public String getInitString(){
        String ret = init[0];

        for(int i = 1; i < init.length; i++)
        {
            ret = ret + ", " + init[i];
        }

        return ret;
    }
    
    public void initTerritories(){
        for(int i = 0; i < 42; i++)
        {
            Territory aTerritory = new Territory(null);
            territories[i] = aTerritory;
        }
    }
    
    public void initArmies(){
        if(numOfPlayers == 3)
        {
            startingArmies = 10;
        }
        else if(numOfPlayers == 4)
        {
            startingArmies = 8;
        }
        else if(numOfPlayers == 5)
        {
            startingArmies = 6;
        }
        else if(numOfPlayers == 6)
        {
            startingArmies = 4;
        }

        return;
    }
    
    public void givePlayerToTerritory(Player aPlayer, Territory aTerritory){
        aTerritory.setTerritoryOwner(aPlayer);
    }
    
    public void randomizeTurns(){
        if(numOfPlayers == 3)
        {
            int n = rand.nextInt(2) + 1;

            Player tmp = players[0];

            players[0] = players[n];
            players[n] = tmp;
        }

        if(numOfPlayers == 4)
        {
            int n = rand.nextInt(3) + 1;

            Player tmp = players[1];

            players[1] = players[2];
            players[2] = tmp;

            Player tmptwo = players[3];

            players[3] = players[n];
            players[n] = tmptwo;
        }

        if(numOfPlayers == 5)
        {
            int n = rand.nextInt(4) + 1;

            Player tmp = players[1];

            players[1] = players[3];
            players[3] = tmp;

            Player tmptwo = players[4];

            players[4] = players[n];
            players[n] = tmptwo;
        }

        if(numOfPlayers == 6)
        {
            int n = rand.nextInt(5) + 1;

            Player tmptwo = players[4];

            players[4] = players[n];
            players[n] = tmptwo;

            Player[] tmp = new Player[6];

            tmp[0] = players[4];
            tmp[1] = players[1];
            tmp[2] = players[2];
            tmp[3] = players[5];
            tmp[4] = players[0];
            tmp[5] = players[3];

            players = tmp;
        }
        
         return;
     }
	  
	  public String[] getArmies(){
        String[] ret = new String[20];

        for(int i = 1; i < 21; i++)
        {
            ret[i-1] = getArmies(i);
        }

        return ret;
    }
    
    public String[] getTerritoryOwners(){
        String[] ret = new String[20];

        for(int i = 0; i < 20; i++)
        {
            ret[i] = territories[i].getTerritoryOwner().getName();
        }

        return ret;
    }
}