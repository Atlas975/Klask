import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Player extends Thread{
    private double minX;
    private double maxX;
    private double minY=200;
    private double maxY=1000;
    private GameWindow window;
    private int overheadStats[];
    private boolean loss=false;

    // Constructor class to control the player pucks motion
    public Player(Ball piece, int playerID, Ball opponent,Ball magnets[],Ball scorePuck, GameWindow window, Object active){
        synchronized(active){
            this.window=window;
            this.minX=window.playerMinX(playerID);
            this.maxX=window.playerMaxX(playerID);
            window.addKeyListener(new KeyListener() {
                boolean keyArray[]=new boolean[8];
                @Override public void keyTyped(KeyEvent e){} // not used
                @Override public void keyReleased(KeyEvent e){
                    keyArray=playerInput(e,keyArray, playerID, false);
                }
                @Override public void keyPressed(KeyEvent e) {
                    keyArray=playerInput(e,keyArray, playerID, true);
                    playerUpdate(keyArray,piece,playerID,active);
                }

            });
        }


    }

    public boolean[] playerInput(KeyEvent e,boolean keyArray[], int playerID, boolean condition){
        if(playerID==1){
            switch(e.getKeyCode()){
                case 65 ->{
                    keyArray[0]=condition;
                }
                case 87 ->{
                    keyArray[1]=condition;

                }
                case 68 ->{
                    keyArray[2]=condition;

                }
                case 83 ->{
                    keyArray[3]=condition;

                }
            }
        }
        else{
            switch(e.getKeyCode()){
                case 37 ->{
                    keyArray[0]=condition;
                }
                case 38 ->{
                    keyArray[1]=condition;

                }
                case 39 ->{
                    keyArray[2]=condition;

                }
                case 40 ->{
                    keyArray[3]=condition;

                }
            }
        }
        return keyArray;
    }



    public void playerUpdate(boolean keyArray[],Ball player,int playerID, Object active){
        synchronized(active){
            double currentX=player.getXPosition();
            double currentY=player.getYPosition();

            if(keyArray[0] && currentX>minX){
                player.setXPosition(currentX-25);
            }
            if(keyArray[1] && currentY>minY){
                player.setYPosition(currentY-25);
            }
            if(keyArray[2] && (currentX<maxX)){
                player.setXPosition(currentX+25);
            }
            if(keyArray[3] && (currentY<maxY)){
                player.setYPosition(currentY+25);
            }

            if(player.getXPosition()==window.goalXPos(playerID) ){
                if(player.getYPosition()==600){
                    active.notify();
                    loss=true;
                }
            }
        }
    }

    public boolean ended(){
        return loss;
    }

    public int[] getResult(){
        return overheadStats;
    }
}


