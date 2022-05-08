public class GameController {
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
            if(puckMovement.result()>0){
                if(puckMovement.result()==1){
                    winner=1;
                    break;
                }
                if(puckMovement.result()==2){
                    winner=2;
                    break;
                }
            }
        }

        overheadStats=roundResult(overheadStats, window, magnets, scorePuck, player1, player2,winner);
        killThreads(p1,p2,puckMovement,magMovement,scorePuck,magnets);
        window.resetBoard(overheadStats, magnets, scorePuck, player1, player2, winner);
        return overheadStats;
    }


    private void killThreads(Player p1, Player p2, ObjectMotion puckMovement, ObjectMotion[] magMovement, Ball scorePuck, Ball magnets[]) {

        scorePuck.setXVelocity(0);
        scorePuck.setYVelocity(0);
        for(int i=0; i<3; i++){
            magnets[i].setXVelocity(0);
            magnets[i].setYVelocity(0);
        }

        p1.terminate();
        p2.terminate();
        puckMovement.terminate();
        for(int i=0; i<3; i++){
            magMovement[i].terminate();
        }



        System.out.println(p1.isAlive());
        System.out.println(p2.isAlive());
        System.out.println(puckMovement.isAlive());
        for(int i=0; i<3; i++){
            System.out.println(magMovement[i].isAlive());
        }

    }

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
