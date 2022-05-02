public class GameRound {

    private int overheadStats[];

    public GameRound(GameWindow window,Ball magnets[],Ball scorePuck,Ball player1,Ball player2, int overheadStats[]){

        Object pause=new Object();
        Player p1=new Player(player1,1,player2,magnets,scorePuck,window,pause);
        p1.start();
        Player p2=new Player(player2,2,player1,magnets,scorePuck,window,pause);
        p2.start();

        synchronized(pause){
            try {
                pause.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        if(p1.ended()){
            p1.reset();
            overheadStats[4]++;
            window.scoreIncremeent(2,overheadStats[4]);
            window.resetBoard(overheadStats, magnets, scorePuck,player1, player2, 1);
        }

        if(p2.ended()){
            p2.reset();
            overheadStats[3]++;
            window.scoreIncremeent(1,overheadStats[3]);
            window.resetBoard(overheadStats, magnets, scorePuck,player1, player2, 2);
        }



    }

    public int[] getUpdatedStats(){
        return overheadStats;
    }
}
