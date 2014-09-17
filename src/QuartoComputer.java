/*
 * QuartoComputer.java
 * Manages the different "thinkers" aka decision algorithms
 * 
 */

//package quarto;

public class QuartoComputer {
    QuartoBoard board;
    SimpleThinker simple;
    MinimaxThinker mini;
    
    boolean debug = true;
    
    public QuartoComputer( QuartoBoard qb ) {
        this.board = qb;
        this.simple = new SimpleThinker();
        this.mini = new MinimaxThinker();
    }
    
    public void setDebug( boolean status ) {
        debug = status;
        mini.setDebug(status);
    }
    
    public QNode getMove() {
        QNode result;
        int nRemSpots = board.getNumRemSpots();
        
        if (nRemSpots > 12) {
            // simple thinker
            result = simple.getMove(board);
        }
        else if (nRemSpots <= 12 && nRemSpots > 7) {
            // minimax with depth restriction
            result = mini.getMove(board, 1);
        }
        else if (nRemSpots <= 7 && nRemSpots > 1) {
			// minimax with no depth restriction
            result = mini.getMove(board, 8);
        }
        else {
            // only one play
            
            int spot = board.getRemainingSpots()[0];
            result = new QNode( spot, 16, 'C' );
            
            // QuartoConsole will check if it's Quarto
        }
            
        return result;
    }
    
}
