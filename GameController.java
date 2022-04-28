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
            magnets[i]=new Ball(1008,0,20, "WHITE",3);
            window.addBall(magnets[i]);
        }
        Ball scorePuck=new Ball(1010,600,30,"YELLOW",3);
        int zoneConstraints[][]={{165,1010,165,1035},{1010,1835,165,1035}};
        int ballPositions[][]={{205,205},{205,995},{1800,205},{1800,995}};
        newRound(window,overheadStats,magnets,scorePuck,zoneConstraints,ballPositions);
    }





    public void newRound(GameArena window, int[] overheadStats, Ball[] magnets, Ball scorePuck, int[][] zoneConstraints, int[][] ballPositions){

        int position=(int)(Math.random()*4);;
        scorePuck.setXPosition(ballPositions[position][0]);
        scorePuck.setYPosition(ballPositions[position][1]);
        window.addBall(scorePuck);






        position=(int)(Math.random()*2);
        resetPosition(window, overheadStats, magnets, scorePuck);
        window.repaint();
    }



    public void resetPosition(GameArena window, int[] overheadStats, Ball[] magnets, Ball scorePuck,position){
        int magnetPosY=850;
        for(int i=0; i<magnets.length; i++){
            magnets[i].setXPosition(1008);
            magnets[i].setYPosition(magnetPosY);
            magnetPosY-=250;
        }
        // scorePuck.setXPosition(1006);
        // scorePuck.setYPosition(600);
    }




}
