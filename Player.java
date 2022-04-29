public class Player extends Ball {

    private double minX;
    private double maxX;
    private double minY=185;
    private double maxY=1015;


    public Player(int xPos, int yPos,int player){
        super(xPos,yPos,40,"BLACK",4);
        if(player==1){
            this.minX=185;
            this.maxX=990;
        }
        else{
            this.minX=1030;
            this.maxX=1815;
        }


        

        // double zoneConstraints[][]={{165,1010,165,1035},{1010,1835,165,1035}};

    }

}
