

public class ObjectMotion extends Thread{

    private Ball player1;
    private Ball player2;
    private Ball magnetMain;
    private Ball magnets[];
    private Ball scorePuck;
    private boolean loss=false;
    private int minX=165;
    private int maxX;
    private int minY;
    private int maxY;

    // Constructor for controlling the score pucks motion
    public ObjectMotion(Ball scorePuck,Ball player1, Ball player2, Ball magnets[]){
        this.player1=player1;
        this.player2=player2;
        this.scorePuck=scorePuck;
        this.magnets=magnets;
    }

    // Constructor for controlling magnet motion, the magnet in focus is stored as magnetMain
    public ObjectMotion(int pieceIndex, Ball player1, Ball player2, Ball magnets[], Ball scorePuck){
        this.player1=player1;
        this.player2=player2;
        this.magnetMain=magnets[pieceIndex];
        Ball magnetsTemp[]=new Ball[2];
        int i=0;
        while(i<magnets.length){
            if(i!=pieceIndex){
                magnetsTemp[i]=magnets[i];
                i++;
            }
        }
        this.magnets=magnetsTemp;
        // this.minX+=
    }

    public void puckMotion(){

    }

}










// //  Constructor class to control the magnets motion
//     public Motion(int pieceIndex,Ball player1,Ball player2,Ball magnets[],Ball scorePuck, GameWindow window){






//     }

//     // Constructor class to control the score pucks motion
//     public Motion(Ball piece,Ball player1, Ball player2, Ball magnets[], GameWindow window){
//         this.window=window;
//         this.minX=185;
//         this.maxX=1780;
//         this.minY+=20;
//         this.maxY+=20;






        // if(player1.getXPosition()+30==piece.getXPosition()+)



        // if(player1.getXPosition()+30==piece.getXPosition() && player1.getYPosition()+30==piece.getYPosition()){
        //     this.winner=1;
        //     overheadStats[3]++;
        //     window.scoreIncremeent(1,overheadStats[3]);
        //     resetBoard(window, overheadStats, magnets, scorePuck, player1, player2, 1);
        // }








