
public class GameController {
    public GameController(int overheadStats[], int modeParameters[]) {
        if (modeParameters[0]==2){ // quit option
            return;
        }
        GameWindow window = new GameWindow(overheadStats);
        Ball []magnets=new Ball[3];
        for(int i=0; i<3; i++){
            magnets[i]=new Ball(0,0,30, "WHITE",3);
            window.addBall(magnets[i]);
        }
        Ball scorePuck=new Ball(0,0,40,"YELLOW",3);
        Ball player1= new Ball(0,0,60,"BLACK",3);
        Ball player2= new Ball(0,0,60,"BLACK",3);
        window.addBall(scorePuck);
        window.addBall(player1);
        window.addBall(player2);
        int ballPositions[][]={{205,205},{205,995},{1800,205},{1800,995}};
        double zoneConstraints[][]={{165,1010,165,1035},{1010,1835,165,1035}};
        startGame(window,overheadStats,magnets,scorePuck,zoneConstraints,ballPositions,player1,player2,0);
    }


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


    public int[] GameRound(GameWindow window,Ball magnets[],Ball scorePuck,Ball player1,Ball player2, int overheadStats[]){

        Object pause=new Object();
        Player p1=new Player(player1,1,player2,magnets,scorePuck,window,pause);
        p1.start();
        Player p2=new Player(player2,2,player1,magnets,scorePuck,window,pause);
        p2.start();
        synchronized(pause){
            try {
                pause.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(p1.ended()){
            overheadStats[4]++;
            window.scoreIncremeent(2,overheadStats[4]);
            window.resetBoard(overheadStats, magnets, scorePuck,player1, player2, 1);
        }

        if(p2.ended()){
            overheadStats[3]++;
            window.scoreIncremeent(1,overheadStats[3]);
            window.resetBoard(overheadStats, magnets, scorePuck,player1, player2, 2);
        }
        return overheadStats;
    }

    public void newGame(GameWindow window, int[] overheadStats, int winner){
        window.getTimerInstance().cancel();
        overheadStats[winner]+=1;
        overheadStats[0]+=1;
        overheadStats[3]=0;
        overheadStats[4]=0;
        MenuOptions options=new MenuOptions("Player "+winner+"wins!",overheadStats);
        options.dispose();
        new GameController(overheadStats,options.getModeParameters());
        window.exit();
    }

}
