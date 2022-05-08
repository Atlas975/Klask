
public class ObjectMotion extends Thread{

    private GameWindow window;
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
        double puckRadius=scorePuck.getSize()/2;
        Ball player1=p1.passObject();
        Ball player2=p2.passObject();
        double frictionLoss=0.9985;

        while(!this.isInterrupted()){
            if(enteredGoal(1)){
                this.result=2;
                break;
            }
            if(enteredGoal(2)){
                this.result=1;
                break;
            }
            playerInteract(scorePuck, player1);
            playerInteract(scorePuck, player2);
            magnetsInteract(scorePuck);
            moveObject(scorePuck,scorePuck.getXPosition(),scorePuck.getYPosition(),puckRadius,frictionLoss);
        }
        return;
    }

    public void magnetMotion(){
        double magnetRadius=magnets[0].getSize()/2;
        Ball player1=p1.passObject();
        Ball player2=p2.passObject();
        double attractionForce=0.05;
        double frictionLoss=0.99;
        while(!this.isInterrupted()){
            magnetsInteract(magnetMain);
            moveObject(magnetMain,magnetMain.getXPosition(),magnetMain.getYPosition(),magnetRadius,frictionLoss);
            attractionForce(magnetMain,player1,player2,attractionForce,frictionLoss);
        }
        return;
    }

    public Boolean enteredGoal(int goalType){
        double distance;
        if(goalType==1){
            distance=Math.sqrt(Math.pow(scorePuck.getXPosition()-window.goalXPos(1),2)+Math.pow(scorePuck.getYPosition()-600,2))-65;
            return distance<0;
        }
        else{
            distance=Math.sqrt(Math.pow(scorePuck.getXPosition()-window.goalXPos(2),2)+Math.pow(scorePuck.getYPosition()-600,2))-65;
            return distance<0;
        }
    }

    public void playerInteract(Ball object, Ball player){
        if(object.collides(player)){
            deflect(object,player);
            double deflectX=player.getXPosition()+player.getXVelocity();
            double deflectY=player.getYPosition()+player.getYVelocity();
            if(p1.validateBounds(deflectX,deflectY)){
                player.setXPosition(deflectX);
                player.setYPosition(deflectY);
            }
        }
    }

    public void magnetsInteract(Ball object){
        for(Ball magnet: magnets){
            if(object.collides(magnet)){
                deflect(object, magnet);
                double deflectX=magnet.getXPosition()+magnet.getXVelocity();
                double deflectY=magnet.getYPosition()+magnet.getYVelocity();
                if(displacementCheck(magnet,deflectX,deflectY,15)){
                    magnet.setXPosition(deflectX);
                    magnet.setYPosition(deflectY);
                }
            }
        }
    }

    // public void magnetsRepel(Ball object){
    //     for(Ball magnet: magnets){
    //         if(object.collides(magnet)){
    //             d
    //         }
    //     }
    //     // for(Ball magnet: magnets){
    //     //     if(object.collides(magnet)){
    // }

    public Boolean displacementCheck(Ball magnet, double deflectX, double deflectY, double radius){
        if(deflectX-radius<=minX || deflectX+radius>=maxX || deflectY-radius<=minY || deflectY+radius>=maxY){
            return false;
        }
        return true;
    }

    public void moveObject(Ball object, double xPosition, double yPosition, double radius, double frictionLoss){
        double deflectX=object.getXPosition()+object.getXVelocity();
        double deflectY=object.getYPosition()+object.getYVelocity();

        if(deflectX-radius<=minX){
            object.setXVelocity(-object.getXVelocity()*0.5);
        }
        else if(deflectX+radius>=maxX){
            object.setXVelocity(-object.getXVelocity()*0.5);
        }
        if(deflectY+radius>=maxY){
            object.setYVelocity(-object.getYVelocity()*0.5);
        }
        else if(deflectY-radius<=minY){
            object.setYVelocity(-object.getYVelocity()*0.5);
        }

        object.setXPosition(xPosition+object.getXVelocity());
        object.setYPosition(yPosition+object.getYVelocity());

        try {
            Thread.sleep(1);
            object.setXVelocity(object.getXVelocity()*frictionLoss);
            object.setYVelocity(object.getYVelocity()*frictionLoss);
        } catch (InterruptedException e) {
            return;
        }
    }

    public void attractionForce(Ball magnet, Ball player1, Ball player2, double attractionForce, double frictionLoss){
        double magnetXPos=magnet.getXPosition();
        double magnetYPos=magnet.getYPosition();
        double player1XPos=player1.getXPosition();
        double player1YPos=player1.getYPosition();
        double player2XPos=player2.getXPosition();
        double player2YPos=player2.getYPosition();
        int attractBoundry=300;
        double p1Distance=Math.sqrt(Math.pow(player1XPos-magnetXPos,2)+Math.pow(player1YPos-magnetYPos,2));
        double p2Distance=Math.sqrt(Math.pow(player2XPos-magnetXPos,2)+Math.pow(player2YPos-magnetYPos,2));

        if(p1Distance==p2Distance){
            return;
        }
        else if(p1Distance<50){
            p1.incrementMagnet();
            permenentAttach(p1.passObject());
        }
        else if(p2Distance<50){
            p2.incrementMagnet();
            permenentAttach(p2.passObject());
        }
        else if(p1Distance<attractBoundry){
            if(player1XPos>magnetXPos){
                magnet.setXVelocity(attractionForce);
            }
            else if(player1XPos<magnetXPos){
                magnet.setXVelocity(-attractionForce);
            }
            if(player1YPos>magnetYPos){
                magnet.setYVelocity(attractionForce);
            }
            else if(player1YPos<magnetYPos){
                magnet.setYVelocity(-attractionForce);
            }
        }
        else if(p2Distance<attractBoundry){
            if(player2XPos>magnetXPos){
                magnet.setXVelocity(attractionForce);
            }
            else if(player2XPos<magnetXPos){
                magnet.setXVelocity(-attractionForce);
            }
            if(player2YPos>magnetYPos){
                magnet.setYVelocity(attractionForce);
            }
            else if(player2YPos<magnetYPos){
                magnet.setYVelocity(-attractionForce);
            }
        }
    }

    public void permenentAttach(Ball player){
        while(!this.isInterrupted()){
            // magnetMain.setXVelocity(0);
            // magnetMain.setYVelocity(0);
            magnetMain.setXPosition(player.getXPosition());
            magnetMain.setYPosition(player.getYPosition());
        }
        // return;
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
        if (mag == 0.0){
            result[0] = 1.0;
            for (int i=1; i < dimensions; i++)
            result[i] = 0.0;
        }
        else{
            for (int i=0; i < dimensions; i++)
            result[i] = vec[i] / mag;
        }
        return result;
    }

    public int result(){
        return result;
    }
}



















