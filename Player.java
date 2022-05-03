import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Player extends Thread{
    private double minX;
    private double maxX;
    private double minY=200;
    private double maxY=1000;
    private GameWindow window;
    private boolean loss=false;
    private Ball player;
    private int playerID;

    // Constructor class to control the player pucks motion
    public Player(Ball piece, int playerID, GameWindow window){
            this.window=window;
            this.minX=window.playerMinX(playerID);
            this.maxX=window.playerMaxX(playerID);
            this.playerID=playerID;
            this.player=piece;
    }


    @Override
    public void run() {
        window.addKeyListener(new KeyListener() {
            boolean keyArray[]=new boolean[4];
            @Override public void keyTyped(KeyEvent e){} // unused input method
            @Override public void keyReleased(KeyEvent e){
                keyArray=playerInput(e,keyArray,false);
            }
            @Override public void keyPressed(KeyEvent e) {
                keyArray=playerInput(e,keyArray,true);
                playerUpdate(keyArray);
            }
        });

    }
    public boolean[] playerInput(KeyEvent e,boolean keyArray[], boolean status){
        if(playerID==1){
            switch(e.getKeyCode()){
                case 65 ->{
                    keyArray[0]=status;
                }
                case 87 ->{
                    keyArray[1]=status;

                }
                case 68 ->{
                    keyArray[2]=status;

                }
                case 83 ->{
                    keyArray[3]=status;

                }
            }
        }
        else{
            switch(e.getKeyCode()){
                case 37 ->{
                    keyArray[0]=status;
                }
                case 38 ->{
                    keyArray[1]=status;

                }
                case 39 ->{
                    keyArray[2]=status;

                }
                case 40 ->{
                    keyArray[3]=status;

                }
            }
        }
        return keyArray;
    }


    public void playerUpdate(boolean keyArray[]){
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
                    loss=true;
                }
            }
    }

    public boolean ended(){
        return loss;
    }



}


