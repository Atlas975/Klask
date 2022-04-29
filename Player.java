public class Player extends Ball {

    private double minX;
    private double maxX;
    private double minY=195;
    private double maxY=1005;
    private int score=0;


    public Player(int player){
        super(0,0,60,"BLACK",4);
        if(player==1){
            this.minX=195;
            this.maxX=1006;
        }
        else{
            this.minX=1030;
            this.maxX=1;
        }
    }

    public void setScore(int score){
        this.score=score;
    }

    public int getScore(){
        return score;
    }

}
