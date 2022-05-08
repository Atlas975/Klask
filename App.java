public class App {
    /**
     * Creates an array of game stats represetning round number, player 1 wins, player 2 wins, player 1 round wins
     * and player 2 round wins. Restrieves the chosen 
     *
     */
    public static void main(String args[]){
        int overheadStats[] = {1,0,0,0,0};
        MenuOptions options=new MenuOptions("Welcome to Klask!",overheadStats);
        options.dispose();
        new GameController(overheadStats,options.getModeParameters());
    }
}
