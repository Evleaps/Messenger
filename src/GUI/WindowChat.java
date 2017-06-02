package GUI;

import javax.swing.*;
import java.awt.*;

public class WindowChat extends JFrame {
    private String nameWindow;

    public void Wind(String nameWindow) {
        this.nameWindow = nameWindow;
        setLayout (new FlowLayout ( ));
        setSize (400, 300);
        setLocationRelativeTo (null); //окно вылезет в центре монитора
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setVisible (true);

        new App().createUIComponents();
    }
}
