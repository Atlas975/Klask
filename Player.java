public class Player extends Ball {

    private double minX;
    private double maxX;
    private double minY=185;
    private double maxY=1015;


    public Player(int player){
        super(0,0,60,"BLACK",4);
        if(player==1){
            this.minX=185;
            this.maxX=990;
        }
        else{
            this.minX=1030;
            this.maxX=1815;
        }
    }

}
