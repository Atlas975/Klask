/**
 * It creates a MenuOptions object, which is a JFrame that allows the user to select the game mode and
 * the number of players. It then creates a GameController object, which is a JFrame that contains the
 * game
 */

public class ObjectMotion extends Thread{

    private Boolean stop=false;
    private GameWindow window;
    private Player p1;
    private Player p2;
    private Ball magnetMain;
    private Ball magnets[];
    private Ball scorePuck;
    private int pieceIndex=-1;
    private int minX=165;
    private int maxX=1835;
    private int minY=165;
    private int maxY=1035;

    /**
     * If the thread is running, stop it.
     */
    public synchronized void terminate(){
        this.stop=true;
    }

    /**
     * @return A boolean value that indicates whether the round is over.
     */
    public synchronized Boolean roundOver(){
        return this.stop;
    }

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

    /**
     * The run activates the motion of the score puck and the magnets depending on the piece index
     */
    @Override
    public void run() {
        double objectRadius;
        double frictionLoss;
        double attractionForce;
        Ball player1=p1.passObject();
        Ball player2=p2.passObject();

        if(pieceIndex==-1){
            objectRadius=scorePuck.getSize()/2;
            frictionLoss=0.9985;

            while(!roundOver()){
                if(enteredGoal(1)){
                    p1.setLoss(true);
                    break;
                }
                if(enteredGoal(2)){
                    p2.setLoss(true);
                    break;
                }
                playerInteract(scorePuck, player1);
                playerInteract(scorePuck, player2);
                magnetsInteract(scorePuck);
                moveObject(scorePuck,scorePuck.getXPosition(),scorePuck.getYPosition(),objectRadius,frictionLoss);
            }
            return;
        }

        else{
            objectRadius=magnets[0].getSize()/2;
            frictionLoss=0.99;
            attractionForce=0.05;

            while(!roundOver()){
                magnetsInteract(magnetMain);
                moveObject(magnetMain,magnetMain.getXPosition(),magnetMain.getYPosition(),objectRadius,frictionLoss);
                attractionControl(magnetMain,player1,player2,attractionForce);
            }
            return;
        }
    }

    /**
     * If the puck is within 50 pixels of the goal, return true
     *
     * @param goalType 1 for the left goal, 2 for the right goal
     * @return The distance between the puck and the goal.
     */
    public Boolean enteredGoal(int goalType){
        double distance;
        if(goalType==1){
            distance=Math.sqrt(Math.pow(scorePuck.getXPosition()-window.goalXPos(1),2)+Math.pow(scorePuck.getYPosition()-600,2))-50;
            return distance<0;
        }
        else{
            distance=Math.sqrt(Math.pow(scorePuck.getXPosition()-window.goalXPos(2),2)+Math.pow(scorePuck.getYPosition()-600,2))-50;
            return distance<0;
        }
    }

    /**
     * If the player collides with the ball, deflect the ball and move the player in the direction of the
     * ball's deflection
     *
     * @param object The object that is being interacted with.
     * @param player The player object
     */
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

    /**
     * If the ball collides with a magnet, deflect the ball and move the magnet
     *
     * @param object the ball that is being deflected
     */
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

    /**
     * If the ball is going to be displaced outside the bounds of the screen, return false
     *
     * @param magnet the ball that is being checked
     * @param deflectX the x coordinate of the ball if deflection occurs
     * @param deflectY the y-coordinate of the ball if deflection occurs
     * @param radius the radius of the ball
     * @return If the ball is safe to deflect
     */
    public Boolean displacementCheck(Ball magnet, double deflectX, double deflectY, double radius){
        if(deflectX-radius<=minX || deflectX+radius>=maxX || deflectY-radius<=minY || deflectY+radius>=maxY){
            return false;
        }
        return true;
    }

