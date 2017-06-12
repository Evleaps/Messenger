package Server.ServerMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConnectionMessage extends Thread {
    private List<ConnectionMessage> allConnectionMessages;
    private BufferedReader in;
    private ObjectOutputStream out;
    private Socket socket;
    private String loginUser = "";
    String message = "";

    public ConnectionMessage(Socket socket, List<ConnectionMessage> allConnectionMessages) {
        this.allConnectionMessages = allConnectionMessages;
        this.socket = socket;
/**
В случае разрыва соединения, потоки закрываются, сокет закрывается. История храниться в ServerMessage, записываем в
 начало истории каждое новое сообщение, что-бы в GUI новое сообщение было вверху */
        try {
            in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
            out = new ObjectOutputStream (socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    @Override
    public void run() {
        try {
            loginUser = in.readLine();
            ServerMessage.historyMessage.append ("Подключился пользователь: \"" + loginUser + "\"\n");

            // Отправляем всем клиентам сообщение о том, что зашёл новый пользователь
            synchronized(allConnectionMessages) {
                for (ConnectionMessage connectionMessage : allConnectionMessages) {
                    connectionMessage.out.writeObject (ServerMessage.historyMessage.toString ());
                }
            }

            while (true) {
                SimpleDateFormat date = new SimpleDateFormat ("HH:mm:ss");
                message = in.readLine();
                ServerMessage.historyMessage.append (loginUser + " "
                        + date.format (new Date ()).toString ()
                        + "\n" + message + "\n");
                System.out.println (ServerMessage.historyMessage.toString () );
                // Отправляем всем клиентам очередное сообщение
                synchronized(allConnectionMessages) {
                    for (ConnectionMessage connectionMessage : allConnectionMessages) {
                        connectionMessage.out.writeObject (ServerMessage.historyMessage.toString ());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println ("Пользователь \"" + loginUser + "\" отключился!" );
            ServerMessage.historyMessage.append ("Пользователь \"" + loginUser + "\" отключился! \n");
            ServerMessage.allConnectionMessages.remove (this);
            close();
        }
    }

    /**Записывем в историю сообщение, об отключении пользователя, удаляем его нить, закрываем все потоки и сокет*/

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.out.println ("Потоки не были закрыты!");
            e.getStackTrace ();
        }
    }
}

