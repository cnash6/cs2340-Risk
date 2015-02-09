package main.java.edu.gatech.cs2340.risk.controller;

import edu.gatech.cs2340.risk.model.Game;
import edu.gatech.cs2340.risk.model.Territory;
import edu.gatech.cs2340.risk.model.Attack;
import edu.gatech.cs2340.risk.model.FortifyTerritory;
import edu.gatech.cs2340.risk.model.GameEnding;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@WebServlet(urlPatterns={
        "/list", 
        "/create", 
        "/update/*", 
        "/auto/*",
        "/reinforce/*",
        "/endTurn/*",
        "/delete/*",
        "/attack/*",
        "/wintest/*",
          "/fortify/*"
    })
public class TodoServlet extends HttpServlet {

    String[] initArray = {"default", "default", "default"};

    Game myGame = new Game(initArray);

    String[] territoryOwners = {
                                    "no owner", "no owner", "no owner", "no owner", "no owner", 
                                    "no owner", "no owner", "no owner", "no owner", "no owner", 
                                    "no owner", "no owner", "no owner", "no owner", "no owner", 
                                    "no owner", "no owner", "no owner", "no owner", "no owner"
                                };
    String[] territoryArmies = { 
                                   "0","0","0","0","0","0","0","0","0","0",
                                   "0","0","0","0","0","0","0","0","0","0" 
                               };

    String currentTurnName         = "";
    String currentTurnPlayerArmies = "0";
    String territoryChoiceString   = "1";
    String turn;
     
     
     String[] attackResults = {"-", "-"};
     int[] attackerDiceResults = {0,0,0};
     int[] defenderDiceResults = {0,0};
     
     String hasGameEnded = "no";
     
     boolean reinforced = false;
     boolean fortified = false;

