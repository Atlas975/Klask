/**
 * Main starting class, connects the options menu to the game window via the MenuOptions and GameController classes.
 */
public class Main {
    /**
     * Creates array of game stats storing round number, player 1 game wins, player 2 game wins, player 1 round wins
     * and player 2 round wins. Restrieves the chosen user option and passes it to gameController to begin game
     */
    public static void main(String args[]){
        int overheadStats[] = {1, 0, 0, 0, 0};
        MenuOptions options = new MenuOptions("Welcome to Klask!", overheadStats);
        options.dispose();
        new GameController(overheadStats, options.getOption());
    }
}
