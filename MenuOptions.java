import java.awt.*;
import javax.swing.*;


/**
 * An extension of JFrame that creates the games main options meny
 */
class MenuOptions extends JFrame {
    private int option;
    private int[] modeParameters;

    /**
    *This is the constructor for the MenuOptions class. it takes in a given title, message and the
    current score and uses this to create the user menu
    *@param title the inro message shown
    *@param message the message displayed to the user
    *@param score the games current score
    */
    public MenuOptions(String title,int[] score) {
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setTitle(title);
        this.setSize(750,250);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel textPanel=new JPanel();
        textPanel=createPanel(textPanel, 1, 4, 750, 150);
        JPanel options=new JPanel();
        options=createPanel(options, 1, 2, 750, 100);

        Font txtFont=new Font("Arial",Font.BOLD,18);
        textDisplay("Select option", txtFont, textPanel);
        textDisplay("Round "+score[0], txtFont, textPanel);
        textDisplay("P1 wins: "+score[1], txtFont, textPanel);
        textDisplay("P2 wins: "+score[2], txtFont, textPanel);

        JButton []buttonOptions=new JButton[2];
        buttonOptions[0]=new JButton("Play");
        buttonOptions[1]=new JButton("Quit");
        buttonOptions[0].setBackground(new Color(0xa3be8c));
        buttonOptions[1].setBackground(new Color(0xb48ead));

        for(int i=0; i<buttonOptions.length; i++){
            final int mode=i;
            options.add(buttonOptions[i]);
            buttonOptions[i].addActionListener(
                e->setOption(mode)
            );
        }

        this.add(textPanel, BorderLayout.WEST);
        this.add(options, BorderLayout.SOUTH);

        synchronized(this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * This function creates a panel with a grid layout, sets the preferred size, and sets the background
     * color
     *
     * @param panel panel object containing the
     * @param rows number of rows in the grid
     * @param cols number of columns
     * @param width The width of the panel
     * @param height The height of the panel
     * @return A JPanel object.
     */
    public JPanel createPanel(JPanel panel, int rows,int cols, int width, int height){
        panel.setLayout(new GridLayout(rows,cols));
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(new Color(0x2e3440));
        return panel;
    }

    public void textDisplay(String message, Font txtFont, JPanel textPanel){
        JLabel txt=new JLabel(message,SwingConstants.CENTER);
        txt.setFont(txtFont);
        txt.setForeground(Color.white);
        textPanel.add(txt);
    }

    public void setOption(int option){
        synchronized (this) {
            this.option = option;
            notify();
        }
    }

    /**
     * This function returns 0 if the user chose to play, 1 if the user chose to quit
     *
     * @return The option variable is being returned.
     */
    public int getOption(){
        return option;
    }


    /**
     * Returns the mode parameters, used to retrieve info on the users option choice
     *
     * @return The mode parameters.
     */
    int[] getModeParameters() {
        return modeParameters;
    }
}
