/*
 * SimpleThinker.java
 * Finds a move randomly, will play Quarto if available
 */

 //package quarto;

import java.util.Random;

public class SimpleThinker {
    public SimpleThinker() {}
    
    public QNode getMove( QuartoBoard board ) {
        QNode result;
        Random rgen = new Random();
        int[] remspots = board.getRemainingSpots();
        int winner = -1;
        boolean isQ = false;
        int randPiece;
        
        // choose spot
        for (int i=0;i<remspots.length;i++) {
            if ( testPlay( board, board.getPassedPiece(), remspots[i] ) ) {
                winner = i;
                isQ = true;
                break;
            }
        }
        if (winner == -1) 
            winner = rgen.nextInt(remspots.length);
        
        // choose piece to pass
        randPiece = board.getRemainingPieces()[ rgen.nextInt(board.getNumRemPieces()) ];
        
        // build QNode
        result = new QNode( remspots[winner], randPiece, 'C' );
        if ( isQ ) 
            result.isQuarto = true;
        
        return result;
    }
    
    public boolean testPlay( QuartoBoard boardstate, int piece, int spot ) {
        QuartoBoard cboard = (QuartoBoard)boardstate.clone();
        
        if (!cboard.play(spot))
            System.out.println( "Error testing play. (SimpleThinker)" );
        
        if (cboard.checkQuarto(spot))
            return true;
        else
            return false;
    }
    
    
}
