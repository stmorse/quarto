/*
 * QNode.java
 * Represents a player's turn as part of a "decision tree"
 * The children array holds all the possible following turns after this turn
 * 
 */
 
//package quarto;

import java.util.ArrayList;

public class QNode {
    int spot;
    int passedPiece;
    char player;
    ArrayList<QNode> children;
    boolean isLeaf;
    boolean isQuarto;
    
    public QNode( int s, int pp, char p ) {
        spot = s;
        passedPiece = pp;
        player = p;
        children = new ArrayList<QNode>();
        isLeaf = true;
        isQuarto = false;
    }
    
    public boolean addChild( QNode node ) {
        if (children.add( node )) {
            isLeaf = false;
            return true;
        }
        else {
            System.out.println( "Error adding child in QNode.addChild" );
            return false;
        }    
    }
    
    public String getString() {
        String str = "<" + spot + "," + passedPiece + "," + player;
        if (isQuarto)
            str += ",Q!";
        str += ">";
        return str;
    }
}
