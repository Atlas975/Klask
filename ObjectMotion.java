import java.util.Timer;
import java.util.TimerTask;


public class ObjectMotion extends Thread{

    private GameWindow window;
    private boolean terminated=false;
    private int[] overheadStats[];
    private Player p1;
    private Player p2;
    private Ball magnetMain;
    private Ball magnets[];
    private Ball scorePuck;
    private int result=0;
    private int pieceIndex=-1;
    private int minX=165;
    private int maxX=1835;
    private int minY=165;
    private int maxY=1035;

    // Constructor for controlling the score pucks motion
    public ObjectMotion(GameWindow window,Ball scorePuck,Player p1, Player p2, Ball magnets[]){
            double puckRadius= scorePuck.getSize()/2;
            this.window=window;
            this.p1=p1;
            this.p2=p2;
            this.scorePuck=scorePuck;
            this.magnets=magnets;
            this.minX+=puckRadius;
            this.maxX-=puckRadius;
            this.minY+=puckRadius;
            this.maxY-=puckRadius;
    }

    // Constructor for controlling magnet motion, the magnet in focus is stored as magnetMain
    public ObjectMotion(GameWindow window,int pieceIndex, Player p1, Player p2, Ball magnets[], Ball scorePuck){
            double magnetRadius= magnets[0].getSize()/2;
            this.window=window;
            this.p1=p1;
            this.p2=p2;
            this.pieceIndex=pieceIndex;
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
            this.minX+=magnetRadius;
            this.maxX-=magnetRadius;
            this.minY+=magnetRadius;
            this.maxY-=magnetRadius;
    }

    @Override
    public void run() {
        if(pieceIndex==-1){
            puckMotion();
        }
        else{
            magnetMotion();
        }
    }



