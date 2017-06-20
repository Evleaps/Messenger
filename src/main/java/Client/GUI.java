package Client;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class GUI extends JFrame {
    private JPanel     tykChat;
    private JButton    send;
    private JButton    edit;
    private JTextArea  userOnline;
    private JTextField inputMessages;
    private JList      areaMessage;

    private ObjectInputStream inputMessage;
    private PrintWriter       outputMessage;
    private Socket            connectionMessage;

    private ObjectInputStream inputCheckForOnline;
    private PrintWriter       outputCheckForOnline;
    private Socket            connectionCheckForOnline;

    private String            activeLogin;

    public static void main(String[] args) {
        new GUI ( );
    }

    public GUI() {
        new SelectionIP ( ).IPButton ( );//пользователь выбирает адресс подключения

        try {
            // Подключаемся к серверам и получаем потоки для передачи сообщений
            connectionMessage = new Socket (ConstantClient.IP, ConstantClient.PORT_MESSAGE);
            inputMessage = new ObjectInputStream (connectionMessage.getInputStream ( ));
            outputMessage = new PrintWriter (connectionMessage.getOutputStream ( ), true);

            connectionCheckForOnline = new Socket (ConstantClient.IP, ConstantClient.PORT_ONLINE);
            inputCheckForOnline = new ObjectInputStream (connectionCheckForOnline.getInputStream ( ));//читаем с сервера
            outputCheckForOnline = new PrintWriter (connectionCheckForOnline.getOutputStream ( ), true); //записываем на сервер
        } catch (IOException e) {
            e.getStackTrace ( );
        }

        //Вводим уникальный в чате логин
        selectLogin ( );
        // Запускаем вывод всех входящих сообщений в консоль
        new Thread (new ReceiveMessage (inputMessage, areaMessage)).start ( );
        //Запускаем поток проверки на Online
        new Thread (new CheckForOnline (outputCheckForOnline, inputCheckForOnline, userOnline)).start ( );

        setContentPane (tykChat);
        pack ( );//размер задается в gui.form
        //setSize (ConstantServer.WIDTH_GUI, ConstantServer.HEIGHT_GUI);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null); //окно всплывет в центре монитора
        setVisible (true);
        userOnline.setText ("Участники беседы:" + "\n" + activeLogin + ConstantClient.LOGIN);
        System.out.println (ConstantClient.LOGIN + " появился в сети!");


        edit.addActionListener (new ActionListener ( ) {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource ( ) == edit)
                    try {
                        editMessage (areaMessage.getSelectedValue ( ).toString ( ));
                    } catch (NullPointerException err) {
                        System.out.println ("Пользователь тыкнул в редактировать");
                    }
            }
        });

        send.addActionListener (new ActionListener ( ) {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource ( ) == send)
                    sendMessage (inputMessages.getText ( ));
            }
        });
    }

    /**
     * Читаем с сервера список занятых логинов, передаем его в качестве параметра в класс
     * SelectionLogin, выбираем свободный, SelectionLogin установит логин в ConstantServer и мы
     * отправим наш логин на сервер
     */
    private void selectLogin() {
        outputCheckForOnline.println ( );
        try {
            activeLogin = inputCheckForOnline.readObject ( ).toString ( );
        } catch (ClassNotFoundException e) {
            e.printStackTrace ( );
        } catch (IOException e) {
            e.printStackTrace ( );
        }
        System.out.println ("занятые логины >>" + activeLogin + "<<");
        try {
            new SelectionLogin ( ).Login (activeLogin);
        } catch (NullPointerException e) {
            System.exit (0);
        }
        outputMessage.println (ConstantClient.LOGIN);
    }

    /**
     * При нажитии на кнопку "отправить" мы передаем на сервер строку содержащую дату и текст сообщения
     */
    private void sendMessage(String messagesText) {
        outputMessage.println (messagesText);
        inputMessages.setText ("");//после отправки поле сообщения очищается.
    }

    /**
     * accessCheck смотрит, какое имя автор сообщения. Если не мы, то нельзя изменять.
     * countMessage показывает порядковый номер сообщения
     * наше сообщение начинается после черты ` - `, по этому split[1]
     */
    private void editMessage(String messageText) {
        String accessCheck = messageText.split (":")[0];
        String countMessage = messageText.split (" ")[0];
        String oldMessage;
        String newMessage = null;
        if (accessCheck.contains (ConstantClient.LOGIN)) {
            oldMessage = messageText.split (" - ")[1];
            newMessage = JOptionPane.showInputDialog (this,
                    "Измените свое сообщение:", oldMessage);
            //отправим на сервер с пометкой на редактирование
            if (newMessage == null);
            else outputMessage.println (countMessage + "EDIT:" + newMessage);
        } else {
            JOptionPane.showMessageDialog (this,
                    "Можно изменять только свои сообщения");
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$ ( );
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel ( );
        panel1.setLayout (new GridLayoutManager (1, 1, new Insets (0, 0, 0, 0), -1, -1));
        panel1.setPreferredSize (new Dimension (700, 470));
        final JPanel panel2 = new JPanel ( );
        panel2.setLayout (new GridLayoutManager (1, 1, new Insets (0, 0, 0, 0), -1, -1));
        panel1.add (panel2, new GridConstraints (0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel ( );
        panel3.setLayout (new BorderLayout (0, 0));
        panel2.add (panel3, new GridConstraints (0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        tykChat = new JPanel ( );
        tykChat.setLayout (new GridLayoutManager (3, 2, new Insets (15, 15, 15, 15), -1, -1));
        panel3.add (tykChat, BorderLayout.CENTER);
        send = new JButton ( );
        send.setForeground (new Color (-12566464));
        send.setText ("Отправить");
        tykChat.add (send, new GridConstraints (2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userOnline = new JTextArea ( );
        userOnline.setForeground (new Color (-12566464));
        userOnline.setLineWrap (true);
        userOnline.setText ("Online");
        tykChat.add (userOnline, new GridConstraints (0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension (120, 360), null, 0, false));
        inputMessages = new JTextField ( );
        inputMessages.setForeground (new Color (-12566464));
        inputMessages.setHorizontalAlignment (2);
        inputMessages.setText ("");
        inputMessages.setToolTipText ("");
        tykChat.add (inputMessages, new GridConstraints (1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension (150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane ( );
        tykChat.add (scrollPane1, new GridConstraints (0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension (800, 600), null, 0, false));
        areaMessage = new JList ( );
        areaMessage.setSelectionMode (2);
        scrollPane1.setViewportView (areaMessage);
        edit = new JButton ( );
        edit.setText ("Редактировать");
        tykChat.add (edit, new GridConstraints (2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }
}