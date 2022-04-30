import java.awt.event.KeyEvent;

public class Player extends Ball {

    private double minX;
    private double maxX;
    private double minY=195;
    private double maxY=1005;

    public Player(int player){
        super(100,100,60,"BLACK",3);
        if(player==1){
            this.minX=195;
            this.maxX=976;
        }
        else{
            this.minX=1036;
            this.maxX=1805;
        }

        // window.addKeyListener(new KeyListener() {
        //     @Override public void keyTyped(KeyEvent e){}      //not using key typed
        //     @Override public void keyReleased(KeyEvent e){}     //not using key released
        //     @Override
        //     public void keyPressed(KeyEvent e) {
        //         processInput(e,window,player);
        //     }
        // });
    }

    public void processInput(KeyEvent e,int player){
        if(player==1){
            switch(e.getKeyCode()){
                case 65 ->{
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
        }
    }

    public void updatePos(int dx,int dy){
        double currentX=this.getXPosition();
        double currentY=this.getYPosition();
        if(dx<0 && currentX>minX){
            this.setXPosition(currentX-dx);
        }
        if(dy<0 && currentY>minY){
            this.setXPosition(currentX-dx);
        }
        if(dx>0 && (currentX<maxX)){
            this.setXPosition(currentX-dx);
        }
        if(dy>0 && (currentY<maxY)){
            this.setXPosition(currentX-dx);
        }

    }

}
