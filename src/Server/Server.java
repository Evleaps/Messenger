package Server;

import Client.Constant;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ромчи on 01.06.2017.
 */
public class Server {
    private static ServerSocket server;
    private static List<ConnectedClient> clients = new ArrayList<> ( );
    private static StringBuffer historyMassages = new StringBuffer ("История сообщений: ");

    /**
     * Сервер ждет подключение в бесконечном цикле, если есть подключение, отправляем в список подключенных клиентов
     * Список клиентов clients содержит каждого клиента как новый поток
     * Если в historyMassages больше 5000 символов, удаляем ранние сообщения
     */
    public static void main(String[] args) {
        try {
            new Thread (new CheckForOnline()).start ();//проверка: в сети ли пользователь?
            server = new ServerSocket (Constant.PORT_MESSAGE);
            while (true) {
                ConnectedClient client = new ConnectedClient (server.accept ( ));
                clients.add (client);
                client.start ( );
            }
        } catch (IOException e) {
            e.printStackTrace ( );
        }
    }

    private static class ConnectedClient extends Thread {
        private static Socket connection;
        private static ObjectInputStream input;
        private static ObjectOutputStream output;


        public ConnectedClient(Socket socket) throws IOException {
            connection = socket;
            System.out.println ("К серверу подключен пользователь...");
            output = new ObjectOutputStream (connection.getOutputStream ( )); //Пишем в чат
            input = new ObjectInputStream (connection.getInputStream ( )); //читаем с сервера
        }

        @Override
        public void run() {
            try {
                while (connection.isConnected ()) {
                    String str = (String) input.readObject();
                    historyMassages.append (str);

                    if (historyMassages.length ( ) > 5000)
                        historyMassages.delete (5000, historyMassages.length ( ));

                    for (ConnectedClient empty : Server.clients) {
                        empty.output.writeObject (historyMassages.toString ( ));
                        empty.output.flush ();
                    }
                }
            } catch (IOException e) {

            } catch (ClassNotFoundException e) {

            } finally {
                System.out.println ("Пользователь отключился");
            }
        }
    }
}