/*
 * BoardUI.java
 * Provides user interface information from a QuartoBoard object
 * 
 */

//package quarto;

public class BoardUI {
    QuartoBoard qb;
    
    public BoardUI( QuartoBoard board ) {
        qb = board;
    }
    
    public String displayBoard() {
        String b = "";
        int p;
        
        for (int i=0;i<16;i++) {
            p = qb.getPieceAt(i);
            if (p==16)
                b += "-" + ((i<10)?"0":"") + i + "-" + "  ";
            else
                b += QPiece.getStringFromIndex( p ) + "  ";
           
            if ( (i+1)%4 == 0 && i!=15 ) {
                b += "\n";
            }
        }
        
        return b;
    }
    
    /*
    public String displayNumberingScheme() {
        String q = " 0   1   2   3 \n"
                +  " 4   5   6   7 \n"
                +  " 8   9   10  11 \n"
                +  " 12  13  14  15";
        
        return q;
    }
    */
    
    public String displayRemainingPieces() {
        String p = "";
        int[] rempieces = qb.getRemainingPieces();
        
        for (int i=0;i<rempieces.length;i++) {
            p += QPiece.getStringFromIndex( rempieces[i] ) + ((i<(rempieces.length-1))?", ":" ");
        }
        
        return p;
    }
    
    public String displayRemainingSpots() {
        String s = "";
        int[] remspots = qb.getRemainingSpots();
        
        for (int i=0;i<remspots.length;i++) {
            s += remspots[i] + ((i<(remspots.length-1))?", ":" ");
        }
        
        return s;
    }
    
    public String displayPassedPiece() {
        return QPiece.getStringFromIndex( qb.getPassedPiece() );
    }
    
    
}
