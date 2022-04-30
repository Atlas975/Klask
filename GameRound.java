public class GameRound {
    public GameRound(GameWindow window,Ball magnets[],Ball scorePuck,Ball player1,Ball player2){
        Motion p1=new Motion(player1,player2,magnets,scorePuck);
        Motion p2=new Motion(player2,player1,magnets,scorePuck);
        p1.start();
        p2.start();
        for(int j=0; j<3; j++){
            Motion magForce=new Motion(j,player1,player2,magnets,scorePuck);
            magForce.start();
        }
        Motion puck=new Motion(scorePuck,player1,player2,magnets);
        puck.start();
    }
}
