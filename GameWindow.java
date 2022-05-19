import java.awt.Color;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameWindow extends GameArena{
    private Timer stopwatch;
    private Ball p1Goal;
    private Ball p2Goal;
    private Text p1Score;
    private Text p2Score;
    private double ballPositions[][]={{189,189},{189,891},{1611,189},{1611,891}};

    public GameWindow(int []overheadStats){
        super(1800,1080);
        this.setFocusable(true);
        this.setBackground(new Color(0x2e3440));
        Rectangle gameZone=new Rectangle(148.5,148.5,1503,783,"BLUE",2);
        Ball p1goal= new Ball(225,540,80,"GREY",2);
        Ball p2goal= new Ball(1575,540,80,"GREY",2);
        Line split= new Line(905.4,148.5,905.4,931.5,5,"GREY",2);
        this.p1Goal=p1goal;
        this.p2Goal=p2goal;
        this.addRectangle(gameZone);
        this.addBall(p1goal);
        this.addBall(p2goal);
        this.addLine(split);
        gameText(overheadStats);
        scoreZones();
    }

    public void gameText(int[] overheadStats){
        Text welcomeText=new Text("Welcome to Klask!",27,135,72,"WHITE");
        Text roundNumber=new Text("Round "+overheadStats[0],27,855,72,"WHITE");
        Text p1Wins=new Text("W : "+overheadStats[1],27,36,1035,"WHITE");
        Text p2Wins=new Text("W : "+overheadStats[2],27,1701,1035,"WHITE");
        Text p1Score=new Text(""+overheadStats[3],36,45,549,"WHITE");
        Text p2Score=new Text(""+overheadStats[4],36,1737,549,"WHITE");
        this.p1Score=p1Score;
        this.p2Score=p2Score;
        Text timerText=new Text("Time elapsed: 0s",27,1425.5,72,"WHITE");
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
        this.stopwatch=stopwatch;
        this.addText(roundNumber);
        this.addText(p1Wins);
        this.addText(p2Wins);
        this.addText(p1Score);
        this.addText(p2Score);
        this.addText(welcomeText);
        this.addText(timerText);
    }

    public void scoreZones(){
        double scorePosition[][]={{166.5,166.5},{166.5,913.5},{1633.5,166.5},{1633.5,913.5}};

        for(int i=0; i<4; i++){
            this.addBall(new Ball(scorePosition[i][0],scorePosition[i][1],135,"GREY",3));
            this.addBall(new Ball(scorePosition[i][0],scorePosition[i][1],108,"BLUE",3));
        }
        int overlapFill[][]={{108,108,1692,108},{1692,108,1692,972},{1692,972,108,972},{108,972,108,108}};
        double borderFill[][]={{144,144,1656,144},{1656,144,1656,936},{1656,936,144,936},{144,936,144,144}};

        for(int i=0; i<4; i++){
            this.addLine(new Line(overlapFill[i][0],overlapFill[i][1],overlapFill[i][2],overlapFill[i][3],54,"BLACK",3));
            this.addLine(new Line(borderFill[i][0],borderFill[i][1],borderFill[i][2],borderFill[i][3],18,"GREY",3));
        }
    }

    public double goalXPos(int type){
        if(type==1){
            return p1Goal.getXPosition();
        }
        else{
            return p2Goal.getXPosition();
        }
    }


    public Timer getTimerInstance(){
        return stopwatch;
    }

    public double playerMinX(int type){
        if(type==1){
            return 198;
        }
        else{
            return 940.5;
        }
    }

    public double playerMaxX(int type){
        if(type==1){
            return 859.5;
        }
        else{
            return 1602;
        }
    }

    public void resetBoard(int[] overheadStats, Ball[] magnets, Ball scorePuck, Ball player1,Ball player2,int startCondition){

        int position=0;
        Random locationIndex=new Random();

        switch(startCondition){
            case 1:
                position=2+locationIndex.nextInt(2);
                break;
            case 2:
                position=locationIndex.nextInt(2);
                break;
            default:
                position=locationIndex.nextInt(4);
        }

        scorePuck.setXPosition(ballPositions[position][0]);
        scorePuck.setYPosition(ballPositions[position][1]);

        int magnetPosY=765;
        for(int i=0; i<magnets.length; i++){
            magnets[i].setXPosition(907.2);
            magnets[i].setYPosition(magnetPosY);
            magnetPosY-=225;
        }

        player1.setXPosition(goalXPos(1)+225);
        player1.setYPosition(540);
        player2.setXPosition(goalXPos(2)-225);
        player2.setYPosition(540);
        for (int i=0; i<this.getKeyListeners().length; i++){
            this.removeKeyListener(this.getKeyListeners()[i]);
        }
    }

    public void scoreIncremeent(int type,int score){
        if(type==1){
            p1Score.setText(""+score);
        }
        else{
            p2Score.setText(""+score);
        }
    }


}

