package nackademin.mackansw.bricks;

import javax.swing.*;
import java.awt.*;

public class Brick extends JButton {

    public Brick(int numbTitle) {
        setText(String.valueOf(numbTitle));
        setBackground(Color.darkGray);
        setForeground(Color.white);
    }
}