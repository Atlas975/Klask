
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerInput{
    private Ball player;
    private int playerNum;
    private GameWindow window;

    public PlayerInput(GameWindow window, Ball player,int playerNum){
        this.player = player;
        this.playerNum=playerNum;
        this.window=window;


        window.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e){
                System.out.println("butt presed");

            }      //not using key typed
            @Override public void keyReleased(KeyEvent e){
                System.out.println("butt presed");

            }     //not using key released
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("butt presed");

                processInput(e,player);
            }
        });

    }

    public void processInput(KeyEvent e,Ball player){
        if(playerNum==1){
            switch(e.getKeyCode()){
                case 65 ->{
                    System.out.println("Hello");
                    updatePos(-1,0);
                }
                case 87 ->{
                    updatePos(0,1);


                }
                case 83 ->{
                    updatePos(1,0);

                }

                case 68 ->{
                    updatePos(0,-1);
                }
            }
        }
        else{
            switch(e.getKeyCode()){
                case 65 ->{
                    System.out.println("Hello");
                    updatePos(-1,0);
                }
                case 87 ->{
                    updatePos(0,1);


                }
                case 83 ->{
                    updatePos(1,0);

                }

                case 68 ->{
                    updatePos(0,-1);
                }
            }
        }
    }

    public void updatePos(int dx,int dy){
        KeyListener retrieveListener = window.getKeyListeners()[0];
        double currentX=player.getXPosition();
        double currentY=player.getYPosition();
        if(dx<0 && currentX>window.playerMinX(playerNum)){
            player.setXPosition(currentX-dx);
        }
        if(dy<0 && currentY>window.ballMinY()){
            player.setXPosition(currentX-dx);
        }
        if(dx>0 && (currentX<window.playerMaxX(playerNum))){
            player.setXPosition(currentX-dx);
        }
        if(dy>0 && (currentY<window.ballMaxY())){
            player.setXPosition(currentX-dx);
        }
        window.removeKeyListener(retrieveListener);
        this.notify();
    }

}
