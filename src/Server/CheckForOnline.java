package Server;

import Client.Constant;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Ромчи on 06.06.2017.
 */
public class CheckForOnline implements Runnable {
    private static ServerSocket server;
    private static List<ConnectedClient> clients = new ArrayList<> ( );
    private static Set<String> nameUser = new HashSet<> (Constant.userLimit);


    @Override
    public void run() {
        try {
            server = new ServerSocket (Constant.PORT_ONLINE);
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
        private static Socket             connection;
        private static ObjectInputStream  input;
        private static ObjectOutputStream output;
        private static String             loginUser;


        public ConnectedClient(Socket socket) throws IOException {
            connection = socket;
            output = new ObjectOutputStream (connection.getOutputStream ()); //Пишем в чат
            input = new ObjectInputStream (connection.getInputStream ()); //читаем с сервера
        }

        @Override
        public void run() {
            try {
                while (connection.isConnected ()) {
                    loginUser = (String) input.readObject ();
                    nameUser.add (loginUser);
                    String buff = "";
                    for (String empty : nameUser) {
                        buff += empty + "\n";
                    }
                    output.writeObject (buff);
                }
            } catch (IOException e) {
            } catch (ClassNotFoundException e) {
            }finally {
                nameUser.remove (loginUser);
                System.out.println (loginUser + " перестал быть онлайн");
            }
        }
    }
}