    @Override

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException, ServletException {

    
        initArray = new String[6];

        initArray[0] = (String) request.getParameter("title1");
        initArray[1] = (String) request.getParameter("title2");
        initArray[2] = (String) request.getParameter("title3");
        initArray[3] = (String) request.getParameter("title4");
        initArray[4] = (String) request.getParameter("title5");
        initArray[5] = (String) request.getParameter("title6");
        
        myGame = new Game(initArray);
        

        territoryOwners         = myGame.getTerritoryOwners();
        territoryArmies         = myGame.getArmies();

        currentTurnName         = myGame.getPlayerTurn();
        currentTurnPlayerArmies = myGame.getPlayerTurnArmies();


        turn = Integer.toString(myGame.getTurn());

        hasGameEnded = "no";
        
        request.setAttribute( "turn",                    turn );
        request.setAttribute( "myGame",                  myGame );
        request.setAttribute( "territoryOwners",         territoryOwners );
        request.setAttribute( "territoryArmies",         territoryArmies );
        request.setAttribute( "currentTurnName",         currentTurnName );
        request.setAttribute( "currentTurnPlayerArmies", currentTurnPlayerArmies );
        request.setAttribute( "territoryChoiceString",   territoryChoiceString );
        request.setAttribute( "attackResults",           attackResults );
        request.setAttribute( "attackerDiceResults",     attackerDiceResults) ; 
        request.setAttribute( "defenderDiceResults",     defenderDiceResults );
        request.setAttribute( "hasGameEnded",            hasGameEnded );

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/list.jsp");
        dispatcher.forward(request,response);
    
    }
    

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException, ServletException {

        System.out.println("In doGet()");
        
        if (request.getServletPath().equals("/reinforce"))
        {
            territoryChoiceString = (String) request.getParameter("territoriesDropDown");

            int territoryChoice = parseEntry(territoryChoiceString);
            
            int asNumber = parseEntry(territoryChoiceString);
            boolean isItOwned = false;

            if((myGame.getTerritories())[asNumber].getTerritoryOwner() == myGame.getPlayerTurnPlayer())
            {
                isItOwned = true;
            }
            
            if(myGame.getPlayerTurnPlayer().getArmiesToPlace() == 1 && isItOwned == true)
            {
                (myGame.getTerritories())[territoryChoice].addArmies(1);
                myGame.getPlayerTurnPlayer().subtractArmy();
                reinforced = true;
            }

            territoryOwners = myGame.getTerritoryOwners();
            territoryArmies = myGame.getArmies();

            currentTurnName         = myGame.getPlayerTurn();
            currentTurnPlayerArmies = myGame.getPlayerTurnArmies();

            turn = Integer.toString(myGame.getTurn());
             
            request.setAttribute( "myGame",                  myGame );
            request.setAttribute( "territoryOwners",         territoryOwners );
            request.setAttribute( "territoryChoiceString",   territoryChoiceString );
            request.setAttribute( "territoryArmies",         territoryArmies );
            request.setAttribute( "currentTurnName",         currentTurnName );
            request.setAttribute( "currentTurnPlayerArmies", currentTurnPlayerArmies );
            request.setAttribute( "turn",                    turn );
            request.setAttribute( "attackResults",           attackResults );
            request.setAttribute( "attackerDiceResults",     attackerDiceResults) ; 
            request.setAttribute( "defenderDiceResults",     defenderDiceResults );
            request.setAttribute( "hasGameEnded",            hasGameEnded );

            RequestDispatcher dispatcher =
            getServletContext().getRequestDispatcher("/list.jsp");
            dispatcher.forward(request,response);
            return;
        }
        else if(request.getServletPath().equals("/wintest"))
        {
            if(myGame.getAutoAssigned() == false){
                myGame.setAutoAssigned(true);
                int ind = 0;
    
                for(int i = 0; i < 20- myGame.getNumOfPlayers(); i++)
                {
                    myGame.getTerritories()[i].getTerritoryOwner().loseTerritory(i);
                    myGame.getTerritories()[i].setTerritoryOwner(myGame.getPlayers()[0]);
                    myGame.getTerritories()[i].setArmies(1);
                    myGame.getPlayers()[0].takeoverTerritory(i);
                    ind = i;
                }
                
                myGame.getTerritories()[ind+1].setTerritoryOwner(myGame.getPlayers()[0]);
                myGame.getTerritories()[ind+1].setArmies(50);
                myGame.getPlayers()[0].takeoverTerritory(ind+1);
                myGame.getPlayers()[0].setArmiesToPlace(1);
                
                for(int i = 0; i < myGame.getNumOfPlayers()-1; i ++)
                {
                    myGame.getTerritories()[19-i].getTerritoryOwner().loseTerritory(19-i);
                    myGame.getTerritories()[19-i].setTerritoryOwner(myGame.getPlayers()[1+i]);
                    myGame.getTerritories()[19-i].setArmies(1);
                    myGame.getPlayers()[1+i].takeoverTerritory(19-i);
                    myGame.getPlayers()[1+i].setArmiesToPlace(1);
                }
                    
                territoryOwners         = myGame.getTerritoryOwners();
                territoryArmies         = myGame.getArmies();
                currentTurnName         = myGame.getPlayerTurn();
                currentTurnPlayerArmies = myGame.getPlayerTurnArmies();
    
                turn = Integer.toString(myGame.getTurn());
                 
                request.setAttribute( "myGame",                  myGame );
                request.setAttribute( "territoryOwners",         territoryOwners );
                request.setAttribute( "territoryChoiceString",   territoryChoiceString );
                request.setAttribute( "territoryArmies",         territoryArmies );
                request.setAttribute( "currentTurnName",         currentTurnName );
                request.setAttribute( "currentTurnPlayerArmies", currentTurnPlayerArmies );
                request.setAttribute( "turn",                    turn );
                request.setAttribute( "attackResults",           attackResults );
                request.setAttribute( "attackerDiceResults",     attackerDiceResults) ; 
                request.setAttribute( "defenderDiceResults",     defenderDiceResults );
                request.setAttribute( "hasGameEnded",            hasGameEnded );
    
                RequestDispatcher dispatcher =
                getServletContext().getRequestDispatcher("/list.jsp");
                dispatcher.forward(request,response);
                return;
            }
            territoryOwners = myGame.getTerritoryOwners();
            territoryArmies = myGame.getArmies();
            currentTurnName = myGame.getPlayerTurn();
            currentTurnPlayerArmies = myGame.getPlayerTurnArmies();
            turn = Integer.toString(myGame.getTurn());
             
            request.setAttribute( "myGame",                  myGame );
            request.setAttribute( "territoryOwners",         territoryOwners );
            request.setAttribute( "territoryChoiceString",   territoryChoiceString );
            request.setAttribute( "territoryArmies",         territoryArmies );
            request.setAttribute( "currentTurnName",         currentTurnName );
            request.setAttribute( "currentTurnPlayerArmies", currentTurnPlayerArmies );
            request.setAttribute( "turn",                    turn );
            request.setAttribute( "attackResults",           attackResults );
            request.setAttribute( "attackerDiceResults",     attackerDiceResults) ; 
            request.setAttribute( "defenderDiceResults",     defenderDiceResults );
            request.setAttribute( "hasGameEnded",            hasGameEnded );

            RequestDispatcher dispatcher =
            getServletContext().getRequestDispatcher("/list.jsp");
            dispatcher.forward(request,response);
            return;
        }
        else if (request.getServletPath().equals("/auto")){
            if(myGame.getAutoAssigned() == false){
                myGame.autoAssign();
                myGame.setAutoAssigned(true);
            }
                
            
            territoryOwners = myGame.getTerritoryOwners();
            territoryArmies = myGame.getArmies();
            currentTurnName = myGame.getPlayerTurn();
            currentTurnPlayerArmies = myGame.getPlayerTurnArmies();
            turn = Integer.toString(myGame.getTurn());
             
            request.setAttribute( "myGame",                  myGame );
            request.setAttribute( "territoryOwners",         territoryOwners );
            request.setAttribute( "territoryChoiceString",   territoryChoiceString );
            request.setAttribute( "territoryArmies",         territoryArmies );
            request.setAttribute( "currentTurnName",         currentTurnName );
            request.setAttribute( "currentTurnPlayerArmies", currentTurnPlayerArmies );
            request.setAttribute( "turn",                    turn );
            request.setAttribute( "attackResults",           attackResults );
            request.setAttribute( "attackerDiceResults",     attackerDiceResults) ; 
            request.setAttribute( "defenderDiceResults",     defenderDiceResults );
            request.setAttribute( "hasGameEnded",            hasGameEnded );

            RequestDispatcher dispatcher =
            getServletContext().getRequestDispatcher("/list.jsp");
            dispatcher.forward(request,response);
            return;
        }
        else if (request.getServletPath().equals("/attack"))
        {
            if(reinforced && !fortified)
            {
            
                int attackingTerritoryNum = parseEntry((String) request.getParameter("attackingTerritoryDropDown"));
                int defendingTerritoryNum = parseEntry((String) request.getParameter("defendingTerritoryDropDown"));
                int attackingDice         = parseEntry((String) request.getParameter("attackDiceDropDown"));
                int defendingDice         = parseEntry((String) request.getParameter("defendDiceDropDown"));
                
                Territory attackingTerritory = myGame.getTerritories()[attackingTerritoryNum];
                Territory defendingTerritory = myGame.getTerritories()[defendingTerritoryNum];
                
                boolean isItOwned = false;
                if((myGame.getTerritories())[attackingTerritoryNum].getTerritoryOwner() == myGame.getPlayerTurnPlayer())
                {
                        isItOwned = true;
                }
                
                boolean ownsDefending = true;
                if((myGame.getTerritories())[defendingTerritoryNum].getTerritoryOwner() != myGame.getPlayerTurnPlayer())
                {
                        ownsDefending = false;
                }
    
                boolean isAdjacent = checkAdjacency(attackingTerritoryNum + 1, defendingTerritoryNum + 1);
                
                if(isAdjacent == true && ownsDefending == false && isItOwned == true)
                {
                    Attack attack = new Attack(attackingTerritory, defendingTerritory, attackingDice, defendingDice, attackingTerritoryNum, defendingTerritoryNum);
                    attack.generateAttack();
                    String[] attackResults = attack.getResultSigns();

                    int[] attackerDiceResults = attack.getAttackingDiceRollResults();
                    int[] defenderDiceResults = attack.getDefendingDiceRollResults();
                    
                    Territory[] allTerritories = myGame.getTerritories();
                    GameEnding checkForEnd = new GameEnding(allTerritories);
                    hasGameEnded = checkForEnd.hasGameEnded();
                    request.setAttribute( "attackResults",       attackResults );
                    request.setAttribute( "attackerDiceResults", attackerDiceResults) ; 
                    request.setAttribute( "defenderDiceResults", defenderDiceResults );
                    request.setAttribute( "hasGameEnded",        hasGameEnded );
                }
                else
                {
                    request.setAttribute( "attackResults",       attackResults );
                    request.setAttribute( "attackerDiceResults", attackerDiceResults) ; 
                    request.setAttribute( "defenderDiceResults", defenderDiceResults );
                    request.setAttribute( "hasGameEnded",        hasGameEnded );
    
                }
            }
            else
            {
                    request.setAttribute( "attackResults",       attackResults );
                    request.setAttribute( "attackerDiceResults", attackerDiceResults) ; 
                    request.setAttribute( "defenderDiceResults", defenderDiceResults );
                    request.setAttribute( "hasGameEnded",        hasGameEnded );
            }

            territoryOwners         = myGame.getTerritoryOwners();
            territoryArmies         = myGame.getArmies();
            currentTurnName         = myGame.getPlayerTurn();
            currentTurnPlayerArmies = myGame.getPlayerTurnArmies();

            turn = Integer.toString(myGame.getTurn());
                              
            request.setAttribute( "myGame",                  myGame );
            request.setAttribute( "territoryOwners",         territoryOwners );
            request.setAttribute( "territoryChoiceString",   territoryChoiceString );
            request.setAttribute( "territoryArmies",         territoryArmies );
            request.setAttribute( "currentTurnName",         currentTurnName );
            request.setAttribute( "currentTurnPlayerArmies", currentTurnPlayerArmies );
            request.setAttribute( "turn",                    turn );
                 
            RequestDispatcher dispatcher =
            getServletContext().getRequestDispatcher("/list.jsp");
            dispatcher.forward(request,response);
            return;
        }
        else if (request.getServletPath().equals("/fortify"))
        {
            territoryOwners         = myGame.getTerritoryOwners();
            territoryArmies         = myGame.getArmies();
            currentTurnName         = myGame.getPlayerTurn();
            currentTurnPlayerArmies = myGame.getPlayerTurnArmies();

            turn = Integer.toString(myGame.getTurn());

            if(reinforced && !fortified)
            {
                int fromTerritoryNum = parseEntry((String) request.getParameter("fortifyingFromTerritoryDropDown"));
                int toTerritoryNum   = parseEntry((String) request.getParameter("fortifyingToTerritoryDropDown"));

                int armiesToMove = parseEntry((String) request.getParameter("numOfArmiesToFortify"));
                
               Territory fromTerritory = myGame.getTerritories()[fromTerritoryNum];
               Territory toTerritory   = myGame.getTerritories()[toTerritoryNum];
                
                boolean ownsFromTerr = false;
                if((myGame.getTerritories())[fromTerritoryNum].getTerritoryOwner() == myGame.getPlayerTurnPlayer())
                {
                    ownsFromTerr = true;
                }
            
                boolean ownsToTerr = false;
                if((myGame.getTerritories())[toTerritoryNum].getTerritoryOwner() == myGame.getPlayerTurnPlayer())
                {
                    ownsToTerr = true;
                }

                boolean isAdjacent = checkAdjacency(fromTerritoryNum+1, toTerritoryNum+1);

                if(isAdjacent == true && ownsToTerr == true && ownsFromTerr == true)
                {
                    FortifyTerritory fortifying = new FortifyTerritory(fromTerritory, toTerritory, armiesToMove);
                    fortifying.fortify();
                    fortified = true;
        
                
                    territoryOwners         = myGame.getTerritoryOwners();
                    territoryArmies         = myGame.getArmies();
                    currentTurnName         = myGame.getPlayerTurn();
                    currentTurnPlayerArmies = myGame.getPlayerTurnArmies();

                    turn = Integer.toString(myGame.getTurn());

                    request.setAttribute( "myGame",                  myGame );
                    request.setAttribute( "territoryOwners",         territoryOwners );
                    request.setAttribute( "territoryChoiceString",   territoryChoiceString );
                    request.setAttribute( "territoryArmies",         territoryArmies );
                    request.setAttribute( "currentTurnName",         currentTurnName );
                    request.setAttribute( "currentTurnPlayerArmies", currentTurnPlayerArmies );
                    request.setAttribute( "turn",                    turn );
                    request.setAttribute( "attackResults",           attackResults );
                    request.setAttribute( "attackerDiceResults",     attackerDiceResults) ; 
                    request.setAttribute( "defenderDiceResults",     defenderDiceResults );
                    request.setAttribute( "hasGameEnded",            hasGameEnded );
                }
                else
                {
                    territoryOwners = myGame.getTerritoryOwners();
                    territoryArmies = myGame.getArmies();
                    currentTurnName = myGame.getPlayerTurn();
                    currentTurnPlayerArmies = myGame.getPlayerTurnArmies();
                    turn = Integer.toString(myGame.getTurn());
                    request.setAttribute( "myGame",                  myGame );
                    request.setAttribute( "territoryOwners",         territoryOwners );
                    request.setAttribute( "territoryChoiceString",   territoryChoiceString );
                    request.setAttribute( "territoryArmies",         territoryArmies );
                    request.setAttribute( "currentTurnName",         currentTurnName );
                    request.setAttribute( "currentTurnPlayerArmies", currentTurnPlayerArmies );
                    request.setAttribute( "turn",                    turn );
                    request.setAttribute( "attackResults",           attackResults );
                    request.setAttribute( "attackerDiceResults",     attackerDiceResults) ; 
                    request.setAttribute( "defenderDiceResults",     defenderDiceResults );
                    request.setAttribute( "hasGameEnded",            hasGameEnded );
                }
            }
            else
            {
                territoryOwners         = myGame.getTerritoryOwners();
                territoryArmies         = myGame.getArmies();
                currentTurnName         = myGame.getPlayerTurn();
                currentTurnPlayerArmies = myGame.getPlayerTurnArmies();

                turn = Integer.toString(myGame.getTurn());

                request.setAttribute( "myGame",                  myGame );
                request.setAttribute( "territoryOwners",         territoryOwners );
                request.setAttribute( "territoryChoiceString",   territoryChoiceString );
                request.setAttribute( "territoryArmies",         territoryArmies );
                request.setAttribute( "currentTurnName",         currentTurnName );
                request.setAttribute( "currentTurnPlayerArmies", currentTurnPlayerArmies );
                request.setAttribute( "turn",                    turn );
                request.setAttribute( "attackResults",           attackResults );
                request.setAttribute( "attackerDiceResults",     attackerDiceResults) ; 
                request.setAttribute( "defenderDiceResults",     defenderDiceResults );
                request.setAttribute( "hasGameEnded",            hasGameEnded );
            }

            RequestDispatcher dispatcher =
            getServletContext().getRequestDispatcher("/list.jsp");
            dispatcher.forward(request,response);
            return;
        }
        else if( request.getServletPath().equals("/endTurn") )
        {          
            fortified = false;
            reinforced = false;
            
            for(int i = 0; i < myGame.getNumOfPlayers(); i++)
            {
                myGame.getPlayers()[i].checkAlive(); 
            }
            
            myGame.getPlayerTurnPlayer().addArmy();
            myGame.nextTurn();

            while(myGame.getPlayerTurnPlayer().isAlive() == false)
            {
                myGame.nextTurn();
            }

            territoryOwners = myGame.getTerritoryOwners();
            territoryArmies = myGame.getArmies();

            currentTurnName         = myGame.getPlayerTurn();
            currentTurnPlayerArmies = myGame.getPlayerTurnArmies();

            turn = Integer.toString(myGame.getTurn());
            
            request.setAttribute( "myGame",                  myGame );
            request.setAttribute( "territoryOwners",         territoryOwners );
            request.setAttribute( "territoryChoiceString",   territoryChoiceString );
            request.setAttribute( "territoryArmies",         territoryArmies );
            request.setAttribute( "currentTurnName",         currentTurnName );
            request.setAttribute( "currentTurnPlayerArmies", currentTurnPlayerArmies );
            request.setAttribute( "turn",                    turn );
            request.setAttribute( "attackResults",           attackResults );
            request.setAttribute( "attackerDiceResults",     attackerDiceResults) ; 
            request.setAttribute( "defenderDiceResults",     defenderDiceResults );
            request.setAttribute( "hasGameEnded",            hasGameEnded );

            RequestDispatcher dispatcher =
            getServletContext().getRequestDispatcher("/list.jsp");
            dispatcher.forward(request,response);
            return;
        }        
        
        request.setAttribute( "myGame",                  myGame );
        request.setAttribute( "territoryOwners",         territoryOwners );
        request.setAttribute( "territoryChoiceString",   territoryChoiceString );
        request.setAttribute( "territoryArmies",         territoryArmies );
        request.setAttribute( "currentTurnName",         currentTurnName );
        request.setAttribute( "currentTurnPlayerArmies", currentTurnPlayerArmies );
        request.setAttribute( "turn",                    turn );
        request.setAttribute( "attackResults",           attackResults );
        request.setAttribute( "attackerDiceResults",     attackerDiceResults) ; 
        request.setAttribute( "defenderDiceResults",     defenderDiceResults );
        request.setAttribute( "hasGameEnded",            hasGameEnded );

        RequestDispatcher dispatcher =
        getServletContext().getRequestDispatcher("/list.jsp");
        dispatcher.forward(request,response);
    }
     
     
     public boolean checkAdjacency(int a, int d){
                ArrayList<Integer> targets = new ArrayList<Integer>();
                if(a<6){
                    targets.add(a+5);
                }
                else{
                    targets.add(a-5);
                }
                if(a>15){
                    targets.add(a-5);
                }
                else{
                    targets.add(a+5);
                }
                if(a%5 == 1){
                    targets.add(a+1);
                }
                else{
                    targets.add(a-1);
                }
                if(a%5 == 0){
                    targets.add(a-1);
                }
                else{
                    targets.add(a+1);
                }

                return targets.contains(d);
            }

    public int parseEntry(String inputFromTextbox){
        int toReturn = 0;
        try{
            toReturn = Integer.parseInt(inputFromTextbox);
				if(toReturn < 0){
					toReturn = 0;
				}
        }
        catch(NumberFormatException e){
            toReturn = 0;
        }
        return toReturn;
    }
     
     
    
}