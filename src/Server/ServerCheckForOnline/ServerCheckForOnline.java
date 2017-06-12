package Server.ServerCheckForOnline;


import Server.ServerMessage.ServerMessage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ромчи on 12.06.2017.
 */
public class ServerCheckForOnline extends ServerMessage {
    protected static List<ConnectionCheckForOnline> connectionCheckForOnline =
            Collections.synchronizedList(new ArrayList<ConnectionCheckForOnline> ());
    private static final int PORT_ONLINE = 7001;
    private ServerSocket server;
    protected static String allUser = "";

    @Override
    public void waitingForConnection() {
        try {
            server = new ServerSocket(PORT_ONLINE);
            while (true) {
                Socket socket = server.accept();
                ConnectionCheckForOnline newConnect = new ConnectionCheckForOnline (socket, connectionCheckForOnline);
                connectionCheckForOnline.add(newConnect);
                newConnect.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    @Override
    public void closeAll() {
        super.closeAll ( );
    }
}
