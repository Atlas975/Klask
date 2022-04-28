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

        GameArena window = guiInit(overheadStats);
        Ball []magnets=new Ball[3];
        for(int i=0; i<3; i++){
            magnets[i]=new Ball(1006,0,20, "WHITE");
            window.addBall(magnets[i]);
        }
        Ball scorePuck=new Ball(1006,600,30,"YELLOW");
        OverheadText(window,overheadStats);
        // window.addBall(scorePuck);
        int zoneConstraints[][]={{165,1006,165,1035},{1006,1835,165,1035}};

        newRound(window,overheadStats,magnets,scorePuck,zoneConstraints);
    }


    public GameArena guiInit(int[] overheadStats) {
        GameArena window=new GameArena(2000,1200);
        window.setBackground(new Color(0x2e3440));
        Rectangle gameBorder=new Rectangle(150,150,1700,900,"GREY",0);
        Rectangle gameZone=new Rectangle(165,165,1670,870,"BLUE",0);
        Ball p1goal= new Ball(250,600,90,"GREY");
        Ball p2goal= new Ball(1750,600,90,"GREY");
        Line split= new Line(1006,165,1006,1035,6,"GREY",0);
        window.addRectangle(gameBorder);
        window.addRectangle(gameZone);
        window.addBall(p1goal);
        window.addBall(p2goal);
        window.addLine(split);
        OverheadText(window, overheadStats);
        return window;
    }


    public void OverheadText(GameArena window, int[] overheadStats){
        Text welcomeText=new Text("Welcome to Klask!",30,150,80,"WHITE");
        Text roundNumber=new Text("Round "+overheadStats[0],30,950,80,"WHITE");
        Text p1Wins=new Text("W : "+overheadStats[1],30,40,1120,"WHITE");
        Text p2Wins=new Text("W : "+overheadStats[2],30,1890,1120,"WHITE");
        Text p1Score=new Text(""+overheadStats[2],40,50,610,"WHITE");
        Text p2Score=new Text(""+overheadStats[2],40,1930,610,"WHITE");
        Text timerText=new Text("Time elapsed: 0s",30,1590,80,"WHITE");
        Timer stopwatch=new Timer();
        TimerTask counter=new TimerTask(){
            int seconds=0;
            @Override
            public void run() {
                timerText.setText("Time elapsed: "+seconds+"s");
                seconds++;
            }
        };
        stopwatch.scheduleAtFixedRate(counter,0,1000);

        window.addText(roundNumber);
        window.addText(p1Wins);
        window.addText(p2Wins);
        window.addText(p1Score);
        window.addText(p2Score);
        window.addText(welcomeText);
        window.addText(timerText);
    }

    public void inputKeys(){

    }

    public void newRound(GameArena window, int[] overheadStats, Ball[] magnets, Ball scorePuck, int[][] zoneConstraints){


        resetPosition(window, overheadStats, magnets, scorePuck);
        window.repaint();
    }

    public void resetPosition(GameArena window, int[] overheadStats, Ball[] magnets, Ball scorePuck){
        int magnetPosY=850;
        for(int i=0; i<magnets.length; i++){
            magnets[i].setXPosition(1006);
            magnets[i].setYPosition(magnetPosY);
            magnetPosY-=250;
        }
        scorePuck.setXPosition(1006);
        scorePuck.setYPosition(600);
    }




}
