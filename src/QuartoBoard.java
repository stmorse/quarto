/*
 * QuartoBoard.java
 * Manages the board and pieces
 * 
 */
 
//package quarto;

import java.util.Arrays;

public class QuartoBoard implements Cloneable {
    
    private int[] board = { 16, 16, 16, 16,
                            16, 16, 16, 16,
                            16, 16, 16, 16,
                            16, 16, 16, 16};
   
    private int[] pstat = new int[16];      // piece status, initially all zero (OFF_BOARD)
    
    private int nRemSpots = 16;
    private int nRemPieces = 16;
    private int passedPiece = 16;
    
    public static final int OFF_BOARD=0, IN_LIMBO=1, IN_PLAY=2;   // codes for pstat
    
    public QuartoBoard() {}
    
    @Override
    public Object clone() {
        // deep copy clone
        
        QuartoBoard c = null;
        
        try {
            c = (QuartoBoard)super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println( "QuartoBoard not cloneable!" );
        }
        
        c.board = board.clone();
        c.pstat = pstat.clone();
        
        return c;
    }
    
    /*
     * 
     *  GAME PLAY METHODS
     * 
     */
    
    public boolean pass( int piece ) {
        // sets the passed piece
        
        if (piece<0 || piece>15) {
            System.out.println( "Error: this piece doesn't exist." );
            return false;
        }
        
        if ( pstat[piece] == OFF_BOARD ) {
            passedPiece = piece;
            pstat[piece] = IN_LIMBO;      // put this piece in limbo
            nRemPieces--;
            
            return true;
        }
        else {
            System.out.println( "Error passing " + piece + ". Piece is " + ((pstat[piece]==IN_LIMBO) ? "in-limbo" : "in-play") + "." );
            return false;
        }
    }
    
    public boolean play( int spot ) {
        // sets the board at spot to the current passed piece
        
        if (spot < 16 && spot >= 0) {
            if (board[spot] != 16) {
                // this spot not empty
                System.out.println( "Error: Spot " + spot + " not empty (contains piece " + board[spot] + ").");
                return false;
            }
            else if (pstat[passedPiece] == OFF_BOARD || pstat[passedPiece] == IN_PLAY) {
                // problem with the passedPiece
                System.out.println( "Error: Piece " + passedPiece + " status is " + ((pstat[passedPiece]==OFF_BOARD) ? "off-board" : "in-play") + "." );
                return false;
            }
            else {
                board[spot] = passedPiece;  
                pstat[passedPiece] = IN_PLAY;
                nRemSpots--;                
                passedPiece = 16;           // no piece in limbo anymore, for the moment
                
                return true;
            }     
        }
        else {
            System.out.println( "Bad arg passed to move(): " + spot + " out of bounds." );
            return false;
        }
    }
    
    /*
     * 
     * checkQuarto method (fairly optimized)
     * 
     */
    
    public boolean checkQuarto( int spot ) {
        boolean checkRow = true;
        boolean checkCol = true;
        
        int rowhead = spot - (spot%4);
        int colhead = spot%4;
        
        int r1 = board[rowhead],   r2 = board[rowhead+1], 
            r3 = board[rowhead+2], r4 = board[rowhead+3];
        int c1 = board[colhead],   c2 = board[colhead+4],
            c3 = board[colhead+8], c4 = board[colhead+12];
        
        // check for empty spots in row or col
        if ( r1==16 || r2==16 ||
             r3==16 || r4==16 ) {
            checkRow = false;
        } 
        if ( c1==16 || c2==16 ||
             c3==16 || c4==16 ) {
            checkCol = false;
        }
        if ( !checkRow && !checkCol ) {
            return false;
        }
        
        // no empty spots, start comparing pieces in row and col
        int result;
        
        // check rows
        if (checkRow) {
            result = QPiece.comparePieces(r1,r2,r3,r4);
            if (result < 4)
                return true;
        }
      
        // check columns
        if (checkCol) {
            result = QPiece.comparePieces(c1,c2,c3,c4);
            if (result < 4)
                return true;
        }    
        
        // nothing found
        return false;
    }
    
    
    /*
     * 
     * "GET" METHODS
     * 
     * 
     */
    
    public int[] getRemainingSpots() {
        int[] rs = new int[nRemSpots];
       
        int m = 0;
        for (int i=0;i<16;i++) {      
            if (board[i] == 16) {
                rs[m] = i;
                m++;
            }
        }
        
        return rs;
    }
    
    public int[] getRemainingPieces() {
        int[] rp = new int[nRemPieces];
          
        int m = 0;
        for (int i=0;i<16;i++) {
            if (pstat[i] == OFF_BOARD) {
                rp[m] = i;
                m++;
            }
        }
        
        return rp;
    }
    
    public int getPassedPiece() {
        return passedPiece;
    }
    
    public int getNumRemSpots() {
        return nRemSpots;
    }
    
    public int getNumRemPieces() {
        return nRemPieces;
    }
    
    public int getPieceAt( int spot ) {
        return board[spot];
    }
    
    public int getPieceStatus( int piece ) {
        return pstat[piece];
    }
   
    public void clearBoard() {
        Arrays.fill( board, 16 );   // 16 -> empty
        Arrays.fill( pstat, OFF_BOARD );    
        nRemSpots = 16;
        nRemPieces = 16;
        passedPiece = 16;
    }
    
    public void initRandomBoard() {
        // init random board and update pmat and counters
        // user has just played piece 5 to spot 4, and must choose a piece to pass
        
        // random board (with no quartos!)
        int[] randboard = { 12, 16, 3, 16,
                            5,  8, 16, 9,
                            16, 7, 1, 13,
                            0, 11, 16, 4  };
        System.arraycopy( randboard, 0, board, 0, 16 );
        
        // update pstat - all pieces in play except 2, 10, 14, 15
        for (int i=0;i<16;i++) {
            pstat[i] = IN_PLAY;
        }
        pstat[2] = 0;
        pstat[6] = 0;
        pstat[10] = 0;
        pstat[14] = 0;
        pstat[15] = 0;
        
        // update counters
        nRemSpots = 5;
        nRemPieces = 5;
        
        // user has not passed yet
        passedPiece = 16;
    }
}
