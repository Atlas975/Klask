// import java.util.Timer;
// import java.util.TimerTask;

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

        resetBoard(window,overheadStats,magnets,scorePuck,ballPositions,player1,player2,startCondition);
        int winner=0;
        boolean p1loss,p2loss,magloss,puckloss;
        boolean gameRun=true;
        Player p1=new Player(player1,1,player2,magnets,scorePuck,window);
        Player p2=new Player(player2,2,player1,magnets,scorePuck,window);
        ObjectMotion magnetMotion[]=new ObjectMotion[3];
        for(int i=0; i<3; i++){
            magnetMotion[i]=new ObjectMotion(i, player1, player2, magnets, scorePuck);
            // magnetMotion[i].start();
        }

        ObjectMotion scorePuckMotion=new ObjectMotion(scorePuck, player1, player2, magnets);
        // scorePuckMotion.start();

        while(gameRun){
            // System.out.println();
            System.out.println();

            if(p1.ended()){
                p1.reset();
                overheadStats[4]++;
                window.scoreIncremeent(2,overheadStats[4]);
                resetBoard(window, overheadStats, magnets, scorePuck, ballPositions, player1, player2, 1);
            }
            if(p2.ended()){
                p2.reset();
                overheadStats[3]++;
                window.scoreIncremeent(1,overheadStats[3]);
                resetBoard(window, overheadStats, magnets, scorePuck, ballPositions, player1, player2, 2);
            }
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


    public void resetBoard(GameWindow window, int[] overheadStats, Ball[] magnets, Ball scorePuck, int[][] ballPositions,Ball player1,Ball player2,int startCondition){

        int position;
        switch(startCondition){
            case 1:
                position=(int)(Math.random()*2);
            case 2:
                position=2+(int)(Math.random()*2);
            default:
                position=(int)(Math.random()*4);
        }
        scorePuck.setXPosition(ballPositions[position][0]);
        scorePuck.setYPosition(ballPositions[position][1]);
        int magnetPosY=850;
        for(int i=0; i<magnets.length; i++){
            magnets[i].setXPosition(1008);
            magnets[i].setYPosition(magnetPosY);
            magnetPosY-=250;
        }
        player1.setXPosition(window.goalXPos(1)+250);
        player1.setYPosition(600);
        player2.setXPosition(window.goalXPos(2)-250);
        player2.setYPosition(600);
    }

    public void newGame(GameWindow window, int[] overheadStats, int winner){
        window.getTimerInstance().cancel();
        overheadStats[winner]+=1;
        overheadStats[0]+=1;
        overheadStats[3]=0;
        overheadStats[4]=0;
        MenuOptions options=new MenuOptions("Player "+winner+"wins!",overheadStats);
        options.dispose();
        window.exit();
        new GameController(overheadStats,options.getModeParameters());
    }

}
