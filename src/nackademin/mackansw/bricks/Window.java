package nackademin.mackansw.bricks;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Window {

    private JFrame window;
    private JFrame helpScreen;
    private JPanel basePanel;
    private JPanel gamePanel;
    private JPanel southPane;
    private JPanel helpPanel;
    private JButton helpButton;
    private JButton newGame;
    private JButton exit;
    private JTextArea textarea;

    private Brick gameBrick;
    private Brick empty = new Brick(0);;

    private List<Brick> bricks = new ArrayList<>();
    private List<Brick> solved = new ArrayList<>();

    private final Font gameTextFont = new Font(Font.MONOSPACED, Font.BOLD, 15);
    private final Font gameBrickFont = new Font(Font.MONOSPACED, Font.BOLD, 21);

    private boolean playing = false;

    private void buildGui() {
        window = new JFrame("Awesome Game of Bricks!");
        window.setSize(600, 600);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        basePanel = new JPanel();
        basePanel.setLayout(new BorderLayout());

        gamePanel = new JPanel();
        basePanel.add(gamePanel, new BorderLayout().CENTER);
        gamePanel.setBackground(Color.gray);
        gamePanel.setLayout(new GridLayout(4, 4));

        southPane = new JPanel();
        basePanel.add(southPane, new BorderLayout().SOUTH);
        southPane.setLayout(new BorderLayout());

        newGame = new JButton("Starta nytt spel!");
        southPane.add(newGame, new BorderLayout().CENTER);
        newGame.setBackground(Color.darkGray);
        newGame.setForeground(Color.white);
        newGame.setFont(gameTextFont);
        newGame.addActionListener(e -> {
            shuffle();
        });

        helpButton = new JButton("?");
        southPane.add(helpButton, new BorderLayout().EAST);
        helpButton.setBackground(Color.darkGray);
        helpButton.setForeground(Color.white);
        helpButton.setFont(gameTextFont);
        helpButton.setPreferredSize(new Dimension(120, 60));
        helpButton.addActionListener(e -> {
            helpScreen.setVisible(true);
        });

        helpScreen = new JFrame("How to play!");
        helpScreen.setSize(570, 270);
        helpScreen.setResizable(false);
        helpScreen.setLocationRelativeTo(window);
        helpScreen.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        helpPanel = new JPanel();
        helpScreen.add(helpPanel);
        helpPanel.setLayout(new BorderLayout());

        textarea = new JTextArea("\t \t \t How to play! \n \n 1. Tryck på en bricka för att flytta den till närmaste tomma yta! \n \n 2. Plasera alla brickor i nummerordning för att vinna spelet! \n \n 3. Tryck 'starta nytt spel' för att börja om !");
        helpScreen.add(textarea, new BorderLayout().CENTER);
        textarea.setBackground(Color.darkGray);
        textarea.setForeground(Color.white);
        textarea.setEditable(false);
        textarea.setFont(gameTextFont);

        exit = new JButton("OK");
        helpScreen.add(exit, new BorderLayout().SOUTH);
        exit.setForeground(Color.white);
        exit.setBackground(Color.gray);
        exit.setFont(gameTextFont);
        exit.setPreferredSize(new Dimension(40,40));
        exit.addActionListener(e -> {
            helpScreen.setVisible(false);
        });

        window.add(basePanel);
        window.setVisible(true);
    }

    //Themed dialog box
    private void showWinMessage() {
        UIManager.put("OptionPane.background", Color.darkGray);
        UIManager.put("OptionPane.messageForeground", Color.white);
        UIManager.put("Panel.background", Color.darkGray);
        UIManager.put("Button.background", Color.gray);
        UIManager.put("Button.foreground", Color.white);
        JOptionPane.showMessageDialog(null, "Grattis du klarade spelet!");
    }

    private void setActionOf(Brick brick) {
        brick.addActionListener(e -> {
            int j = 0;

            //Gets event source in list
            for (int i = 0; i < bricks.size(); i++) {
                if (bricks.get(i).equals(brick)) {
                    j = i;
                    break;
                }
            }

            //Can only move if playing
            if(playing) {

                //Moves sides
                if(j > 0) {
                    if(j != 4 & j != 8 & j != 12) {
                        if (!bricks.get(j -1).isVisible()) {
                            moveBack(j, 1, brick);
                        }
                    }
                    if(j < 15) {
                        if(j != 3 & j != 7 & j != 11) {
                            if (!bricks.get(j + 1).isVisible()) {
                                moveForward(j, 1, brick);
                            }
                        }
                    }
                }
                else {
                    if(!bricks.get(j + 1).isVisible()) {
                        moveForward(j, 1, brick);
                    }
                }

                //Moves down/up
                if(j < 12) {
                    if (!bricks.get(j + 4).isVisible()) {
                        moveForward(j, 4, brick);
                    }
                    if(j > 3) {
                        if (!bricks.get(j - 4).isVisible()) {
                            moveBack(j, 4, brick);
                        }
                    }
                }
                else {
                    if (!bricks.get(j - 4).isVisible()) {
                        moveBack(j, 4, brick);

                    }
                }
                checkBoard();
            }
        });
    }

    /**
     * Moves the brick backwards
     * @param j the index of pressed button in list
     * @param i how many steps back it should move
     * @param brick the pressed brick
     */
    private void moveBack(int j, int i, Brick brick) {
        bricks.get(j - i).setText(brick.getText());
        brick.setText(bricks.get(j - i).getText());
        bricks.get(j - i).setVisible(true);
        brick.setVisible(false);
    }

    /**
     * Moves the brick forwards
     * @param j the index of pressed button in list
     * @param i how many steps back it should move
     * @param brick the pressed brick
     */
    private void moveForward(int j, int i, Brick brick) {
        bricks.get(j + i).setText(brick.getText());
        brick.setText(bricks.get(j + i).getText());
        bricks.get(j + i).setVisible(true);
        brick.setVisible(false);
    }

    /**
     * Checks if the game is compleate
     */
    private void checkBoard() {
        boolean compleate = true;

        //Matches the lists against each other
        for(int i = 0; i < bricks.size(); i++) {
            if(!bricks.get(i).getText().equals(solved.get(i).getText())) {
                compleate = false;
                break;
            }
        }

        //Output
        if(compleate) {
            showWinMessage();
            playing = false;
        }
        else {
            System.out.println("Still Unsolved!");
        }
    }

    //Generates the board
    private void generateBricks() {
        for (int i = 1; 16 > i; i++) {
            gameBrick = new Brick(i);
            gameBrick.setFont(gameBrickFont);
            gamePanel.add(gameBrick);
            bricks.add(gameBrick);
            solved.add(gameBrick);
        }

        //Adds empty after the gamebricks
        empty.setVisible(false);
        empty.setFont(gameTextFont);
        gamePanel.add(empty);
        bricks.add(empty);
        solved.add(empty);

        gamePanel.revalidate();
    }

    //shuffles the board
    private void shuffle() {
        //removes empty and all components from panel, empty should not be suffled
        bricks.remove(empty);
        gamePanel.removeAll();

        //Resets order, prevents dupplicates
        for (int i = 0; i < bricks.size(); i++) {
            bricks.get(i).setText(solved.get(i).getText());
        }

        //Shuffles list
        Collections.shuffle(bricks);

        //Adds new buttons to gamepanel, their text are equal to the new shuufled bricks
        for (int i = 0; i < bricks.size(); i++) {
            gameBrick = new Brick(Integer.valueOf(bricks.get(i).getText()));
            setActionOf(gameBrick);
            gameBrick.setFont(gameBrickFont);
            gamePanel.add(gameBrick);
            bricks.set(i, gameBrick);
        }

        //adds empty and sets action
        empty.setVisible(false);
        empty.setFont(gameTextFont);
        setActionOf(empty);
        gamePanel.add(empty);
        bricks.add(empty);

        //gamepanel refresh
        gamePanel.repaint();
        gamePanel.revalidate();
        playing = true;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Window game = new Window();
        game.buildGui();
        game.generateBricks();
        game.shuffle();
    }
}