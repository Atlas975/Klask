
public class GameController {
    public GameController(int overheadStats[], int modeParameters[]) {
        if (modeParameters[0]==2){ // quit option
            return;
        }
        GameWindow window = new GameWindow(overheadStats);
        Ball []magnets=new Ball[3];
        for(int i=0; i<3; i++){
            magnets[i]=new Ball(0,0,25, "WHITE",3);
            window.addBall(magnets[i]);
        }
        Ball scorePuck=new Ball(0,0,35,"YELLOW",3);
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


        Boolean gameRun=true;
        // GameWindow.addKeyListener(player1,1);
        // GameWindow.addKeyListener(player2,2);
        while(gameRun){

            synchronized(this){
                overheadStats=gameRound(window, overheadStats, magnets, scorePuck, player1, player2);
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            if(overheadStats[3]==6){
                gameRun=false;
            }
            if(overheadStats[4]==6){
                gameRun=false;
            }

            resetBoard(window,overheadStats,magnets,scorePuck,ballPositions,player1,player2,startCondition);
        }

    }

    public int[] gameRound(GameWindow window,int overheadStats[],Ball magnets[],Ball scorePuck,Ball player1,Ball player2){
        Motion p1=new Motion(player1,1,player2,magnets,scorePuck,window);
        p1.start();
        Motion p2=new Motion(player2,2,player1,magnets,scorePuck,window);
        p2.start();
        // for(int j=0; j<3; j++){
        //     Motion magForce=new Motion(j,player1,player2,magnets,scorePuck,window);
        //     magForce.start();
        // }
        // Motion puck=new Motion(scorePuck,player1,player2,magnets,window);
        // puck.start();


        return overheadStats;
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

    public void newGame(GameArena window, int[] overheadStats, int winner){
        overheadStats[0]+=1;
        overheadStats[3]=0;
        overheadStats[4]=0;
        MenuOptions options=new MenuOptions("Player "+winner+"wins!",overheadStats);
        window.exit();
        options.dispose();
        new GameController(overheadStats,options.getModeParameters());
    }

}
