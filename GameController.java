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
            magnets[i]=new Ball(1008,0,20, "WHITE",3);
            window.addBall(magnets[i]);
        }
        Ball scorePuck=new Ball(1010,600,30,"YELLOW",3);
        // overheadText(window,overheadStats);
        // window.addBall(scorePuck);
        int zoneConstraints[][]={{165,1010,165,1035},{1010,1835,165,1035}};

        newRound(window,overheadStats,magnets,scorePuck,zoneConstraints);
    }


    public GameArena guiInit(int[] overheadStats) {
        GameArena window=new GameArena(2000,1200);
        window.setBackground(new Color(0x2e3440));
        // Rectangle gameBorder=new Rectangle(150,150,1700,900,"GREY",1);
        Rectangle gameZone=new Rectangle(165,165,1670,870,"BLUE",2);
        Ball p1goal= new Ball(250,600,90,"GREY");
        Ball p2goal= new Ball(1750,600,90,"GREY");
        Line split= new Line(1006,165,1010,1035,5,"GREY",2);


        // window.addRectangle(gameBorder);
        window.addRectangle(gameZone);
        window.addBall(p1goal);
        window.addBall(p2goal);
        window.addLine(split);
        gameText(window, overheadStats);
        scoreZones(window);
        return window;
    }

    public void gameText(GameArena window, int[] overheadStats){
        Text welcomeText=new Text("Welcome to Klask!",30,150,80,"WHITE");
        Text roundNumber=new Text("Round "+overheadStats[0],30,950,80,"WHITE");
        Text p1Wins=new Text("W : "+overheadStats[1],30,40,1150,"WHITE");
        Text p2Wins=new Text("W : "+overheadStats[2],30,1890,1150,"WHITE");
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

    public void scoreZones(GameArena window){
        int scorePosition[][]={{185,185},{185,1015},{1815,185},{1815,1015}};
        for(int i=0; i<4; i++){
            window.addBall(new Ball(scorePosition[i][0],scorePosition[i][1],150,"GREY",3));
            window.addBall(new Ball(scorePosition[i][0],scorePosition[i][1],120,"BLUE",3));
        }
        int overlapFill[][]={{120,120,1880,120},{1880,120,1880,1080},{1880,1080,120,1080},{120,1080,120,120}};
        double borderFill[][]={{172.5,172.5,1827.5,172.5},{1700,150,1700,1050},{1700,1050,150,1050},{150,1050,150,150}};
        for(int i=0; i<4; i++){
            window.addLine(new Line(overlapFill[i][0],overlapFill[i][1],overlapFill[i][2],overlapFill[i][3],60,"BLACK",3));
            window.addLine(new Line(borderFill[i][0],borderFill[i][1],borderFill[i][2],borderFill[i][3],15,"GREY",3));
        }
    }


    
    public void newRound(GameArena window, int[] overheadStats, Ball[] magnets, Ball scorePuck, int[][] zoneConstraints){


        resetPosition(window, overheadStats, magnets, scorePuck);
        window.repaint();
    }

    public void resetPosition(GameArena window, int[] overheadStats, Ball[] magnets, Ball scorePuck){
        int magnetPosY=850;
        for(int i=0; i<magnets.length; i++){
            magnets[i].setXPosition(1008);
            magnets[i].setYPosition(magnetPosY);
            magnetPosY-=250;
        }
        scorePuck.setXPosition(1006);
        scorePuck.setYPosition(600);
    }




}
