/* 
 * QPiece.java
 * Utility class for handling information of a Quarto piece 
 */

//package quarto;

import java.lang.IllegalArgumentException;

public class QPiece {
    
    // structure:
    // HEIGHT COLOR SHAPE SOLIDITY
    //   2^3   2^2   2^1   2^0
    
    // ex:
    // piece #14 = 1110 = tall, black, square, hollow
    
    public static int  HEIGHT_MASK = 8, COLOR_MASK = 4, SHAPE_MASK = 2, INSIDE_MASK = 1;
    public static int  TALL = 1, BLACK = 1, SQUARE = 1, SOLID = 1,
                SHORT = 0, WHITE = 0, CIRCLE = 0, HOLLOW = 0;
    public static int  OFF_BOARD = 0,
                IN_PLAY = 1;
    
    public static String getStringFromIndex( int index ) throws IllegalArgumentException {
        if (index==16)
            return "----";
        
        if (index<0 || index>16)
            throw new IllegalArgumentException("Piece index out of bounds.");
        
        String p = "";
        
        int height = (index & HEIGHT_MASK)>>3,
            color  = (index & COLOR_MASK)>>2,
            shape  = (index & SHAPE_MASK)>>1,
            inside = (index & INSIDE_MASK);
        
        if (height == TALL) { p += "T"; }
        else { p += "S"; }
        if (color == BLACK) { p += "B"; }
        else { p += "W"; }
        if (shape == SQUARE) { p += "Q"; }
        else { p += "C"; }
        if (inside == SOLID) { p += "D"; }
        else { p += "H"; }

        return p;
    }
    
    public static int getIndexFromString( String p ) throws IllegalArgumentException {
        p = p.toUpperCase().trim();
        
        if ( p.length() != 4 ) {
            throw new IllegalArgumentException( "The piece \"" + p + "\" isn't recognized, wrong length (" + p.length() + ")." );
        }
        
        int h, c, s, i;
        	
        if (p.substring(0,1).equals("T")) { h = TALL; }
        else { h = SHORT; }
        if (p.substring(1,2).equals("B")) { c = BLACK; }
        else { c = WHITE; }
        if (p.substring(2,3).equals("Q")) { s = SQUARE; }
        else { s = CIRCLE; }
        if (p.substring(3,4).equals("D")) { i = SOLID; }
        else { i = HOLLOW; }
        
        int index = h*8 + c*4 + s*2 + i;   // index guaranteed between 0-15

        return index;
    }
    
    public static int comparePieces( int p1, int p2, int p3, int p4 ) {
        // returns attribute that made the first found quarto
        
        int tot = 0;
        
        // loop on type
        for (int j=0;j<4;j++) {
            tot = ((p1 & (1<<j))>>j) + ((p2 & (1<<j))>>j) + ((p3 & (1<<j))>>j) + ((p4 & (1<<j))>>j) ;
            if (tot==4 || tot==0) {
                return j;
            }
            tot = 0;
        }
        
        return 4;
    }
    
    public static String getAttributeAsString( int att ) {
        if (att==0) return "solidity";
        else if (att==1) return "shape";
        else if (att==2) return "color";
        else if (att==3) return "height";
        else return "unknown";
    }
}