    public void puckMotion(){


        // double xVelocity=0;
        // double yVelocity=0;
        // int magnetMass=1;
        // int puckMass=2;
        // int playerMass=3;


        double puckRadius=scorePuck.getSize()/2;

        double xPosition;
        double yPosition;


        Ball player1=p1.passObject();
        Ball player2=p2.passObject();
        double puckMoveX=0;
        double puckMoveY=0;
        double p1MoveX=0;
        double p1MoveY=0;
        double p2MoveX=0;
        double p2MoveY=0;
        double frictionForce=0.9;

        Timer friction=new Timer();
        TimerTask counter=new TimerTask(){
            public int seconds=0;
            @Override
            public void run() {
                seconds++;
            }
        };
        friction.scheduleAtFixedRate(counter,0,1000);
        boolean frictionFlag=false;

        while(frictionFlag==false){

            if(terminate()){
                frictionFlag=true;
            }

            xPosition=scorePuck.getXPosition();
            yPosition=scorePuck.getYPosition();

            // try {
            //     Thread.sleep((long) 0.2);
            //     scorePuck.setXVelocity(scorePuck.getXVelocity()*frictionForce);
            //     scorePuck.setYVelocity(scorePuck.getYVelocity()*frictionForce);

            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }
            // System.out.println("X: "+xPosition+" Y: "+yPosition);
            // System.out.print(" X: "+player1.getXPosition()+" Y: "+player1.getYPosition());
            // System.out.println(xPosition+" "+yPosition);
            if(scorePuck.collides(player1)){
                deflect(scorePuck,player1);
                p1MoveX=player1.getXPosition()+player1.getXVelocity();
                p1MoveY=player1.getYPosition()+player1.getYVelocity();
                if(p1.validateBounds(p1MoveX,p1MoveY)){
                    player1.setXPosition(p1MoveX);
                    player1.setYPosition(p1MoveY);
                }

            }

            if(scorePuck.collides(player2)){
                deflect(scorePuck, player2);
                p2MoveX=player2.getXPosition()+player2.getXVelocity();
                p2MoveY=player2.getYPosition()+player2.getYVelocity();
                if(p2.validateBounds(p2MoveX,p2MoveY)){
                    player2.setXPosition(p2MoveX);
                    player2.setYPosition(p2MoveY);
                }
            }


            puckMoveX=scorePuck.getXPosition()+scorePuck.getXVelocity();
            puckMoveY=scorePuck.getYPosition()+scorePuck.getYVelocity();

            if(puckMoveX-puckRadius<=minX){
                scorePuck.setXVelocity(-scorePuck.getXVelocity()*0.5);
            }
            else if(puckMoveX+puckRadius>=maxX){
                scorePuck.setXVelocity(-scorePuck.getXVelocity()*0.5);
            }
            if(puckMoveY+puckRadius>=maxY){
                scorePuck.setYVelocity(-scorePuck.getYVelocity()*0.5);
            }
            else if(puckMoveY-puckRadius<=minY){
                scorePuck.setYVelocity(-scorePuck.getYVelocity()*0.5);
            }

            scorePuck.setXPosition(xPosition+scorePuck.getXVelocity());
            scorePuck.setYPosition(yPosition+scorePuck.getYVelocity());

        }
        System.out.println("Reddddd: "+result);
    }





    public void magnetMotion(){
        boolean frictionFlag=false;
        while(frictionFlag==false){
            if(terminate()){
                frictionFlag=true;
            }
            if(terminate()){
                break;
            }

        }

    }



    // public void magnetAttachment(Ball magnet, Player p){
    //     double magnetX=magnet.getXPosition()-p.getXPosition();
    //     double magnetY=magnet.getYPosition()-p.getYPosition();
    //     while(true){
    //         magnet.setXPosition(p.getXPosition()+magnetX);
    //         magnet.setYPosition(p.getYPosition()+magnetY);
    //     }
    // }
    public Boolean puckBoundsInteract(double xPosition, double yPosition, double puckRadius){
        Boolean moveForward=true;
        if(xPosition+puckRadius>=maxX){
            moveForward=false;
            scorePuck.setXPosition(-xPosition/2);

            if(yPosition+puckRadius>=maxY){
                scorePuck.setYPosition(-yPosition/2);
            }
            else if(yPosition-puckRadius<=minY){
                scorePuck.setXPosition(-xPosition/2);
                scorePuck.setYPosition(-yPosition/2);
                moveForward=false;
            }
        }
        else if(xPosition-puckRadius<=minX){
            moveForward=false;
            if(yPosition+puckRadius>maxY){
                scorePuck.setXVelocity(xPosition-scorePuck.getXVelocity()/2);
                scorePuck.setYVelocity(yPosition-scorePuck.getYVelocity()/2);
                moveForward=false;
            }
            else if(yPosition-puckRadius<minY){
                scorePuck.setXVelocity(xPosition-scorePuck.getXVelocity()/2);
                scorePuck.setYVelocity(yPosition-scorePuck.getYVelocity()/2);
                moveForward=false;
            }
        }
        return moveForward;
    }


    public void deflect(Ball object1, Ball object2){
        // The position and speed of each of the two balls in the x and y axis before collision.
        // YOU NEED TO FILL THESE VALUES IN AS APPROPRIATE...
        double xPosition1=object1.getXPosition();
        double xPosition2=object2.getXPosition();
        double yPosition1=object1.getYPosition();
        double yPosition2=object2.getYPosition();
        double xSpeed1=object1.getXVelocity();
        double xSpeed2=object2.getXVelocity();
        double ySpeed1=object1.getYVelocity();
        double ySpeed2=object2.getYVelocity();
        // Calculate initial momentum of the balls... We assume unit mass here.
        double p1InitialMomentum = Math.sqrt(xSpeed1 * xSpeed1 + ySpeed1 * ySpeed1);
        double p2InitialMomentum = Math.sqrt(xSpeed2 * xSpeed2 + ySpeed2 * ySpeed2);
        // calculate motion vectors
        double[] p1Trajectory = {xSpeed1, ySpeed1};
        double[] p2Trajectory = {xSpeed2, ySpeed2};
        // Calculate Impact Vector
        double[] impactVector = {xPosition2 - xPosition1, yPosition2 - yPosition1};
        double[] impactVectorNorm = normalizeVector(impactVector);
        // Calculate scalar product of each trajectory and impact vector
        double p1dotImpact = Math.abs(p1Trajectory[0] * impactVectorNorm[0] + p1Trajectory[1] * impactVectorNorm[1]);
        double p2dotImpact = Math.abs(p2Trajectory[0] * impactVectorNorm[0] + p2Trajectory[1] * impactVectorNorm[1]);
        // Calculate the deflection vectors - the amount of energy transferred from one ball to the other in each axis
        double[] p1Deflect = { -impactVectorNorm[0] * p2dotImpact, -impactVectorNorm[1] * p2dotImpact };
        double[] p2Deflect = { impactVectorNorm[0] * p1dotImpact, impactVectorNorm[1] * p1dotImpact };
        // Calculate the final trajectories
        double[] p1FinalTrajectory = {p1Trajectory[0] + p1Deflect[0] - p2Deflect[0], p1Trajectory[1] + p1Deflect[1] - p2Deflect[1]};
        double[] p2FinalTrajectory = {p2Trajectory[0] + p2Deflect[0] - p1Deflect[0], p2Trajectory[1] + p2Deflect[1] - p1Deflect[1]};
        // Calculate the final energy in the system.
        double p1FinalMomentum = Math.sqrt(p1FinalTrajectory[0] * p1FinalTrajectory[0] + p1FinalTrajectory[1] * p1FinalTrajectory[1]);
        double p2FinalMomentum = Math.sqrt(p2FinalTrajectory[0] * p2FinalTrajectory[0] + p2FinalTrajectory[1] * p2FinalTrajectory[1]);
        // Scale the resultant trajectories if we've accidentally broken the laws of physics.
        double mag = (p1InitialMomentum + p2InitialMomentum) / (p1FinalMomentum + p2FinalMomentum);
        // Calculate the final x and y speed settings for the two balls after collision.
        object1.setXVelocity(p1FinalTrajectory[0] * mag);
        object1.setYVelocity(p1FinalTrajectory[1] * mag);
        object2.setXVelocity(p2FinalTrajectory[0] * mag);
        object2.setYVelocity(p2FinalTrajectory[1] * mag);
    }

    /**
     * Converts a vector into a unit vector.
     * Used by the deflect() method to calculate the resultnt direction after a collision.
     */
    private double[] normalizeVector(double[] vec){
        double mag = 0.0;
        int dimensions = vec.length;
        double[] result = new double[dimensions];
        for (int i=0; i < dimensions; i++)
        mag += vec[i] * vec[i];
        mag = Math.sqrt(mag);
        if (mag == 0.0)
        {
            result[0] = 1.0;
            for (int i=1; i < dimensions; i++)
            result[i] = 0.0;
        }
        else
        {
            for (int i=0; i < dimensions; i++)
            result[i] = vec[i] / mag;
        }
        return result;
    }

    public int result(){
        return result;
    }

    public void forceStop(){
        this.terminated=true;
    }

    public boolean terminate(){
        return terminated;
    }
}



















