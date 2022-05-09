
public class GameController{
    // This is the constructor for the GameController class. It creates a new GameWindow object, and
    // then creates the magnets, the scorePuck, and the player1 and player2 balls. It then calls the
    // startGame function.
    public GameController(int overheadStats[], int modeParameters[]) {
        if (modeParameters[0]==2){ // quit option
            return;
        }


        GameWindow window = new GameWindow(overheadStats);
        Ball []magnets=new Ball[3];
        for(int i=0; i<3; i++){
            magnets[i]=new Ball(0,0,30, "WHITE",4);
            window.addBall(magnets[i]);
        }
        Ball scorePuck=new Ball(0,0,50,"YELLOW",3);
        Ball player1= new Ball(0,0,70,"BLACK",3);
        Ball player2= new Ball(0,0,70,"BLACK",3);
        window.addBall(scorePuck);
        window.addBall(player1);
        window.addBall(player2);
        int ballPositions[][]={{205,205},{205,995},{1800,205},{1800,995}};
        double zoneConstraints[][]={{165,1010,165,1035},{1010,1835,165,1035}};
        startGame(window,overheadStats,magnets,scorePuck,zoneConstraints,ballPositions,player1,player2,0);
    }


/**
 * This function is the main game loop. It runs the game until one player reaches 6 points
 *
 * @param window The GameWindow object that the game is being played in.
 * @param overheadStats an array of integers that contains the following information:
 * @param magnets the array of magnets that are on the board
 * @param scorePuck the puck that is used to score points
 * @param zoneConstraints This is a 2D array that contains the x and y coordinates of the top left and
 * bottom right corners of the zone.
 * @param ballPositions The positions of the balls on the board.
 * @param player1 The first player's ball
 * @param player2 The second player's ball
 * @param startCondition 0 for normal, 1 for player 1, 2 for player 2
 */
    public void startGame(GameWindow window, int[] overheadStats, Ball[] magnets, Ball scorePuck, double[][] zoneConstraints, int[][] ballPositions,Ball player1,Ball player2,int startCondition){
        window.resetBoard(overheadStats,magnets,scorePuck,player1,player2,startCondition);
        int winner=0;
        boolean gameRun=true;

        while(gameRun){
            for (int i=0; i<window.getKeyListeners().length; i++){
                window.removeKeyListener(window.getKeyListeners()[i]);
            }
            overheadStats=GameRound(window, magnets, scorePuck, player1, player2, overheadStats);
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
 * @param magnets an array of 3 magnets
 * @param scorePuck The puck that is used to score points
 * @param player1 The first player's paddle
 * @param player2 The second player's paddle
 * @param overheadStats an array of integers that keeps track of the score, the number of rounds
 * played, and the number of rounds won by each player.
 * @return The overheadStats array is being returned.
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

        overheadStats=roundResult(overheadStats, window, magnets, scorePuck, player1, player2,winner);
        killThreads(p1,p2,puckMovement,magMovement,scorePuck,magnets);
        window.resetBoard(overheadStats, magnets, scorePuck, player1, player2, winner);
        return overheadStats;
    }


    private void killThreads(Player p1, Player p2, ObjectMotion puckMovement, ObjectMotion[] magMovement, Ball scorePuck, Ball magnets[]) {
        p1.terminate();
        p2.terminate();
        scorePuck.setXVelocity(0);
        scorePuck.setYVelocity(0);
        puckMovement.terminate();
        puckMovement.interrupt();
        for(int i=0; i<3; i++){
            magnets[i].setXVelocity(0);
            magnets[i].setYVelocity(0);
            magMovement[i].terminate();
            magMovement[i].interrupt();
        }
    }


/**
 * This function takes in the overheadStats array, the GameWindow, the magnets array, the scorePuck,
 * the player1 and player2 balls, and the winner of the round. It then increments the winner's score
 * and returns the overheadStats array
 *
 * @param overheadStats an array of integers that contains the following information:
 * @param window The GameWindow object that the game is being played in.
 * @param magnets An array of all the magnets in the game.
 * @param scorePuck The puck that is used to keep track of the score.
 * @param player1 The first player's ball
 * @param player2 The second player's ball
 * @param winner the player who won the round
 * @return The overheadStats array is being returned.
 */
    public int[] roundResult(int[] overheadStats, GameWindow window, Ball magnets[], Ball scorePuck, Ball player1, Ball player2, int winner){
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


    public void newGame(GameWindow window, int[] overheadStats, int winner){
        window.getTimerInstance().cancel();
        overheadStats[winner]+=1;
        overheadStats[0]+=1;
        overheadStats[3]=0;
        overheadStats[4]=0;
        MenuOptions options=new MenuOptions("Player "+winner+" wins!",overheadStats);
        options.dispose();
        new GameController(overheadStats,options.getModeParameters());
        window.exit();
    }

}
