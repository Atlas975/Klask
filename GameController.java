import java.awt.Color;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import javax.swing.*;

public class GameController {
    public GameController(int overheadStats[]) {
        GameArena window = guiInit();
        Ball []magnets=new Ball[3];
        for(int i=0; i<3; i++){
            magnets[i]=new Ball(1006,0,20, "WHITE");
            window.addBall(magnets[i]);
        }
        Ball scorePuck=new Ball(1006,600,30,"YELLOW");
        OverheadText(window);
        // window.addBall(scorePuck);
        newRound(window,overheadStats,magnets,scorePuck);
    }


    public GameArena guiInit(){
        GameArena window=new GameArena(2000,1200);
        window.setBackground(new Color(0x2e3440));
        Rectangle gameBorder=new Rectangle(150,150,1700,900,"GREY",0);
        Rectangle gameZone=new Rectangle(165,165,1670,870,"BLUE",0);
        Ball p1goal= new Ball(250,600,90,"GREY");
        Ball p2goal= new Ball(1750,600,90,"GREY");
        Line split= new Line(1010,165,1006,1035,6,"GREY",0);
        window.addRectangle(gameBorder);
        window.addRectangle(gameZone);
        window.addBall(p1goal);
        window.addBall(p2goal);
        window.addLine(split);
        return window;
    }


    public void OverheadText(GameArena window){

        Text timerText=new Text("Time elapsed: 0s",30,1550,90,"WHITE");
        Timer stopwatch=new Timer();
        TimerTask countDown=new TimerTask(){
            int seconds=0;
            @Override
            public void run() {
                timerText.setText("Time elapsed: "+seconds+"s");
                seconds++;
            }
        };
        stopwatch.scheduleAtFixedRate(countDown,0,1000);

        window.addText(timerText);
    }

    public void newRound(GameArena window, int[] overheadStats, Ball[] magnets, Ball scorePuck){
        int magnetPosY=850;
        for(int i=0; i<magnets.length; i++){
            magnets[i].setXPosition(1006);
            magnets[i].setYPosition(magnetPosY);
            magnetPosY-=250;
        }
    }



}
