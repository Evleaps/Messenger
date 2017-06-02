package GUI;

import Logic.Constant;

import javax.swing.*;

/**
 * Created by Ромчи on 02.06.2017.
 */
public class App extends WindowChat{
    private JTextField        Messages;
    private JButton           Send;
    private JTextArea         Chat;
    private JTextArea         userChat;
    private static String[]   setIP   = {"LocalHost: 127.0.0.1", "Другой IP"};
    private static ImageIcon  icon    = null;

    public void createUIComponents() {
        Object selectionIP = JOptionPane.showInputDialog (this,
                "Выберите адресс соединения",
                "Настройка подключения",
                JOptionPane.QUESTION_MESSAGE,
                icon,setIP,setIP[0]);

        if (selectionIP.toString().equals("LocalHost: 127.0.0.1")) {
            Constant.IP = "127.0.0.1";
        } else {
            Constant.IP = JOptionPane.showInputDialog (this,
                    "Введите IP-адресс подключения",
                    "Настройка подключения",
                    JOptionPane.WARNING_MESSAGE);
        }

        Messages = new JTextField ();
        Send = new JButton ();
        Chat = new JTextArea ();
        userChat = new JTextArea ();

        add (Messages);
        add (Send);
        add (Chat);
        add (userChat);
    }
}
