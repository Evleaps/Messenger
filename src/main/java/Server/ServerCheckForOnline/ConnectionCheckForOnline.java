package Server.ServerCheckForOnline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ConnectionCheckForOnline extends Thread {
    private List<ConnectionCheckForOnline> connectionCheckForOnline;
    private BufferedReader in;
    private ObjectOutputStream out;
    private Socket socket;
    private String loginUser;


    public ConnectionCheckForOnline(Socket socket,
                                    List<ConnectionCheckForOnline> connectionCheckForOnlines) {
        this.connectionCheckForOnline = connectionCheckForOnlines;
        this.socket = socket;
/**меняем кодировку на Windows-1251, для верного отображения при запуске из Windows*/
        try {
            in = new BufferedReader (new InputStreamReader (socket.getInputStream ( ),"Windows-1251"));
            out = new ObjectOutputStream (socket.getOutputStream ( ));
        } catch (IOException e) {
            e.printStackTrace ( );
            close ( );
        }
    }

    /**Нам передают логин, мы проверяем, является ли он новым или уже хранится в истории allUser, если используется,
     * мы добавляем новичка в список имен. */
    @Override
    public void run() {
        try {
            while (socket.isConnected ( )) {
                loginUser = in.readLine ( );
                String[] user = ServerCheckForOnline.allUser.split ("\n");

                boolean coincidence = false;
                for (String s : user) {
                    if (s.equals (loginUser)) coincidence = true;
                }
                if (coincidence == false) ServerCheckForOnline.allUser += (loginUser.concat ("\n"));

                // Отправляем всем клиентам список активных пользователей
                synchronized (connectionCheckForOnline) {
                    for (ConnectionCheckForOnline connection : connectionCheckForOnline) {
                        connection.out.writeObject (ServerCheckForOnline.allUser);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace ( );
        } finally {
            //удаляем логин отключенного пользователя из allUser, теперь его логин снова можно занять!
            //удаляем нить, закрываем потоки
            ServerCheckForOnline.allUser =
                    ServerCheckForOnline.allUser.replaceAll (loginUser.concat ("\n"),"");
            ServerCheckForOnline.connectionCheckForOnline.remove (this);
            close ();
        }
    }

    public void close() {
        try {
            in.close ( );
            out.close ( );
            socket.close ( );
        } catch (Exception e) {
            System.err.println ("Потоки не были закрыты!");
        }
    }
}
