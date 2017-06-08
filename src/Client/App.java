package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ромчи on 02.06.2017.
 */
public class App extends JFrame implements Runnable {
    private JPanel      tykChat;
    private JTextField  messages;
    private JButton     send;
    private JTextArea   chat;
    private JTextArea   userChat;

    private Socket              connection;
    private ObjectInputStream   input;
    private ObjectOutputStream  output;

    private Socket              connectionCheckOn;
    private ObjectInputStream   inputCheckOn;
    private ObjectOutputStream  outputCheckOn;

    public static void main(String[] args) throws UnknownHostException {
        new Thread (new App ()).start ();
    }

    public App() {
        new SelectionIP().IPButton ();//пользователь выбирает адресс подключения
        send.addActionListener (new ActionListener ( ) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource () == send)
                    Send (messages.getText ( ));
            }
        });
    }

    @Override
    public void run() {
        String activeLogin;
        System.out.println ("Запущен Клиент..." );
        try {
            connection = new Socket (Constant.IP, Constant.PORT_MESSAGE);
            input = new ObjectInputStream (connection.getInputStream ());//читаем с сервера
            output = new ObjectOutputStream (connection.getOutputStream ()); //записываем на сервер

            activeLogin = input.readObject().toString ();//список активных\занятых логинов
            new SelectionLogin().Login (activeLogin);//Идентификация пользователя(Ввод логина)
            if (Constant.LOGIN == null) System.exit (0);//в случе если нажмет крестик на вводе логина

            setContentPane (tykChat);
            setSize (400, 700);
            setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo (null); //окно вылезет в центре монитора
            setVisible (true);
            userChat.setText ("Участники беседы:" + "\n" + activeLogin + Constant.LOGIN);
            System.out.println (Constant.LOGIN + " появился в сети!" );

            while (true) {
                Thread.sleep (5000);
                UserOnline();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace ( );
        } catch (IOException e) {
            e.printStackTrace ( );
        } catch (InterruptedException e) {
            e.printStackTrace ( );
        } catch (ClassNotFoundException e) {
            e.printStackTrace ( );
        }
    }


    private void Send(Object messagesText) {
        try {
            SimpleDateFormat date = new SimpleDateFormat ("HH:mm:ss");
            output.flush ();
            output.writeObject ( "\n" + Constant.LOGIN + ": "
                    + date.format (new Date ()) + "\n"
                    + messagesText.toString ());

            String str = input.readObject ().toString ();
            chat.setText (str);
            messages.setText (null);//после отправки поле сообщения очищается.
        } catch (IOException e) {
            e.printStackTrace ( );
        } catch (ClassNotFoundException e) {
            e.printStackTrace ( );
        }
    }

    private void UserOnline() {
        try {
            connectionCheckOn = new Socket (Constant.IP, Constant.PORT_ONLINE);
            inputCheckOn = new ObjectInputStream (connectionCheckOn.getInputStream ());//читаем с сервера
            outputCheckOn = new ObjectOutputStream (connectionCheckOn.getOutputStream ()); //записываем на сервер

            outputCheckOn.writeObject (Constant.LOGIN);
            userChat.setText ("Участники беседы:" + "\n"
                    + inputCheckOn.readObject().toString ());
        } catch (IOException e) {
            e.printStackTrace ( );
        } catch (ClassNotFoundException e) {
            e.printStackTrace ( );
        }
    }
}