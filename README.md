#Battleship
A classic Battleship game contains Hotseat Mode and PVE Mode.

##Requirement

Recommand java 21 or later because I build the jar with claim v21 in pom

##Build and Run

- option1
1. Open the project in a Java IDE, such as NetBeans
2. Ensure all file(Battleship.java Board.java ComputerPlayer.java Game.java Player.java Ship.java)
   are in the same project and package
3. Run the project or run Battleship.java's main method

- option2
Use terminal or mouse to run battleship-1.0-SNAPSHOT.jar in *\battleship\target

##Play

1. Start phase
When launch the game, click the box to choose a mode (human vs human or human vs computer), 
then click START

2. Place phase
- Select a ship from the left list
- Click Vertical button to switch its orientation (vertical or horizontal)
- Click a cell on the right grid to place it, when moving your mouse, a preview of its shape
  will be shown
  attention: You can't put your ship overlap another one, if you did, there will be a pop-up
  window for warning and you have to choose another valid square
- Use Undo to withdraw your latest place, but only the latest one
- Click finish when placed all 5 ships
  in PVP mode, then its turn to your opponent's placing; in PVE mode, AI will secretly place
  its ships automatically

3. Fire phase
- Starting at player1's turn, click a cell on the opponent's board to shoot
- If its a valid fire, switch turn; if not, select another cell to try again
- The text on bottom will show the turn and this shot's result
- "O" miss
  "X" hit
  "#" sunk (when a ship sunk, the former "X" also convert to "#")
- It's an invalid fire if you shot a cell which has already been shot

4. End phase
- When one's all 5 ships were sunk, the opponent win
- There will be a pop-up window, you can choose exit the game or start a new game


##Structure

- Ship.java
  Represents a ship, including its type, size, starting position, orientation, and damage
- Board.java
  Stores ships and shot results, contains ship placement, find ship from a point, process
  shot, check all sunk
- Player.java
  The player and the player's board
- Game.java
  Dtermine winner, decide turn, show shot result
- Battleship.java
  GUI, creates interface
- ComputerPlayer.java
  A simple AI opponent, randomly place ships and fire