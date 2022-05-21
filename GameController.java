
/**
 * Controls game's core logic, manages this via GameWindow, Player and ObjectMotion classes.
 */
public class GameController{
    // This is the constructor for the GameController class. It creates a new GameWindow object,
    // then creates the magnets, the scorePuck, and the player1 / player2 balls. It then calls the
    // startGame function to begin the game.
    public GameController(int overheadStats[], int option) {
        if (option==1){ // quit option
            return;
        }

        GameWindow window = new GameWindow(overheadStats);
        Ball []magnets=new Ball[3];
        for(int i=0; i<3; i++){
            magnets[i]=new Ball(0,0,27, "WHITE",4);
            window.addBall(magnets[i]);
        }
        Ball scorePuck=new Ball(0,0,45,"YELLOW",3);
        Ball player1= new Ball(0,0,63,"BLACK",3);
        Ball player2= new Ball(0,0,63,"BLACK",3);
        window.addBall(scorePuck);
        window.addBall(player1);
        window.addBall(player2);
        startGame(window,overheadStats,magnets,scorePuck, player1,player2);
    }

    /**
     * This function is the main game loop. It runs the game until one player reaches 6 points
     *
     * @param window The GameWindow object that the game is being played in.
     * @param overheadStats an array of integers that contain the values in the text display
     * @param magnets the array of magnet ball that are on the board
     * @param scorePuck the puck that is used to score points
     * @param player1 The first player's ball
     * @param player2 The second player's ball
    */
    public void startGame(GameWindow window, int[] overheadStats, Ball[] magnets, Ball scorePuck, Ball player1,Ball player2){
        window.resetBoard(overheadStats,magnets,scorePuck,player1,player2,0);
        int winner=0;
        boolean gameRun=true;

        while(gameRun){
            for (int i=0; i<window.getKeyListeners().length; i++){
                window.removeKeyListener(window.getKeyListeners()[i]);
            }
            overheadStats=GameRound(window, magnets, scorePuck, player1, player2, overheadStats);
            window.resetBoard(overheadStats, magnets, scorePuck, player1, player2, -1);
            if(overheadStats[3]==6){
                gameRun=false;
                winner=1;
            }
            if(overheadStats[4]==6){
                gameRun=false;
                winner=2;
            }
        }
        newGame(window, overheadStats, winner);
    }


    /**
     * This function is the main game loop. It creates the players, the puck, and the magnets, and then
     * starts the threads for each of them. It then waits for one of the players to lose, and then returns
     * the overhead stats
     *
     * @param window The game window
     * @param magnets an array of 3 magnet objects
     * @param scorePuck The puck that is used to score points
     * @param player1 The first player's paddle
     * @param player2 The second player's paddle
     * @param overheadStats an array of integers that keeps track of the score, the number of rounds
     * played, and the number of rounds won by each player.
     * @return The modified overhead stats array with a players game wins incremented
     */
    public int[] GameRound(GameWindow window,Ball magnets[],Ball scorePuck,Ball player1,Ball player2, int overheadStats[]){

        int winner=0;
        Player p1=new Player(player1,1,window);
        Player p2=new Player(player2,2,window);
        ObjectMotion puckMovement=new ObjectMotion(window,scorePuck,p1,p2,magnets);
        ObjectMotion magMovement[]=new ObjectMotion[3];
        for(int i=0; i<3; i++){
            magMovement[i]=new ObjectMotion(window,i,p1,p2,magnets,scorePuck);
            magMovement[i].start();
        }
        p1.start();
        p2.start();
        puckMovement.start();

        while(true){
            System.out.print("");
            if(p1.ended()){
                winner=2;
                break;
            }
            if(p2.ended()){
                winner=1;
                break;
            }
        }

        overheadStats=roundResult(overheadStats, window, winner);
        killThreads(p1,p2,puckMovement,magMovement,scorePuck,magnets);
        window.resetBoard(overheadStats, magnets, scorePuck, player1, player2, winner);
        return overheadStats;
    }


    /**
     * Terminates the 6 threads controlling movement of objects (puck, 2 players and 3 magnets)
     *
     * @param p1 The first player's paddle
     * @param p2 The second player's paddle
     * @param puckMovement The puck's movement thread
     * @param magMovement The array of magnet movement threads
     * @param scorePuck The puck ball object, velocity is reset to 0
     * @param magnets The array of magnet ball objects, velocity is reset to 0
     */
    private void killThreads(Player p1, Player p2, ObjectMotion puckMovement, ObjectMotion[] magMovement, Ball scorePuck, Ball magnets[]) {
        p1.terminate();
        p2.terminate();
        puckMovement.terminate();
        scorePuck.setXVelocity(0);
        scorePuck.setYVelocity(0);
        for(int i=0; i<3; i++){
            magMovement[i].terminate();
            magnets[i].setXVelocity(0);
            magnets[i].setYVelocity(0);
        }
        p1.removeHandle();
        p2.removeHandle();
    }


    /**
     * This function increments the round wins for the winning player and updates game text to reflect this
     *
     * @param overheadStats an array of integers containing info on the state of the game
     * @param window The GameWindow object that the game is being played in.
     * @param winner the player who won the round
     * @return The modified overheadStats array
     */
    public int[] roundResult(int[] overheadStats, GameWindow window, int winner){
        if(winner==1){
            overheadStats[3]++;
            window.scoreIncremeent(1,overheadStats[3]);
        }
        else{
            overheadStats[4]++;
            window.scoreIncremeent(2,overheadStats[4]);
        }
        return overheadStats;
    }


    /**
     * Creates a new game
     *
     * @param window The GameWindow instance that is currently running.
     * @param overheadStats an array of integers that contains the following information:
     * @param winner the player who won the game
     */
    public void newGame(GameWindow window, int[] overheadStats, int winner){
        window.getTimerInstance().cancel();
        overheadStats[winner]+=1;
        overheadStats[0]+=1;
        overheadStats[3]=0;
        overheadStats[4]=0;
        MenuOptions options=new MenuOptions("Player "+winner+" wins!",overheadStats);
        options.dispose();
        new GameController(overheadStats,options.getOption());
        window.exit();
    }

}
