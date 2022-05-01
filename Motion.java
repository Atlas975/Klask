
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class Motion{

    private int winner;
    private double minX;
    private double maxX;
    private double minY=165;
    private double maxY=1035;
    private int baseleep;
    private GameWindow window;
    private int overheadStats[];
    private boolean loss=false;




    //  Constructor class to control the magnets motion
    public Motion(int pieceIndex,Ball player1,Ball player2,Ball magnets[],Ball scorePuck, GameWindow window){






    }

    // Constructor class to control the score pucks motion
    public Motion(Ball piece,Ball player1, Ball player2, Ball magnets[], GameWindow window){





    }

    // Constructor class to control the player pucks motion
    public Motion(Ball piece, int playerID, Ball opponent,Ball magnets[],Ball scorePuck, GameWindow window,int overheadStats[]){

        this.window=window;
        this.overheadStats=overheadStats;
        this.minX=window.playerMinX(playerID);
        this.maxX=window.playerMaxX(playerID);
        this.minY=window.ballMinY();
        this.maxY=window.ballMaxY();

        window.addKeyListener(new KeyListener() {
            boolean keyArray[]=new boolean[8];
            @Override public void keyTyped(KeyEvent e){} // not used
            @Override public void keyReleased(KeyEvent e){
                keyArray=playerInput(e,keyArray, playerID, false);
            }
            @Override public void keyPressed(KeyEvent e) {
                keyArray=playerInput(e,keyArray, playerID, true);
                playerUpdate(keyArray,piece,playerID);
            }

        });

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

    public void playerUpdate(boolean keyArray[],Ball player,int playerID){
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




            // if((currentX-30<window.goalXPos(playerID)+40) || (currentX+30>window.goalXPos(playerID)-40)){
            //     System.out.println("Player "+playerID+" scored!");

            //     if(currentY+30<560 || currentY-30>640){
            //         // System.out.println("Player "+playerID+" scored!");
            //         this.run=false;
            //     }

            // }





        // if(currentY<6 && currentY>220){
        //     System


        // }
    }
    public boolean ended(){
        return loss;
    }

    public void reset(){
        loss=false;
    }

    public int[] getResult(){
        return overheadStats;
    }



//     public void playerController(Ball piece,Ball allPiecs){

//     }

//     public void scoreController(Ball piece,Ball allPieces[]){

//     }

//     public void magnetController(Ball piece,Ball allPieces[]){

//     }

// }




// public void deflect(){
//  // The position and speed of each of the two balls in the x and y axis before collision.
//  // YOU NEED TO FILL THESE VALUES IN AS APPROPRIATE...
//     double xPosition1, xPosition2, yPosition1, yPosition2;
//     double xSpeed1, xSpeed2, ySpeed1, ySpeed2;
//     // Calculate initial momentum of the balls... We assume unit mass here.
//     double p1InitialMomentum = Math.sqrt(xSpeed1 * xSpeed1 + ySpeed1 * ySpeed1);
//     double p2InitialMomentum = Math.sqrt(xSpeed2 * xSpeed2 + ySpeed2 * ySpeed2);
//     // calculate motion vectors
//     double[] p1Trajectory = {xSpeed1, ySpeed1};
//     double[] p2Trajectory = {xSpeed2, ySpeed2};
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
//     double[] p1FinalTrajectory = {p1Trajectory[0] + p1Deflect[0] - p2Deflect[0], p1Trajectory[1] + p1Deflect[1],p2Deflect[1]};
//     double[] p2FinalTrajectory = {p2Trajectory[0] + p2Deflect[0] - p1Deflect[0], p2Trajectory[1] + p2Deflect[1],p1Deflect[1]};
//     // Calculate the final energy in the system.
//     double p1FinalMomentum = Math.sqrt(p1FinalTrajectory[0] * p1FinalTrajectory[0] + p1FinalTrajectory[1] * p1FinalTrajectory[1]);
//     double p2FinalMomentum = Math.sqrt(p2FinalTrajectory[0] * p2FinalTrajectory[0] + p2FinalTrajectory[1] * p2FinalTrajectory[1]);

//     // Scale the resultant trajectories if we've accidentally broken the laws of physics.
//     double mag = (p1InitialMomentum + p2InitialMomentum) / (p1FinalMomentum + p2FinalMomentum);
//     // Calculate the final x and y speed settings for the two balls after collision.
//     xSpeed1 = p1FinalTrajectory[0] * mag;
//     ySpeed1 = p1FinalTrajectory[1] * mag;
//     xSpeed2 = p2FinalTrajectory[0] * mag;
//     ySpeed2 = p2FinalTrajectory[1] * mag;
// }
//  /**
//  * Converts a vector into a unit vector.
//  * Used by the deflect() method to calculate the resultnt direction after a collision.
//  */
//  private double[] normalizeVector(double[] vec)
//  {
//         double mag = 0.0;
//         int dimensions = vec.length;
//         double[] result = new double[dimensions];
//         for (int i=0; i < dimensions; i++)
//             mag += vec[i] * vec[i];
//             mag = Math.sqrt(mag);
//             if (mag == 0.0)
//         {
//         result[0] = 1.0;
//         for (int i=1; i < dimensions; i++)
//             result[i] = 0.0;
//         }
//         else
//         {
//             for (int i=0; i < dimensions; i++)
//                 result[i] = vec[i] / mag;
//         }
//         return result;
//  }
}
