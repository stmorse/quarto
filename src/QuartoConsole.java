/*
 * QuartoConsole.java
 * Runs the main user interface loop on the command line
 * 
 */

//package quarto;

import java.util.Scanner;

public class QuartoConsole {
    private QuartoBoard qb;
    private QuartoComputer compy;
    private BoardUI gui;
    
    public QuartoConsole( QuartoBoard board, QuartoComputer comp ) {
        this.qb    = board;
        this.compy = comp;
        this.gui   = new BoardUI( qb );
    }
    
    
    public void go() {
        Scanner in         = new Scanner(System.in);
        String  user_input = "";
        int     phase      = 2;          
        
        // PHASE 1: user plays piece, 
        // PHASE 2: user passes piece and computer plays
        // PHASE 3: game is over
        
        System.out.println( "Debug is on. Enter h for help." );
        System.out.println( "Choose a piece to pass." );
        
        while (true) {
            System.out.print(">");
            
            try {
                user_input = in.nextLine();
            } catch (Exception e) {
                System.err.println( "Error with input:" + e );
            }
            
            user_input = user_input.trim().toLowerCase();
            
            //
            // ADMIN REQUESTS
            //
            if (user_input.equals("q") || user_input.equals("quit")) {
                System.out.println( "See ya!..." );
                break;
            }
            else if (user_input.equals("h") || user_input.equals("help") || user_input.equals("?")) {
               System.out.println( displayHelp() );
            }
            else if (user_input.startsWith("init")) {
                System.out.println( "Initializing random board..." );
                qb.initRandomBoard();
                phase = 2;
                System.out.println( "You just moved.  Choose a piece to pass. (Enter h for help.)" );
            }
            else if (user_input.startsWith("new")) {
                System.out.println( "Starting new game..." );
                qb.clearBoard();
                phase = 2;
                System.out.println( "Choose a piece to pass." );
            }
            else if (user_input.startsWith("debug")) {
                compy.setDebug(!compy.debug);
                System.out.println( "Debug is " + ((compy.debug)?"on":"off") + "." );
            }
            
            //
            // DISPLAY REQUESTS
            //
            else if (user_input.equals("db")) {
                System.out.println( gui.displayBoard() );
            }
            else if (user_input.equals("dp") ) {
                System.out.println( gui.displayRemainingPieces() );
            }
            else if (user_input.equals("ds") ) {
                System.out.println( gui.displayRemainingSpots() );
            }
            else if (user_input.equals("dc") ) {
                System.out.println( gui.displayPassedPiece() );
            }
            
            //
            // GAME INPUT: PLAY
            //
            else if (user_input.length() > 5 && user_input.startsWith("play")) {
                if (phase != 1) {
                    System.out.println( "It's not your turn to play." );
                    continue;
                }
                
                String s = user_input.split(" ")[1];    // pull string after "play "
                int spot = -1;

                try {
                    spot = Integer.parseInt(user_input.substring(5,user_input.length()).trim());
                } catch (NumberFormatException e) {
                    System.out.println( "Enter a number. (Enter ds for available spots.)" );
                    continue;
                }

                if (spot < 0 || spot > 15) {
                    System.out.println( "Choose between 0-15. (Enter ds for available spots.)" );
                    continue;
                }
                
                if ( qb.play( spot ) ) {
                    if (qb.checkQuarto( spot )) {
                        System.out.println( "Quarto! User wins." );
                        System.out.println( "New game?" );
                        phase = 3;
                    }
                    else {
                        System.out.print( "OK, piece played. " );
                        if (qb.getNumRemSpots() == 0) {
                            System.out.println( "No Quartos: you and the Computer tied. Game Over.");
                            phase = 3;
                        }
                        else {
                            System.out.println( "Choose a piece to pass." );
                            phase = 2;
                        }
                    } 
                }
                else
                    System.out.println( "Error moving piece " + qb.getPassedPiece() + " to " + spot + ". Try a different square." );

            }
            
            //
            // GAME INPUT: PASS
            //
            else if (user_input.length() > 5 && user_input.startsWith("pass")) {
                if (phase != 2) {
                    System.out.println( "It's not your turn to pass a piece." );
                    continue;
                }
                
                int user_pass = -1;
                        
                try {
                    user_pass = QPiece.getIndexFromString( user_input.substring(5,user_input.length()) );
                } catch (IllegalArgumentException iae) {
                    System.out.println( iae.getMessage() );
                    continue;
                }

                if ( qb.pass( user_pass ) ) {
                    QNode cmove = compy.getMove( );
                    
                    qb.play( cmove.spot);
                    System.out.println( "Computer plays on spot " + cmove.spot + "." );

                    if ( qb.checkQuarto( cmove.spot ) ) {
                        System.out.println( "Computer makes Quarto! at spot " + cmove.spot + ". Game over." );
                        System.out.println( "New game?" );
                        phase = 3;
                        continue;
                    }
                    
                    if (qb.getNumRemSpots() == 0) {
                        System.out.println( "No Quartos: you and the Computer tied. Game Over.");
                        System.out.println( "New game?" );
                        phase = 3;
                        continue;
                    }

                    qb.pass( cmove.passedPiece );
                    System.out.println( "Computer passes piece " + cmove.passedPiece + ":" + 
                            QPiece.getStringFromIndex(cmove.passedPiece) + " to you. Your move." );

                    // computer has moved, now in phase 1 (play)
                    phase = 1;
                }
                else
                    System.out.println( "Pick a different piece." );
            }
            
            //
            // ERROR
            //
            else {
                System.out.println( "Input not recognized.  Enter h for help." );
            }
            
        } //end while(true)
    }
    
    public String displayHelp() {
        String help = "";
        
         help += "ADMIN:   q -> quit,              new -> new game\n"
               + "         h -> help,              init -> random board\n";
         help += "DISPLAY: db -> board,            dp -> remaining pieces\n"
               + "         dc -> passed piece      ds -> remaining squares\n";
         help += "GAME:    pass XXXX -> choose pass piece, \n"
               + "         play XX -> choose square to play (00-15)\n\n";
         help += "debug -> turns debug on/off";
         
         return help;
    }
}
