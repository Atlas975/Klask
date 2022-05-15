import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.event.KeyListener;

public class Player extends Thread{
    private Boolean stop=false;
    private double minX;
    private double maxX;
    private double minY=215;
    private double maxY=980;
    private GameWindow window;
    private boolean loss=false;
    private Ball player;
    private int playerID;
    private int magnetsLatched=0;
    private Rectangle handle;

    // A synchronized method that allows the thread to be terminated.
    public synchronized void terminate(){
        this.stop=true;
    }

    public synchronized Boolean roundOver(){
        return this.stop;
    }

    // Constructor class to control the player pucks motion
    public Player(Ball piece, int playerID, GameWindow window){
        this.minX=window.playerMinX(playerID);
        this.maxX=window.playerMaxX(playerID);
        this.playerID=playerID;
        this.player=piece;
        this.window=window;
        this.handle=new Rectangle(piece.getXPosition()-15, piece.getYPosition()-80, 30, 80,"BLACK",4);
        // window.addRectangle(this.handle);
    }

    /**
     * The run function adds a key listener to the window, which updates the player's position based on
     * the keys pressed
     */
    @Override
    public void run() {
        if(roundOver()){
            return;
        }
        window.addKeyListener(new KeyListener() {
            boolean keyArray[]=new boolean[4];
            @Override public void keyTyped(KeyEvent e){} // unused input method
            @Override public void keyReleased(KeyEvent e){
                keyArray=playerInput(e,keyArray,false);
                playerUpdate(keyArray);

            }
            @Override public void keyPressed(KeyEvent e) {
                keyArray=playerInput(e,keyArray,true);
                playerUpdate(keyArray);
            }
        });
    }

    /**
     * This function takes in a key event, a boolean array, and a boolean status. It then checks the
     * key code of the key event and sets the corresponding index of the boolean array to the boolean
     * status
     *
     * @param e The KeyEvent that is passed in from the KeyListener
     * @param keyArray The array that stores the keys on/off status.
     * @param status true if the key is pressed, false if the key is released
     * @return The keyArray after the key is set to the status
     */
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

    /**
     * If the player is pressing a key, and the player is not at the edge of the screen, then move the
     * player in the direction of the key
     *
     * @param keyArray An array of booleans that represent the keys that are pressed.
     */
    public void playerUpdate(boolean keyArray[]){
        double currentX=player.getXPosition();
        double currentY=player.getYPosition();
        // double handleX=handle.getXPosition();
        // double handleY=handle.getYPosition();

        if((keyArray[0] ^ keyArray[2]) && player.getXVelocity()<12){
            player.setXVelocity(player.getXVelocity()+4);
        }
        else{
            player.setXVelocity(0);
        }

        if((keyArray[1] ^ keyArray[3]) && player.getYVelocity()<12){
            player.setYVelocity(player.getYVelocity()+4);
        }
        else{
            player.setYVelocity(0);
        }


        if(keyArray[0] && currentX>minX){
            player.setXPosition(currentX-25);
            // handle.setXPosition(currentX-25);
        }
        if(keyArray[1] && currentY>minY){
            player.setYPosition(currentY-25);
            // handle.setYPosition(currentY-25);
        }
        if(keyArray[2] && (currentX<maxX)){
            player.setXPosition(currentX+25);
            // handle.setXPosition(currentX+25);
        }
        if(keyArray[3] && (currentY<maxY)){
            player.setYPosition(currentY+25);
            // handle.setYPosition(currentY+25);
        }

        if(goalEnter(window.goalXPos(playerID), currentX, currentY)){
            loss=true;
        }
    }

    /**
     * If the distance between the ball and the goal is less than 15, then the ball has entered the
     * goal
     *
     * @param goalXPos The x position of the goal
     * @param ballXPos The x position of the ball
     * @param ballYPos The y position of the ball
     * @return A boolean value.
     */
    public boolean goalEnter(double goalXPos, double ballXPos, double ballYPos){
        double distance=Math.sqrt(Math.pow(goalXPos-ballXPos,2)+Math.pow(600-ballYPos,2))-70;
        return distance < -15;
    }

    /**
     * If the player's position is within the bounds of the screen, return true. Otherwise, return
     * false
     *
     * @param moveX The x coordinate of the center of the circle
     * @param moveY The y coordinate of the center of the circle
     * @return A boolean value.
     */
    public Boolean validateBounds(double moveX, double moveY){
        if(moveX-35<=minX || moveX+35>=maxX || moveY-35<=minY || moveY+35>=maxY){
            return false;
        }
        return true;
    }



    /**
     * Passes the ball object
     *
     * @return The player object used in this thread
     */
    public Ball passObject(){
        return player;
    }

    /**
     * If the number of magnets latched is equal to 2, then the player has lost
     */
    public void incrementMagnet(){
        magnetsLatched++;
        if(magnetsLatched==2){
            loss=true;
        }
    }

    /**
     * This function sets the loss variable to the value of the loss parameter.
     *
     * @param loss If the player has lost the game.
     */
    public void setLoss(boolean loss){
        this.loss=loss;
    }

    /**
     * This function returns a boolean value that is true if the game has ended and false otherswise.
     *
     * @return the active status of the game
     */
    public boolean ended(){
        return loss;
    }

}


