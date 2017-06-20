package Server.ServerMessage;

import Server.ConstantServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/** Сервер бесконечно ожидает подключение. Если оно появилось, то создает новый поток для каждого клиента
 * Список allConections содержит всех пользователей, получив вообщ мы отправляем его всем пользователям
 * из списка используя foreach. См.класс Connections*/
public class ServerMessage extends Thread {
    private static List<ConnectionMessage> allConnectionMessages =
            Collections.synchronizedList (new ArrayList<ConnectionMessage> ());
    private static ServerSocket server;
    private static StringBuffer historyMessage = new StringBuffer ();

    @Override
    public void run() {
        waitingForConnection ();
    }

    public void waitingForConnection() {
        try {
            server = new ServerSocket (ConstantServer.PORT_MESSAGE);
            while (true) {
                Socket socket = server.accept ( );
                ConnectionMessage newConected = new ConnectionMessage (socket, allConnectionMessages, historyMessage);
                allConnectionMessages.add (newConected);
                newConected.start ( );
            }
        } catch (IOException e) {
            e.printStackTrace ();
        } finally {
            closeAll ();
        }
    }

    public void closeAll() {
        try {
            server.close ( );
            synchronized (allConnectionMessages) {
                for (ConnectionMessage connectionMessage : allConnectionMessages) {
                    connectionMessage.close ();
                }
            }
        } catch (Exception e) {
            System.err.println ("Потоки не были закрыты!");
        }
    }
}



