package GUI;
import Logic.Constant;
import javax.swing.*;

public class SelectionIP extends JFrame {
    private static String[]   setIP   = {"LocalHost: 127.0.0.1", "Другой IP"};
    private static ImageIcon  icon    = null;

    public void IPButton() {
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
    }
}
