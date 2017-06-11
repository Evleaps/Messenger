package Client1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GUI extends JFrame {
    private JPanel     tykChat;
    private JButton    send;
    private JTextArea  userChat;
    private JTextField messages;
    private JTextArea  chat;

    private BufferedReader inputMessage;
    private PrintWriter    outputMessage;
    private Socket         connectionMessage;

    private BufferedReader inputCheckForOnline;
    private PrintWriter    outputCheckForOnline;
    private Socket         connectionCheckForOnline;

    private String         activeLogin;

    public static void main(String[] args) {
        new GUI ();
    }

    public GUI() {
        new SelectionIP ().IPButton ();//пользователь выбирает адресс подключения
        try {
            // Подключаемся в серверам и получаем потоки для передачи сообщений
            connectionMessage = new Socket (Constant.IP, Constant.PORT_MESSAGE);
            inputMessage = new BufferedReader (new InputStreamReader (connectionMessage.getInputStream ( )));
            outputMessage = new PrintWriter (connectionMessage.getOutputStream ( ), true);

            connectionCheckForOnline = new Socket (Constant.IP, Constant.PORT_ONLINE);
            inputCheckForOnline = new BufferedReader (new InputStreamReader (connectionCheckForOnline.getInputStream ( )));//читаем с сервера
            outputCheckForOnline = new PrintWriter (connectionCheckForOnline.getOutputStream ( ), true); //записываем на сервер

            //Вводим уникальный в чате логин
            selectLogin ();

            // Запускаем вывод всех входящих сообщений в консоль
            new Thread (new ReceiveMessage (inputMessage,chat)).start ();
            //Запускаем поток проверки на Online
            new Thread (new CheckForOnline (outputCheckForOnline, inputCheckForOnline,  userChat)).start ();

            setContentPane (tykChat);
            setSize (Constant.WIDTH_GUI, Constant.HEIGHT_GUI);
            setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo (null); //окно всплывет в центре монитора
            setVisible (true);
            userChat.setText ("Участники беседы:" + "\n" + activeLogin + Constant.LOGIN);
            System.out.println (Constant.LOGIN + " появился в сети!");


            send.addActionListener (new ActionListener ( ) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource ( ) == send)
                        sendMessage (messages.getText ( ));
                }
            });

        } catch (Exception e) {
            e.printStackTrace ( );
        }
    }

    private void selectLogin() throws IOException {
        /** читаем с сервера список занятых логинов, передаем его в качестве параметра, выбираем свободный
         * SelectionLogin установит логин в Constant и мы отправим наш логин на сервер
         * */
        outputCheckForOnline.println ();
        activeLogin = inputCheckForOnline.readLine ();
        System.out.println ("занятые логины >>" + activeLogin + "<<" );
        new SelectionLogin ( ).Login (activeLogin);
        outputMessage.println (Constant.LOGIN);
    }

    private void sendMessage(Object messagesText) {
        SimpleDateFormat date = new SimpleDateFormat ("HH:mm:ss");
        outputMessage.println (date.format (new Date ( )) + " - "
                + messagesText.toString ());

        messages.setText ("");//после отправки поле сообщения очищается.
    }
}