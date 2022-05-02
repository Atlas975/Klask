import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

public class GameWindow extends GameArena{
    private Timer stopwatch;
    private Text p1Score;
    private Text p2Score;

    public GameWindow(int []overheadStats){
        super(2000,1200);
        this.setFocusable(true);
        this.setBackground(new Color(0x2e3440));
        Rectangle gameZone=new Rectangle(165,165,1670,870,"BLUE",2);
        Ball p1goal= new Ball(250,600,80,"GREY",2);
        Ball p2goal= new Ball(1750,600,80,"GREY",2);
        Line split= new Line(1006,165,1006,1035,6,"GREY",2);
        this.addRectangle(gameZone);
        this.addBall(p1goal);
        this.addBall(p2goal);
        this.addLine(split);
        gameText(overheadStats);
        scoreZones();
    }

    public void gameText(int[] overheadStats){
        Text welcomeText=new Text("Welcome to Klask!",30,150,80,"WHITE");
        Text roundNumber=new Text("Round "+overheadStats[0],30,950,80,"WHITE");
        Text p1Wins=new Text("W : "+overheadStats[1],30,40,1150,"WHITE");
        Text p2Wins=new Text("W : "+overheadStats[2],30,1890,1150,"WHITE");
        Text p1Score=new Text(""+overheadStats[3],40,50,610,"WHITE");
        Text p2Score=new Text(""+overheadStats[4],40,1930,610,"WHITE");
        this.p1Score=p1Score;
        this.p2Score=p2Score;
        Text timerText=new Text("Time elapsed: 0s",30,1585,80,"WHITE");
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
        int scorePosition[][]={{185,185},{185,1015},{1815,185},{1815,1015}};
        for(int i=0; i<4; i++){
            this.addBall(new Ball(scorePosition[i][0],scorePosition[i][1],150,"GREY",3));
            this.addBall(new Ball(scorePosition[i][0],scorePosition[i][1],120,"BLUE",3));
        }
        int overlapFill[][]={{120,120,1880,120},{1880,120,1880,1080},{1880,1080,120,1080},{120,1080,120,120}};
        double borderFill[][]={{160,160,1840,160},{1840,160,1840,1040},{1840,1040,160,1040},{160,1040,160,160}};

        for(int i=0; i<4; i++){
            this.addLine(new Line(overlapFill[i][0],overlapFill[i][1],overlapFill[i][2],overlapFill[i][3],60,"BLACK",3));
            this.addLine(new Line(borderFill[i][0],borderFill[i][1],borderFill[i][2],borderFill[i][3],20,"GREY",3));
        }
    }

    public int goalXPos(int type){
        if(type==1){
            return 250;
        }
        else{
            return 1750;
        }
    }

    public Timer getTimerInstance(){
        return stopwatch;
    }

    public int playerMinX(int type){
        if(type==1){
            return 200;
        }
        else{
            return 1025;
        }
    }

    public int playerMaxX(int type){
        if(type==1){
            return 975;
        }
        else{
            return 1800;
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