    /**
     * If the object is going to hit a wall, this method deflects the object and absorbs part
     * of the objects momentum
     *
     * The first thing the function does is calculate the position the object will be in if it
     * continues on its current trajectory. If the object is going to hit a wall, the function reverses
     * the object's velocity and slows it down. Then the function moves the object and slows it down
     *
     * @param object
     * @param xPosition
     * @param yPosition
     * @param radius
     * @param frictionLoss velocity percetage conserved while moving
     */
    public void moveObject(Ball object, double xPosition, double yPosition, double radius, double frictionLoss){
        double deflectX=object.getXPosition()+object.getXVelocity();
        double deflectY=object.getYPosition()+object.getYVelocity();

        if(deflectX-radius<=minX){
            object.setXVelocity(-object.getXVelocity()*0.75);
        }
        else if(deflectX+radius>=maxX){
            object.setXVelocity(-object.getXVelocity()*0.75);
        }
        if(deflectY+radius>=maxY){
            object.setYVelocity(-object.getYVelocity()*0.75);
        }
        else if(deflectY-radius<=minY){
            object.setYVelocity(-object.getYVelocity()*0.75);
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

    /**
     * If the distance between the magnet and either player is less than 50, the magnet latches on to the player. If
     * the distance is less than 300, the magnet is attracted to the player
     *
     * @param magnet The magnet object
     * @param player1 The first player object
     * @param player2 The second player object
     * @param attractionForce The velocity that magnets head towards the player
     */
    public void attractionControl(Ball magnet, Ball player1, Ball player2, double attractionForce){
        double magnetXPos=magnet.getXPosition();
        double magnetYPos=magnet.getYPosition();
        double player1XPos=player1.getXPosition();
        double player1YPos=player1.getYPosition();
        double player2XPos=player2.getXPosition();
        double player2YPos=player2.getYPosition();
        int attractBoundry=275;
        double p1Distance=Math.sqrt(Math.pow(player1XPos-magnetXPos,2)+Math.pow(player1YPos-magnetYPos,2));
        double p2Distance=Math.sqrt(Math.pow(player2XPos-magnetXPos,2)+Math.pow(player2YPos-magnetYPos,2));
        if(p1Distance==p2Distance){
            return;
        }
        else if(p1Distance<35){
            p1.incrementMagnet();
            playerLatch(p1.passObject());
        }
        else if(p2Distance<35){
            p2.incrementMagnet();
            playerLatch(p2.passObject());
        }
        else if(p1Distance<attractBoundry){
            if(p1Distance<150){
                attract(magnet,magnetXPos,magnetYPos,player1XPos,player1YPos,attractionForce*2);
            }
            else if(p1Distance<200){
                attract(magnet,magnetXPos,magnetYPos,player1XPos,player1YPos,attractionForce*1.5);
            }
            else{
                attract(magnet,magnetXPos,magnetYPos,player1XPos,player1YPos,attractionForce);
            }
        }
        else if(p2Distance<attractBoundry){
            if(p2Distance<150){
                attract(magnet,magnetXPos,magnetYPos,player2XPos,player2YPos,attractionForce*2);
            }
            else if(p2Distance<200){
                attract(magnet,magnetXPos,magnetYPos,player2XPos,player2YPos,attractionForce*1.5);
            }
            else{
                attract(magnet,magnetXPos,magnetYPos,player2XPos,player2YPos,attractionForce);
            }
        }
    }

    public void attract(Ball magnet, double magnetXPos,double magnetYPos, double playerXPos, double playerYPos, double attractionForce){
        if(playerXPos>magnetXPos){
            magnet.setXVelocity(attractionForce);
        }
        else if(playerXPos<magnetXPos){
            magnet.setXVelocity(-attractionForce);
        }
        if(playerYPos>magnetYPos){
            magnet.setYVelocity(attractionForce);
        }
        else if(playerYPos<magnetYPos){
            magnet.setYVelocity(-attractionForce);
        }
    }

    /**
     * While the thread is not interrupted, set the magnet's x and y position to the player's x and y
     * position.
     *
     * @param player The player object that the magnet will latch onto.
     */
    public void playerLatch(Ball player){
        while(!roundOver()){
            magnetMain.setXPosition(player.getXPosition());
            magnetMain.setYPosition(player.getYPosition());
        }
        return;
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
}



















