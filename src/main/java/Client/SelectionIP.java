package Client;

import javax.swing.*;
/**Всплывает окно которое предложит несколько вариантов подключения, в случае с другой IP
 * нужно будет ввести адресс ПК сервера
 * cmd>>ipconfig - узнаем IP*/

public class SelectionIP extends JFrame {
    private static String[] setIP = {"LocalHost: 127.0.0.1",
            "Статический IP: 192.168.137.1",
            "Динамический IP: 95.79.129.164",
            "Другой IP"};
    private static ImageIcon icon = null;

    public void IPButton() {
        Object selectionIP = JOptionPane.showInputDialog (this,
                "Выберите адресс соединения",
                "Настройка подключения",
                JOptionPane.QUESTION_MESSAGE,
                icon, setIP, setIP[0]);

        try {
            if (selectionIP.toString ( ).equals ("LocalHost: 127.0.0.1")) {
                Constant.IP = "127.0.0.1";
            } else if (selectionIP.toString ( ).equals ("Статический IP: 192.168.137.1")) {
                Constant.IP = "192.168.137.1";
            } else if (selectionIP.toString ( ).equals ("Динамический IP: 95.79.129.164")) {
                Constant.IP = "95.79.129.164";
            } else {
                Constant.IP = JOptionPane.showInputDialog (this,
                        "Введите IP-адресс подключения",
                        "Настройка подключения",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (NullPointerException e) {
            //если пользователь закроек окно, программа завершится.
            System.exit (0);
        }
    }
}
