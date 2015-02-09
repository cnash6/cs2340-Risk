<%@ page import="edu.gatech.cs2340.risk.model.Game" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="javax.script.*" %>

<% 
   
    Game myGame = (Game) request.getAttribute("myGame");
	String currentTurnName = (String)request.getAttribute("currentTurnName");
	String currentTurnPlayerArmies = (String) request.getAttribute("currentTurnPlayerArmies");
	String territoryChoiceString = (String) request.getAttribute("territoryChoiceString");
	String[] territoryOwners = (String[]) request.getAttribute("territoryOwners");
	String turn = (String)request.getAttribute("turn");
	String[] territoryArmies = (String[]) request.getAttribute("territoryArmies");
	String[] attackResults = (String[]) request.getAttribute("attackResults");
	int[] attackerDiceResults = (int[]) request.getAttribute("attackerDiceResults");
	int[] defenderDiceResults = (int[]) request.getAttribute("defenderDiceResults");
	String hasGameEnded = (String) request.getAttribute("hasGameEnded");%>
	   

<html>
<head>
<title>RISK!!!!!!!!!</title>
</head>
<body bgcolor="#E0ECF8">

<h1 ALIGN=CENTER>RISK</h1>

<table>
<tr> 
<td><b>Enter player names. Use at least 3 players, do not skip text fields. Software will ignore illegal moves.</b></td>
</tr>
</table>

<table>
<form action="/risk/update/" method="POST">
<tr>
<td><input type="text" Id = "title1" name="title1" /></td>
<td><input type="text" Id = "title2" name="title2" /></td>
<td><input type="text" Id = "title3" name="title3" /></td>
<td><input type="text" Id = "title4" name="title4"/></td>
<td><input type="text" Id = "title5" name="title5"/></td>
<td><input type="text" Id = "title6" name="title6"/></td>
<td><input type="button" Id = "checkForErrors" value="Check For Errors" onclick="javascript: confirmation()"/><td>
<td><input type="submit" disabled Id = "startGame" value="Start Game" onclick="javascript: startTheGame()"/><td>
</form>
</tr>
</table>

<table>
<tr>
<td>

<table cellpadding="10" cellspacing="10" border="1" bgcolor="#5858FA">
<tr>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 1<br><%=territoryOwners[0]%><br><%=territoryArmies[0]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 2<br><%=territoryOwners[1]%><br><%=territoryArmies[1]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 3<br><%=territoryOwners[2]%><br><%=territoryArmies[2]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 4<br><%=territoryOwners[3]%><br><%=territoryArmies[3]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 5<br><%=territoryOwners[4]%><br><%=territoryArmies[4]%></td>

</tr>
<tr>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 6<br><%=territoryOwners[5]%><br><%=territoryArmies[5]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 7<br><%=territoryOwners[6]%><br><%=territoryArmies[6]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 8<br><%=territoryOwners[7]%><br><%=territoryArmies[7]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 9<br><%=territoryOwners[8]%><br><%=territoryArmies[8]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 10<br><%=territoryOwners[9]%><br><%=territoryArmies[9]%></td>

</tr>
<tr>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 11<br><%=territoryOwners[10]%><br><%=territoryArmies[10]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 12<br><%=territoryOwners[11]%><br><%=territoryArmies[11]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 13<br><%=territoryOwners[12]%><br><%=territoryArmies[12]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 14<br><%=territoryOwners[13]%><br><%=territoryArmies[13]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 15<br><%=territoryOwners[14]%><br><%=territoryArmies[14]%></td>

</tr>
<tr>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 16<br><%=territoryOwners[15]%><br><%=territoryArmies[15]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 17<br><%=territoryOwners[16]%><br><%=territoryArmies[16]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 18<br><%=territoryOwners[17]%><br><%=territoryArmies[17]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 19<br><%=territoryOwners[18]%><br><%=territoryArmies[18]%></td>
<td onclick="this.style.backgroundColor = get_random_color();">Territory 20<br><%=territoryOwners[19]%><br><%=territoryArmies[19]%></td>

</tr>
<tr>
</tr>
</table>
</td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>


<td>
<table>
<tr>
<td>
<form action="/risk/reinforce/" method="GET">
<table>
<tr>
<td><%= currentTurnName %>, it is your turn, you have <%=currentTurnPlayerArmies%> armies<td>
<td><input type="submit" Id = "addReinforcement" value="Add Reinforcement" onclick="placeArmy()"/><td>
</tr>
</table>
</td>

