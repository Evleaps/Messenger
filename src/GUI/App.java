package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Ромчи on 02.06.2017.
 */
public class App extends JFrame implements Runnable {
    private JTextField        messages;
    private JButton           send;
    private JTextArea         chat;
    private JTextArea         userChat;
    private JPanel            tykChat;
    private static String[]   setIP   = {"LocalHost: 127.0.0.1", "Другой IP"};
    private static ImageIcon  icon    = null;

    public App() {
        setContentPane (tykChat);
        setSize (350, 400);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null); //окно вылезет в центре монитора

        setVisible (true);
    }

    @Override
    public void run() {
        send.addActionListener (new ActionListener ( ) {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog (null,"dfdf");
            }
        });
/**
 * Нужно этот класс превратить в Клиент и отсюда из Run делать операции. Обернуть в рекурсию, и расположить кнопки
 * JOptionPane.showMessageDialog (null,"dfdf"); должен работать из рекурсии и выводить сообщение только если есть
 * сейчас ничего не выводит*/
    }
}
