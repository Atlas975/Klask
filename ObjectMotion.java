import java.util.Timer;
import java.util.TimerTask;

public class ObjectMotion extends Thread{

    private GameWindow window;
    private int[] overheadStats[];
    private double[] movementArray={0,0};
    private int baseSleep=5;
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
        // double leftImpact;
        // double rightImpact;
        // double topImpact;
        // double bottomImpact;
        // leftImpact=scorePuck.getXPosition()-20;
        // rightImpact=scorePuck.getXPosition()+20;
        // topImpact=scorePuck.getYPosition()+20;
        // bottomImpact=scorePuck.getYPosition()-20;

        double xVelocity=0;
        double yVelocity=0;
        double puckRadius=scorePuck.getSize()/2;
        double playerPuckOffset=p1.radius()+puckRadius;
        double magnetPuckOffset=(magnets[0].getSize()/2)+puckRadius;
        double xPosition;
        double yPosition;

        Timer deacceleration=new Timer();
        TimerTask counter=new TimerTask(){
            int seconds=0;
            @Override
            public void run() {
                seconds++;
            }
        };

        deacceleration.scheduleAtFixedRate(counter,0,1000);


        while(true){
            xPosition=scorePuck.getXPosition();
            yPosition=scorePuck.getYPosition();

            if(checkCollide(xPosition,yPosition,p1.getXPosition(),p1.getYPosition(),playerPuckOffset)){
                System.out.println("HIT");


            }

            if(checkCollide(xPosition,yPosition,p2.getXPosition(),p2.getYPosition(),playerPuckOffset)){
                xVelocity=p2.getXVelocity();

            }




            for(int i=0; i<3; i++){
                if(checkCollide(xPosition,yPosition,magnets[i].getXPosition(),magnets[i].getYPosition(),magnetPuckOffset)){

                }

            }



            if(xPosition-puckRadius<=minX){
                xVelocity=2;
                System.out.println("X");
                continue;
            }
            if(xPosition+puckRadius>=maxX){
                xVelocity=-2;
                System.out.println("X");
                continue;
            }
            if(yPosition+puckRadius>=maxY){
                yVelocity=-2;
                continue;
            }
            if(yPosition-puckRadius<=minY){
                yVelocity=2;
                continue;
            }

        }
    }


    public void magnetMotion(){

        while(true){

        }

    }

    public boolean checkCollide(double x1, double y1, double x2, double y2, double offset){
        double vectors[]={x2-x1,y2-y1};
        double distance=Math.sqrt(Math.pow(vectors[0],2)+Math.pow(vectors[1],2))-offset;
        if(distance<=0){
            return true;
        }
        return false;

        // if(x2>x1){

        // }
    }

    public double impactAngle(double a, double b, double c){

    }

    public void magnetAttachment(Ball magnet, Player p){
        double magnetX=magnet.getXPosition()-p.getXPosition();
        double magnetY=magnet.getYPosition()-p.getYPosition();
        while(true){
            magnet.setXPosition(p.getXPosition()+magnetX);
            magnet.setYPosition(p.getYPosition()+magnetY);
        }
    }


    public int result(){
        return result;
    }



    // public void deflect(double xPosition1, double xPosition2, double yPosition1, double yPosition2, double xVelocity2, double yVelocity1, double yVelocity2){
    //     // The position and Velocity of each of the two balls in the x and y axis before collision.
    //     // YOU NEED TO FILL THESE VALUES IN AS APPROPRIATE...
    //     // Calculate initial momentum of the balls... We assume unit mass here.
    //     double p1InitialMomentum = Math.sqrt(xVelocity * xVelocity + yVelocity1 * yVelocity1);
    //     double p2InitialMomentum = Math.sqrt(xVelocity2 * xVelocity2 + yVelocity2 * yVelocity2);
    //     // calculate motion vectors
    //     double[] p1Trajectory = {xVelocity, yVelocity1};
    //     double[] p2Trajectory = {xVelocity2, yVelocity2};
    //     // Calculate Impact Vector
    //     double[] impactVector = {xPosition2 - xPosition1, yPosition2 - yPosition1};
    //     double[] impactVectorNorm = normalizeVector(impactVector);
    //     // Calculate scalar product of each trajectory and impact vector
    //     double p1dotImpact = Math.abs(p1Trajectory[0] * impactVectorNorm[0] + p1Trajectory[1] * impactVectorNorm[1]);
    //     double p2dotImpact = Math.abs(p2Trajectory[0] * impactVectorNorm[0] + p2Trajectory[1] * impactVectorNorm[1]);
    //     // Calculate the deflection vectors - the amount of energy transferred from one ball to the other in each axis
    //     double[] p1Deflect = { -impactVectorNorm[0] * p2dotImpact, -impactVectorNorm[1] * p2dotImpact };
    //     double[] p2Deflect = { impactVectorNorm[0] * p1dotImpact, impactVectorNorm[1] * p1dotImpact };
    //     // Calculate the final trajectories
    //     double[] p1FinalTrajectory = {p1Trajectory[0] + p1Deflect[0] - p2Deflect[0], p1Trajectory[1] + p1Deflect[1] - p2Deflect[1]};
    //     double[] p2FinalTrajectory = {p2Trajectory[0] + p2Deflect[0] - p1Deflect[0], p2Trajectory[1] + p2Deflect[1] - p1Deflect[1]};
    //     // Calculate the final energy in the system.
    //     double p1FinalMomentum = Math.sqrt(p1FinalTrajectory[0] * p1FinalTrajectory[0] + p1FinalTrajectory[1] * p1FinalTrajectory[1]);
    //     double p2FinalMomentum = Math.sqrt(p2FinalTrajectory[0] * p2FinalTrajectory[0] + p2FinalTrajectory[1] * p2FinalTrajectory[1]);

    //     // Scale the resultant trajectories if we've accidentally broken the laws of physics.
    //     double mag = (p1InitialMomentum + p2InitialMomentum) / (p1FinalMomentum + p2FinalMomentum);
    //     // Calculate the final x and y Velocity settings for the two balls after collision.
    //     xVelocity = p1FinalTrajectory[0] * mag;
    //     yVelocity = p1FinalTrajectory[1] * mag;
    //     xVelocity2 = p2FinalTrajectory[0] * mag;
    //     yVelocity2 = p2FinalTrajectory[1] * mag;
    //  }

    //  /**
    //  * Converts a vector into a unit vector.
    //  * Used by the deflect() method to calculate the resultnt direction after a collision.
    //  */
    // private double[] normalizeVector(double[] vec){

    //     double mag = 0.0;
    //     int dimensions = vec.length;
    //     double[] result = new double[dimensions];
    //     for (int i=0; i < dimensions; i++)
    //     mag += vec[i] * vec[i];
    //     mag = Math.sqrt(mag);
    //     if (mag == 0.0)
    //     {
    //     result[0] = 1.0;
    //     for (int i=1; i < dimensions; i++)
    //     result[i] = 0.0;
    //     }
    //     else
    //     {
    //     for (int i=0; i < dimensions; i++)
    //     result[i] = vec[i] / mag;
    //     }
    //     return result;
    // }

}



















