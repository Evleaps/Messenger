package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Ромчи on 12.06.2017.
 */
public class GUI extends JFrame{
    private JPanel     tykChat;
    private JButton    send;
    private JTextArea  userChat;
    private JTextField messages;
    private JTextArea  chat;
    private JScrollPane scroll;

    private ObjectInputStream inputMessage;
    private PrintWriter outputMessage;
    private Socket connectionMessage;

    private ObjectInputStream inputCheckForOnline;
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
            inputMessage = new ObjectInputStream (connectionMessage.getInputStream ( ));
            outputMessage = new PrintWriter (connectionMessage.getOutputStream ( ), true);

            connectionCheckForOnline = new Socket (Constant.IP, Constant.PORT_ONLINE);
            inputCheckForOnline = new ObjectInputStream (connectionCheckForOnline.getInputStream ( ));//читаем с сервера
            outputCheckForOnline = new PrintWriter (connectionCheckForOnline.getOutputStream ( ), true); //записываем на сервер
        }catch (IOException e) {
            e.getStackTrace ();
        }

        //Вводим уникальный в чате логин
        selectLogin ( );

        // Запускаем вывод всех входящих сообщений в консоль
        new Thread (new ReceiveMessage (inputMessage,chat)).start ();
        //Запускаем поток проверки на Online
        new Thread (new CheckForOnline (outputCheckForOnline, inputCheckForOnline,  userChat)).start ();

        setContentPane (tykChat);
        pack ();
        //setSize (Constant.WIDTH_GUI, Constant.HEIGHT_GUI);
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
    }
    /** читаем с сервера список занятых логинов, передаем его в качестве параметра, выбираем свободный
     * SelectionLogin установит логин в Constant и мы отправим наш логин на сервер
     * */
    private void selectLogin() {
        outputCheckForOnline.println ();
        try {
            activeLogin = inputCheckForOnline.readObject ().toString ();
        } catch (ClassNotFoundException e) {
            e.printStackTrace ( );
        } catch (IOException e) {
            e.printStackTrace ( );
        }
        System.out.println ("занятые логины >>" + activeLogin + "<<" );
        try {
            new SelectionLogin ( ).Login (activeLogin);
        }catch (NullPointerException e) {
            System.exit (0);
        }
        outputMessage.println (Constant.LOGIN);
    }
    /**При нажитии на кнопку "отправить" мы передаем на сервер строку содержащую дату и текст сообщения */
    private void sendMessage(Object messagesText) {
        outputMessage.println (messagesText.toString ());
        messages.setText ("");//после отправки поле сообщения очищается.
    }
}