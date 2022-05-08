import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Player extends Thread{
    private double minX;
    private double maxX;
    private double minY=215;
    private double maxY=980;
    private GameWindow window;
    private boolean loss=false;
    private Ball player;
    private int playerID;
    private int magnetsLatched=0;

    // Constructor class to control the player pucks motion
    public Player(Ball piece, int playerID, GameWindow window){
        this.minX=window.playerMinX(playerID);
        this.maxX=window.playerMaxX(playerID);
        this.playerID=playerID;
        this.player=piece;
        this.window=window;
    }


    @Override
    public void run() {
        if(this.isInterrupted()){
            return;
        }
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
                    break;
                }
                case 87 ->{
                    keyArray[1]=status;
                    break;

                }
                case 68 ->{
                    keyArray[2]=status;
                    break;

                }
                case 83 ->{
                    keyArray[3]=status;
                    break;

                }
            }
        }
        else{
            switch(e.getKeyCode()){
                case 37 ->{
                    keyArray[0]=status;
                    break;
                }
                case 38 ->{
                    keyArray[1]=status;
                    break;

                }
                case 39 ->{
                    keyArray[2]=status;
                    break;

                }
                case 40 ->{
                    keyArray[3]=status;
                    break;

                }
            }
        }
        return keyArray;
    }


    public void playerUpdate(boolean keyArray[]){
        double currentX=player.getXPosition();
        double currentY=player.getYPosition();

        if((keyArray[0] ^ keyArray[2]) && player.getXVelocity()<9){
            player.setXVelocity(player.getXVelocity()+3);
        }
        else{
            player.setXVelocity(5);
        }

        if((keyArray[1] ^ keyArray[3]) && player.getYVelocity()<9){
            player.setYVelocity(player.getYVelocity()+3);
        }
        else{
            player.setYVelocity(5);
        }


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

        if(goalEnter(window.goalXPos(playerID), currentX, currentY)){
            loss=true;
        }
    }

    public boolean goalEnter(double goalXPos, double ballXPos, double ballYPos){
        double distance=Math.sqrt(Math.pow(goalXPos-ballXPos,2)+Math.pow(600-ballYPos,2))-70;
        return distance < -15;
    }

    public Boolean validateBounds(double moveX, double moveY){
        if(moveX-35<=minX || moveX+35>=maxX || moveY-35<=minY || moveY+35>=maxY){
            return false;
        }
        return true;
    }


    public Ball passObject(){
        return player;
    }

    public void incrementMagnet(){
        magnetsLatched++;
        if(magnetsLatched==2){
            loss=true;
        }
    }

    public int getMagnetNum(){
        return magnetsLatched;
    }

    public void setLoss(boolean loss){
        this.loss=loss;
    }

    public boolean ended(){
        return loss;
    }

}


