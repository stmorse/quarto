/*
 * QuartoMain.java
 * Initiates the single computer and board objects, then starts console UI
 */
 
//package quarto;

public class QuartoMain {
    
    public static void main(String[] args) {
        QuartoBoard     qb      = new QuartoBoard();            // the actual board
        QuartoComputer  compy   = new QuartoComputer( qb );
        QuartoConsole   qcon    = new QuartoConsole( qb, compy );
        
        qcon.go();
        
    }
    
}
