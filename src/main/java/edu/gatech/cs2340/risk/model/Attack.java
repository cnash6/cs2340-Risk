package edu.gatech.cs2340.risk.model;
import java.util.*;

/*
everytime player chooses to attack, create new attack taking in the attacking territory
how many dice they are rolling and the same for the defending territory. then call the 
generate attack method, which generates an array for each territory containing random num
1-6(sides of dice). and then sorts them and compares the results. returns the winning territory
*/
public class Attack{
	
	Territory attackingTerritory, defendingTerritory;
	int attackingTerritoryNumber, defendingTerritoryNumber;
	int numAttackingDice, numDefendingDice;
	int sidesOfDice = 6;
	int numAttackerWin = 0;
	int numDefenderWin = 0;
	Territory winningTerritory;
	int[] attackingDiceRollResults, defendingDiceRollResults;
	String[] resultSigns;
	
	
	private Random rand = new Random();
	
	public Attack(Territory attackingTerritory, Territory defendingTerritory, int numAttackingDice, int numDefendingDice, int attackingTerritoryNumber, int defendingTerritoryNumber){
		this.attackingTerritory = attackingTerritory;
		this.defendingTerritory = defendingTerritory;
		this.numAttackingDice = numAttackingDice;
		this.numDefendingDice = numDefendingDice;
		this.attackingTerritoryNumber = attackingTerritoryNumber;
		this.defendingTerritoryNumber = defendingTerritoryNumber;
	}
	
	public void setAttackingTerritory(Territory attackingTerritory){
		this.attackingTerritory = attackingTerritory;
	}
	
	public Territory getAttackingTerritory(){
		return attackingTerritory;
	}
	
	public Player getAttackingTerritoryOwner(){
		return attackingTerritory.getTerritoryOwner();
	}
	
	public void setDefendingTerritory(Territory defendingTerritory){
		this.defendingTerritory = defendingTerritory;
	}
	
	public Territory getDefendingTerritory(){
		return defendingTerritory;
	}
	
	public Player getDefendingTerritoryOwner(){
		return defendingTerritory.getTerritoryOwner();
	}
	
	public int getNumAttackingDice(){
		return numAttackingDice;
	}
	
	public void setNumAttackingDice(int numAttackingDice){
		this.numAttackingDice = numAttackingDice;
	}
	
	public int getNumDefendingDice(){
		return numDefendingDice;
	}
	
	public void setNumDefendingDice(int numDefendingDice){
		this.numDefendingDice = numDefendingDice;
	}
	
	
	public int[] getAttackingDiceRollResults(){
		return attackingDiceRollResults;
	}
	
	public int[] getDefendingDiceRollResults(){
		return defendingDiceRollResults;
	}
	
	public String[] getResultSigns(){
		return resultSigns;
	}
	
	public boolean ableToAttack(Territory attacker, Territory defender){
		//not enough armies to attack for attacker
		//or there exists no armies on defending territory
		if(attacker.getArmies() < 2 || defender.getArmies() <= 0)
			return false;
		
		return true;
	}
	
	
	//return the winning territory
	public Territory generateAttack(){
		if(ableToAttack(attackingTerritory, defendingTerritory) == true){
			if(numDefendingDice > defendingTerritory.getArmies())
				numDefendingDice = defendingTerritory.getArmies();
			if(numAttackingDice > attackingTerritory.getArmies() - 1)
				numAttackingDice = attackingTerritory.getArmies() - 1;
			randomizeAttackingDice(numAttackingDice);
			randomizeDefendingDice(numDefendingDice);
		
			
			compareDiceRolls();
			
			calculateResults(numDefenderWin, numAttackerWin);
			return winningTerritory;
		}
		return attackingTerritory;
	}
	
	public void calculateResults(int defenderScore, int attackerScore){
		defendingTerritory.subtractArmies(attackerScore);
		attackingTerritory.subtractArmies(defenderScore);
		checkForNewDefendingTerritoryOwner();
	}
	
	public void checkForNewDefendingTerritoryOwner(){
		if(defendingTerritory.getArmies() <= 0){
			attackingTerritory.getTerritoryOwner().takeoverTerritory(attackingTerritoryNumber);
			defendingTerritory.getTerritoryOwner().loseTerritory(defendingTerritoryNumber);;
			defendingTerritory.setTerritoryOwner(attackingTerritory.getTerritoryOwner());
			defendingTerritory.addArmies(numAttackingDice);
			attackingTerritory.subtractArmies(numAttackingDice);
		}
	}
	
	public void compareDiceRolls(){
		int index = 0;
		numAttackerWin = 0;
		numDefenderWin = 0;
		winningTerritory = null;
		resultSigns = new String[3];
		for(int i = 0; i < 3; i++){
			resultSigns[i] = "-";
		}
		for(int i = 0; i < numDefendingDice; i ++){
			if(attackingDiceRollResults[index] > defendingDiceRollResults[index]){
				numAttackerWin ++;
				resultSigns[index] = ">";
			}
			else{
				numDefenderWin ++;
				resultSigns[index] = "<";
			}
			
			index++;
		}
	}
	
	public void randomizeAttackingDice(int numAttackingDice){
		attackingDiceRollResults = new int[3];
		for(int i = 0; i < 3; i++){
			attackingDiceRollResults[i] = 0;
		}
		int index = 0;
		for(int i = 0; i < numAttackingDice; i ++){
			int roll = rand.nextInt(sidesOfDice);
			attackingDiceRollResults[index] = roll + 1;
			index++;
		}
		sortDiceRolls(attackingDiceRollResults);
	}
	
	public void randomizeDefendingDice(int numDefendingDice){
		defendingDiceRollResults = new int[2];
		for(int i = 0; i < 2; i++){
			defendingDiceRollResults[i] = 0;
		}
		int index = 0;
		for(int i = 0; i < numDefendingDice; i ++){
			int roll = rand.nextInt(sidesOfDice);
			defendingDiceRollResults[index] = roll + 1;
			index++;
		}
		
		sortDiceRolls(defendingDiceRollResults);
	}
	
	//sort arrays from largest to smallest, compare two arrays to decipher winner
	//used in randomizing dice roll methods
	public void sortDiceRolls(int[] diceRolls){
		int temp;
		for(int j = 0; j < diceRolls.length - 1; j++){
			for(int i = 0; i < diceRolls.length -1; i++){
				if(diceRolls[i] < diceRolls[i+1]){
					temp = diceRolls[i];
					diceRolls[i] = diceRolls[i+1];
					diceRolls[i+1] = temp;
				}
			}
		}
	}
	
}
