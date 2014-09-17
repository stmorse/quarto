/*
 * MinimaxThinker.java
 * Returns a move using minimax algorithm.
 * 
 */

//package quarto;

import java.util.Iterator;

public class MinimaxThinker {
    public boolean debug = true;
    
    public MinimaxThinker() {}
    
    public QNode getMove( QuartoBoard board, int depth ) {
        int     z         = board.getNumRemSpots() * board.getNumRemPieces();
        QNode[] rootnodes = new QNode[z];
        
        // build tree
        int[]   remspots  = board.getRemainingSpots();
        int[]   rempieces = board.getRemainingPieces();
        int     passedPiece = board.getPassedPiece();
        int     m = 0;
        for (int i=0;i<board.getNumRemSpots();i++) {
            for (int j=0;j<board.getNumRemPieces();j++) {
                rootnodes[m] = buildTree( passedPiece, remspots[i], rempieces[j], board, 'C', depth );
                m++;
            }
        }
        
        // display tree (debug)
        if (debug) {
            for (int k=0;k<m;k++) {
                System.out.println( " ROOT:" + displayTree( rootnodes[k] ) );
            }
        }
        
        // compute minimax
        if (debug) System.out.print( " Computing minimax: " );
        int[]   outcomes = new int[z];
        int     res = 0;
        
        for (int x=0;x<z;x++) {
            outcomes[x] = computeMiniMax(rootnodes[x]);
            
            if (debug) System.out.print( rootnodes[x].getString() + "=" + outcomes[x] + ((x<(z-1)) ? "," : " ") );
            
            // continues to upgrade, otherwise keeps res=0
            if (x>0 && outcomes[x] > outcomes[res]) {
                res = x;
            }
        }
        if (debug) System.out.println();
        
        return rootnodes[res];
    }
    
    public QNode buildTree( int passedPiece, int spot, int nextPiece, QuartoBoard boardstate, char player, int depth ) {
        QNode       node    = new QNode( spot, nextPiece, player );
        QuartoBoard cboard  = (QuartoBoard)boardstate.clone();
        
        if (!cboard.play( spot ))
            System.out.println( "Error testing play in buildTree(). (MinimaxThinker)" );
        
        if (cboard.checkQuarto(spot)) {
            node.isLeaf = true;
            node.isQuarto = true;
            return node;
        }
        
        // check if this is a leaf before we continue
        if (nextPiece==16) {
            node.isLeaf = true;
            return node;
        }
        
        if (depth == 0) {
            // we have reached desired depth
            node.isLeaf = true;
            return node;
        }
        
        // begin to build child nodes...
        
        cboard.pass( nextPiece );
        
        int[] remspots  = cboard.getRemainingSpots();
        int[] rempieces = cboard.getRemainingPieces();
        
        for (int i=0;i<cboard.getNumRemSpots();i++) {
            if (rempieces.length>0) {
                for (int j=0;j<cboard.getNumRemPieces();j++) {
                    node.addChild( buildTree( nextPiece, remspots[i], rempieces[j], cboard, (player=='C') ? 'U' : 'C' , depth-1 ) );
                }
            }
            else  // no pieces left = leaf node, use a placeholder for nextPiece
                node.addChild( buildTree( nextPiece, remspots[i], 16, cboard, (player=='C') ? 'U' : 'C', depth-1 ) );
        }
        
        return node;
    }
    
    
    private int computeMiniMax( QNode node ) {
        if (node.isLeaf) {
            if ( node.isQuarto)
                return (node.player=='C') ? 1 : -1;
            else
                return 0;
        }
        
        int tans, ans = 0;
        Iterator i = node.children.iterator();
        while (i.hasNext()) {
            tans = computeMiniMax( (QNode)i.next() );
            if ( node.player == 'C' )
                ans = Math.min( ans, tans );
            else
                ans = Math.max( ans, tans );
        }
        
        return ans;
    } 
    
    
    public String displayTree( QNode root ) {
        // returns decision tree in String form
        
        String str = "";
        
        str += root.getString();
        
        if (!root.isLeaf) {
            str += "-(";
            Iterator ci = root.children.iterator();
            while (ci.hasNext()) {
                str += displayTree( (QNode)ci.next() );
            }
            str += ")";
        }
        
        return str;
    }
    
    public void setDebug( boolean status ) {
        debug = status;
    }
}
