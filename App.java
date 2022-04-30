public class App {
    public static void main(String args[]){
        int overheadStats[] = {1,0,0,0,0};
        MenuOptions options=new MenuOptions("Welcome to Klask!",overheadStats);
        options.dispose();
        new GameController(overheadStats,options.getModeParameters());

        // this is an implementation of the game klask, a similar game to pong but with magnetic pucks and magnets added into the fold.
    }
}
