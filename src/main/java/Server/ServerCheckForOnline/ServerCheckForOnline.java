package Server.ServerCheckForOnline;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 Как только есть подключение, открываем новую нить.
 */
public class ServerCheckForOnline extends Thread {
    protected static List<ConnectionCheckForOnline> connectionCheckForOnline =
            Collections.synchronizedList (new ArrayList<ConnectionCheckForOnline> ( ));
    private static final int PORT_ONLINE = 7001;
    private ServerSocket server;
    protected static String allUser = "";

    @Override
    public void run() {
        waitingForConnection ( );
    }

    public void waitingForConnection() {
        try {
            server = new ServerSocket (PORT_ONLINE);
            while (true) {
                Socket socket = server.accept ( );
                ConnectionCheckForOnline newConnect = new ConnectionCheckForOnline (socket, connectionCheckForOnline);
                connectionCheckForOnline.add (newConnect);
                newConnect.start ( );
            }
        } catch (IOException e) {
            e.printStackTrace ( );
        } finally {
            closeAll ( );
        }
    }


    public void closeAll() {
        try {
            server.close ( );
            synchronized (connectionCheckForOnline) {
                for (ConnectionCheckForOnline connectionCheckForOnline : connectionCheckForOnline) {
                    connectionCheckForOnline.close ( );
                }
            }
        } catch (Exception e) {
            System.err.println ("Потоки не были закрыты!");
        }
    }
}
