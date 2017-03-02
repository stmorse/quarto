quarto
======

Quarto boardgame - minimax algorithm - standalone Java app

1. The Game
2. The Code
3. The Computer

## THE GAME

4x4 blank board and 16 pieces that start off the board.  Each piece is a 
unique combination of tall/short, black/white, circle/square, hollow/solid.
For example, there is exactly one Tall-Black-sQuare-soliD piece (TBQD).

The object is to place 4 pieces in a row that have at least one 
attribute in common - then yell Quarto!

Players take turns placing pieces on the board until someone achieves the 
goal - Quarto!  

As a twist, each player selects the piece his opponent will place.  


## THE CODE

- `QuartoMain.java` (MAIN-CLASS) initiates the single QuartoComputer object and the actual QuartoBoard object (all others are clones), initiates and starts QuartoConsole
	
- `QuartoConsole.java` runs all the messy console-based user-interface with a long chain of if-else type statements in a continuous loop uses a BoardUI object to interface with the QuartoBoard object
	
- `BoardUI.java` returns information about QuartoBoard as Strings
	
- `QPiece.java` utility class for handling pieces.  Pieces in the game have four on-off attributes.  QuartoBoard stores the pieces as numbers 0 to 15, where the binary representation encodes  all the attribute information.   For example, piece 13 = 1101 = Tall-Black-Circular-soliD = TBCD.  QPiece has methods to extract and convert this information
	
- `QuartoBoard.java` contains the board information and the status of each piece (off-board, being passed, in-play) as int arrays.  Handles all gameplay with play() and pass().  Has an option to initialize a "random" board with only 5 open squares left.
	
- `QuartoComputer.java` manager of the "thinkers" aka the decision algorithms. Returns a move with increasing depth of thought as the game gets toward the end.
	
- `MinimaxThinker.java` builds a decision tree to a specified depth using QNodes, then uses the minimax algorithm to return the best available move.
	
- `SimpleThinker.java` checks for any first-play Quartos - if nothing, then randomly selects a spot to play and a piece to pass
	
- `QNode.java` building block of the computer's decision tree.  Represents one player's turn: i.e., where did he play and what piece did he pass.  Also holds all  the child nodes as an ArrayList
	
**NOTE:**  Debug mode prints the computer's decision tree and then displays the minimax
value for each root node.


## THE COMPUTER

### DECISION TREE

The computer starts the tree with X rootnodes, where `X = # remaining spots * # remaining pieces`.  Then it builds a depth-first tree.  As an example late in a game, one of the rootnodes and its "children" might look like this:

```
ROOT:<14,15,C>-(<6,6,U>-(<8,16,C>)<8,6,U,Q!>)
```

which means the computer will play whatever piece you handed him to spot 14, then pass piece 15.  We see this is not a good move for the computer, because one of its immediate children is `<8,6,U,Q!>`, i.e. the user plays piece 15 to  spot 8 and wins.

### MINIMAX ALGORITHM

The minimax algorithm works by starting at the "leaf" nodes, such as `<8,16,C>` or `<8,6,U,Q!>`, assigning a good/bad/neutral value, and working its way back up the tree, always choosing "bad" for the opposing player and "good" for itself, essentially simulating best play from both sides.  In the example, the rootnode `<14,15,C>` would have a value of -1 (bad).
