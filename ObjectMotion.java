

public class ObjectMotion extends Thread{

    private int[] overheadStats[];
    private double[] movementArray={0,0};
    private int baseSleep=5;
    private Ball player1;
    private Ball player2;
    private Ball magnetMain;
    private Ball magnets[];
    private Ball scorePuck;
    private int result=0;
    private int minX=165;
    private int maxX=1835;
    private int minY=165;
    private int maxY=1035;

    // Constructor for controlling the score pucks motion
    public ObjectMotion(Ball scorePuck,Ball player1, Ball player2, Ball magnets[], Object active){
        synchronized(active){
            this.player1=player1;
            this.player2=player2;
            this.scorePuck=scorePuck;
            this.magnets=magnets;
            this.minX+=20;
            this.maxX-=20;
            this.minY-=20;
            this.maxY-=20;
            puckMotion(scorePuck,player1,player2,magnets,active);

        }
    }

    // Constructor for controlling magnet motion, the magnet in focus is stored as magnetMain
    public ObjectMotion(int pieceIndex, Ball player1, Ball player2, Ball magnets[], Ball scorePuck, Object active){
        synchronized(active){
            this.player1=player1;
            this.player2=player2;
            this.magnetMain=magnets[pieceIndex];
            Ball magnetsTemp[]=new Ball[2];
            int i=0;
            for(Ball magnet: magnets){
                if(magnet!=magnetMain){
                    magnetsTemp[i]=magnet;
                    i++;
                }
            }

            this.magnets=magnetsTemp;
        }
        // this.minX+=
    }

    public void puckMotion(Ball scorePuck,Ball player1, Ball player2, Ball magnets[], Object active){
        synchronized(active){
            try {
                Thread.sleep(baseSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public int result(){
        return result;
    }

}


