<tr>
<td>
<table>
<tr>
<td>Select a territory to reinforce:<td>
<td><select name="territoriesDropDown">
<option value="0">1</option>
<option value="1">2</option>
<option value="2">3</option>
<option value="3">4</option>
<option value="4">5</option>
<option value="5">6</option>
<option value="6">7</option>
<option value="7">8</option>
<option value="8">9</option>
<option value="9">10</option>
<option value="10">11</option>
<option value="11">12</option>
<option value="12">13</option>
<option value="13">14</option>
<option value="14">15</option>
<option value="15">16</option>
<option value="16">17</option>
<option value="17">18</option>
<option value="18">19</option>
<option value="19">20</option>
</select><td>
</tr>
</table>
</form>
</tr>
</table>










<form action="/risk/attack/" method="GET">
<table style="border:3px solid black;">
<tr>
<td></td>
<td ALIGN=CENTER><b>Attacking/Defending</b></td>
<td></td>
</tr>

<tr>
<td>Attacking Territory:</td>
<td><select name="attackingTerritoryDropDown">
<option value="0">1</option>
<option value="1">2</option>
<option value="2">3</option>
<option value="3">4</option>
<option value="4">5</option>
<option value="5">6</option>
<option value="6">7</option>
<option value="7">8</option>
<option value="8">9</option>
<option value="9">10</option>
<option value="10">11</option>
<option value="11">12</option>
<option value="12">13</option>
<option value="13">14</option>
<option value="14">15</option>
<option value="15">16</option>
<option value="16">17</option>
<option value="17">18</option>
<option value="18">19</option>
<option value="19">20</option>
</select></td>

<td>Defending territory:</td>
<td><select name="defendingTerritoryDropDown">
<option value="0">1</option>
<option value="1">2</option>
<option value="2">3</option>
<option value="3">4</option>
<option value="4">5</option>
<option value="5">6</option>
<option value="6">7</option>
<option value="7">8</option>
<option value="8">9</option>
<option value="9">10</option>
<option value="10">11</option>
<option value="11">12</option>
<option value="12">13</option>
<option value="13">14</option>
<option value="14">15</option>
<option value="15">16</option>
<option value="16">17</option>
<option value="17">18</option>
<option value="18">19</option>
<option value="19">20</option>
</select></td>
</tr>

<tr></tr>
<tr></tr>

<tr>
<td></td>
<td ALIGN=CENTER><b>Select the number of dice</b></td>
<td></td>
</tr>

<tr>
<td>Attacker:</td>
<td><select name="attackDiceDropDown">
<option value="1">1</option>
<option value="2">2</option>
<option value="3">3</option>
</select></td>
<td> Defender:</td>
<td><select name="defendDiceDropDown">
<option value="1">1</option>
<option value="2">2</option>
</select></td>
</tr>

<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>

<tr>
<td></td>
<td ALIGN=CENTER><input type="submit" Id = "rollDice" value="Roll Dice" ></td>
<td></td>
</tr>

<tr>
<td></td>
<td ALIGN=CENTER><b>Results</b></td>
<td></td>
</tr>


<tr>
<td ALIGN=CENTER>Attacker</td>
<td ALIGN=CENTER> - </td>
<td ALIGN=CENTER >Defender</td>
</tr>
<tr>
<td ALIGN=CENTER><%=attackerDiceResults[0]%></td> <td ALIGN=CENTER><%=attackResults[0]%></td> <td ALIGN=CENTER><%=defenderDiceResults[0]%></td>
</tr>
<tr>
<td ALIGN=CENTER><%=attackerDiceResults[1]%></td> <td ALIGN=CENTER><%=attackResults[1]%></td> <td ALIGN=CENTER><%=defenderDiceResults[1] %></td>
</tr>
<tr>
<td ALIGN=CENTER><%=attackerDiceResults[2] %></td> <td ALIGN=CENTER></td> <td></td>
</tr>
</form>
</table>






<tr>

<td>
<form action="/risk/auto/" method="GET">
<table>
<tr>
<td><input type="submit" Id = "autoAssign" value="Auto Assign Armies" onclick="autoAssign()"/><td>
</tr>
</table>
</form>
</td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>



