import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;

/**
 * An extension of CBbody that initializes the games main menu where the user
 * can select a mode to play
 */
class MenuOptions extends JFrame {
    private int option;
    private int[] modeParameters;

    /**
    *This is the constructor for the CBoptions class. it takes in a given title, message and the
    current score and uses this to create the user menu
    *@param title the title of the user
    *@param message the message disp layed to the user
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
        options=createPanel(options, 1, 3, 750, 100);

        Font txtFont=new Font("Arial",Font.BOLD,18);
        textDisplay("Select option", txtFont, textPanel);
        textDisplay("Round "+score[0], txtFont, textPanel);
        textDisplay("P1 wins: "+score[1], txtFont, textPanel);
        textDisplay("P2 wins: "+score[2], txtFont, textPanel);

        JButton []buttonOptions=new JButton[3];
        buttonOptions[0]=new JButton("Normal");
        buttonOptions[1]=new JButton("Frenzy");
        buttonOptions[2]=new JButton("Quit");
        buttonOptions[0].setBackground(new Color(0xa3be8c));
        buttonOptions[1].setBackground(new Color(0xbf616a));
        buttonOptions[2].setBackground(new Color(0xb48ead));

        for(int i=0; i<buttonOptions.length; i++){
            final int mode=i;
            options.add(buttonOptions[i]);
            buttonOptions[i].addActionListener(
                e->setModeParameters(mode)
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

    /**
     * This method initializes parameters that control various characteristics of
     * each mode
     *
     * @param mode the identifying number of the game mode the use chose
     *             param modeParameters[0] stores each modes idetifying number
     *             param modeParameters[1] maximum number of overall inputs from a
     *             user
     *             param modeParameters[2] number of rounds the user gets
     *             param modeParameters[3] number of colours the user has to guess
     *             param modeParameters[4] number of rows for score board
     *             param modeParameters[5] number of columns for score board
     *             param modeParameters[6] amount of time each mode has
     */
    public void setModeParameters(int mode) {
        synchronized (this) {
            int[] modeParameters = new int[7];
            modeParameters[0] = mode;
            switch (mode) {
                case 0 -> {
                    modeParameters[1]=0;
                }
                case 1 -> {
                    modeParameters[1] = 16;
                    modeParameters[2] = 4;
                    modeParameters[3] = 4;
                    modeParameters[4] = 8;
                    modeParameters[5] = 2;
                    modeParameters[6] = 45;
                }
            }
            this.modeParameters = modeParameters;
            notify();
        }
    }

    /**
     * This is a getter method that returns the option that the user selected.
     */
    int getOption() {
        return option;
    }

    /**
     * Returns the mode parameters
     *
     * @return The mode parameters.
     */
    int[] getModeParameters() {
        return modeParameters;
    }
}
