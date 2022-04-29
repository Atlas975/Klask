import java.awt.Color;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import javax.swing.*;

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
        Player player1= new Player(1);
        Player player2= new Player(2);
        window.addBall(scorePuck);
        window.addBall(player1);
        window.addBall(player2);

        int ballPositions[][]={{205,205},{205,995},{1800,205},{1800,995}};
        double zoneConstraints[][]={{165,1010,165,1035},{1010,1835,165,1035}};
        startGame(window,overheadStats,magnets,scorePuck,zoneConstraints,ballPositions,player1,player2,0);
    }


    public void startGame(GameWindow window, int[] overheadStats, Ball[] magnets, Ball scorePuck, double[][] zoneConstraints, int[][] ballPositions,Player player1,Player player2,int startCondition){

        resetBoard(window,overheadStats,magnets,scorePuck,ballPositions,player1,player2,startCondition);
        // for(int i=0; i<6; i++){

        //     if(overheadStats[3]==5){
        //         overheadStats[1]+=1;
        //         newGame(window, overheadStats,1);
        //     }
        //     if(overheadStats[4]==5){
        //         overheadStats[2]+=1;
        //         newGame(window, overheadStats,2);
        //     }

        // }
        // movementControl(magnets, scorePuck,zoneConstraints);



        // newGame(window, overheadStats, 1);

    }

    public void movementControl(Ball[] magnets, Ball scorePuck, double[][] zoneConstraints){

    }



    public void resetBoard(GameWindow window, int[] overheadStats, Ball[] magnets, Ball scorePuck, int[][] ballPositions,Player player1,Player player2,int startCondition){

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
        player1.setXPosition(window.goalXPos(0)+200);
        player1.setYPosition(600);
        player2.setXPosition(window.goalXPos(1)-200);
        player2.setYPosition(600);

    }

    public void newGame(GameArena window, int[] overheadStats, int winner){
        overheadStats[0]+=1;
        MenuOptions options=new MenuOptions("Player "+winner+"wins!",overheadStats);
        window.exit();
        options.dispose();
        new GameController(overheadStats,options.getModeParameters());
    }

}
