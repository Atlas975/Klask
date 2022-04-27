public class App {
    public static void main(String args[]){
        int overheadStats[] = {1,0,0};
        OptionsMenu options=new OptionsMenu("Welcome to Klask!","Select Option",overheadStats);
        options.dispose();
        new GameController(overheadStats);
    }
}