<td>
<form action="/risk/fortify/" method="GET">
<table style="border:3px solid black;">
<tr>
<td></td>
<td></td>
<td></td>
<td ALIGN=CENTER><b>Fortifying</b></td>
<td></td>
<td></td>
<td></td>
</tr>
<tr>
<td>From Territory:</td>
<td><select name="fortifyingFromTerritoryDropDown">
<option value="0">1</option>
<option value="1">2</option>
<option value="2">3</option>
<option value="3">4</option>
<option value="4">5</option>
<option value="5">6</option>
<option value="6">7</option>
<option value="7">8</option>
<option value="8">9</option>
<option value="9">10</option>
<option value="10">11</option>
<option value="11">12</option>
<option value="12">13</option>
<option value="13">14</option>
<option value="14">15</option>
<option value="15">16</option>
<option value="16">17</option>
<option value="17">18</option>
<option value="18">19</option>
<option value="19">20</option>
</select></td>
<td>To Territory:</td>
<td><select name="fortifyingToTerritoryDropDown">
<option value="0">1</option>
<option value="1">2</option>
<option value="2">3</option>
<option value="3">4</option>
<option value="4">5</option>
<option value="5">6</option>
<option value="6">7</option>
<option value="7">8</option>
<option value="8">9</option>
<option value="9">10</option>
<option value="10">11</option>
<option value="11">12</option>
<option value="12">13</option>
<option value="13">14</option>
<option value="14">15</option>
<option value="15">16</option>
<option value="16">17</option>
<option value="17">18</option>
<option value="18">19</option>
<option value="19">20</option>
</select></td>
<td>Number of Armies:</td>
<td><input type="text" name="numOfArmiesToFortify" value=""></td>
<td><input type="submit" Id = "fortifyTerritories" value="Fortify" ></td>
<td></td>
</tr>
</table>
</form>

<table>
<form action="/risk/endTurn/" method="GET">
<tr>
<td><input type="button" Id = "continueTurn" value="continue turn" onclick="javascript: checkThatArmyHasBeenPlaced()"/><td>
<td><input type="submit" disabled Id = "endTurn" value="End Turn"/></td>
</tr>
<tr>
<td></td>
</tr>
<tr>
<td></td>
</tr>
<tr>
<td></td>
</tr>

</form>
</table>

</td>


</tr>

<form action="/risk/wintest/" method="GET">
<tr>
<td><input type="submit"  Id = "wintest" value="wintest" /></td>
</tr>
</form>






<script type="text/javascript">
<!--
function confirmation() {
	var numPlayers = 0;
	var p1 = document.getElementById("title1").value;
	var p2 = document.getElementById("title2").value;
	var p3 = document.getElementById("title3").value;
	var p4 = document.getElementById("title4").value;
	var p5 = document.getElementById("title5").value;
	var p6 = document.getElementById("title6").value;
	
	if(p1 != "" && p2 != "" && p3 != ""){
		numPlayers = 3;
	}
	
	if(numPlayers == 3){
		if(p4 != ""){
			numPlayers = 4;
		}
	}
	
	if(numPlayers == 4){
		if(p5 != ""){
			numPlayers = 5;
		}
	}
	
	if(numPlayers == 5){
		if(p6 != ""){
			numPlayers = 6;
		}
	}

	if(numPlayers > 2){
		document.getElementById("startGame").disabled=false;
	}
	
	else{
		alert("You made a mistake entering player information. Please try again.")
	}
}

</script>

<script type="text/javascript">
<!--
function checkThatArmyHasBeenPlaced() {
	if(<%= currentTurnPlayerArmies %> == "0")
	{
		document.getElementById("endTurn").disabled=false;
	}
	else
	{
		alert("You must place your army before attacking or ending turn.")
	}
}

</script>






<script type="text/javascript">
<!--

function startTheGame(){
	document.getElementById("autoAssign").disabled=false;
}

</script>

<script type="text/javascript">

    var hasGameEnded = "<%= hasGameEnded %>";
    if(hasGameEnded == "yes")
    {
	    var name = "<%= currentTurnName %>";
	    alert("The game has ended! " + name + " won the game! To play another game, enter the player names, check your entries for errors, and click 'Start Game'");
	    document.getElementById("continueTurn").disabled=true;
	    document.getElementById("rollDice").disabled=true;
	    document.getElementById("fortifyTerritories").disabled=true;
	    document.getElementById("addReinforcement").disabled=true;
	    document.getElementById("autoAssign").disabled=true;
	    document.getElementById("wintest").disabled=true;
    }


</script>





</body>
</html>