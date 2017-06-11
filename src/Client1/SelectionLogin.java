package Client1;

import javax.swing.*;

import static КлиенСерверДля2компов.three.Constant.LOGIN;

public  class SelectionLogin extends JFrame {
    private static String[]  setIP          = {LOGIN, "Войти под другим именем"};
    private static ImageIcon icon           = null;
    private static String    unverifiedName = null;

    public void Login(String activeLogin) {
        if (LOGIN != null) selectionLogin (activeLogin);
        else {
            addName (activeLogin);
        }
    }

    private void selectionLogin(String activeLogin) {
        Object selectionLogin = JOptionPane.showInputDialog (this,
                "Ваш Логин?",
                "Настройка подключения",
                JOptionPane.QUESTION_MESSAGE,
                icon,setIP,setIP[0]);

         if (selectionLogin.toString().equals("Войти под другим именем")){
             addName (activeLogin);
        }
    }

    private void addName(String activeLogin) {
        while (true) {
            unverifiedName = JOptionPane.showInputDialog (this,
                    "Введите свое имя:",
                    "Настройка подключения",
                    JOptionPane.QUESTION_MESSAGE);

                String[] allLogin = activeLogin.split ("\\p{Punct}");
                boolean flag = false;
                for (String s : allLogin) {
                    if (s.toLowerCase ( ).equals (unverifiedName.toLowerCase ( ))) {
                        flag = true;
                    }
                }

                if (flag == true)
                    JOptionPane.showMessageDialog (this, "Логин занят другим пользователем!");
                else {
                    if (unverifiedName.length () > Constant.MIN_SYMBOL && unverifiedName.length () <= Constant.MAX_SYMBOL) {
                        LOGIN = unverifiedName;
                        break;
                    } else
                        JOptionPane.showMessageDialog (this, "Логин должен быть от 3-х до 15-и символов");
                }
        }
    }
}
