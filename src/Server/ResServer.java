package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ромчи on 01.06.2017.
 */
public class ResServer {
    public static final int     PORT_MESSAGE = 7000;
    private static ServerSocket server;
    private static List<ConnectedClient> clients = Collections.synchronizedList ( new ArrayList<ConnectedClient> () );
    private static StringBuffer historyMassages = new StringBuffer ("История сообщений: ");

    /**
     * Сервер ждет подключение в бесконечном цикле, если есть подключение, отправляем в список подключенных клиентов
     * Список клиентов clients содержит каждого клиента как новый поток
     * Если в historyMassages больше 5000 символов, удаляем ранние сообщения
     */
    public static void main(String[] args) throws IOException {
        try {
            new Thread (new CheckForOnline()).start ();//проверка: в сети ли пользователь?
            server = new ServerSocket (PORT_MESSAGE);
            while (true) {
                ConnectedClient client = new ConnectedClient (server.accept ( ));
                System.out.println ("Колличество клиентов main = " + clients.size () );
                clients.add (client);
                ConnectedClient.nameClient = client;
                client.start ( );
            }
        } catch (IOException e) {
            e.printStackTrace ( );
        } finally {
            server.close ();
        }
    }

    private static class ConnectedClient extends Thread {
        private static Socket connection;
        private static ObjectInputStream input;
        private static ObjectOutputStream output;
        protected static ConnectedClient nameClient;
        private static String messages;



        public ConnectedClient(Socket socket) throws IOException {
            connection = socket;
            System.out.println ("К серверу подключен пользователь...");
            output = new ObjectOutputStream (connection.getOutputStream ( )); //Пишем в чат
            input = new ObjectInputStream (connection.getInputStream ( )); //читаем с сервера
            output.writeObject (CheckForOnline.nameUser);
            output.flush ();
            System.out.println ("Сервер передал новому пользователю список занятых логинов" );
        }

        @Override
        public void run() {
            try {
                System.out.println ("Колличество клиентов run = " + clients.size () );
                while (connection.isConnected ()) {
                    messages = (String) input.readObject();
                    historyMassages.append (messages);

                    if (historyMassages.length ( ) > 5000)
                        historyMassages.delete (5000, historyMassages.length ( ));

                    synchronized (clients) {
                        for (ConnectedClient empty : ResServer.clients) {
                            empty.output.writeObject (historyMassages.toString ( ));
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println ("ой" );
            } catch (ClassNotFoundException e) {
                System.out.println ("ойой" );
            } finally {
                clients.remove (nameClient);
                System.out.println ("Пользователь отключился");
                System.out.println ("Количество клиентов = " + clients.size () );
            }
        }
    }
}