package Logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ромчи on 06.06.2017.
 */
public class CheckForOnline implements Runnable {
    private static Socket             connection;
    private static ServerSocket       server;
    private static ObjectInputStream  input;
    private static ObjectOutputStream output;
    private static Set<String> nameUser = new HashSet<> (Constant.userLimit);


    @Override
    public void run() {
        try {
            server = new ServerSocket (Constant.PORT_ONLINE,Constant.userLimit);
            System.out.println ("Проверка пользователя на сеть" );
            while (true) {
                if (connection != null) connection.close ();
                connection = server.accept ();//возвращает сокет который получил
                System.out.println ("Сервер ONLINE подключил пользователя" );
                output = new ObjectOutputStream (connection.getOutputStream ()); //Пишем в чат
                input = new ObjectInputStream (connection.getInputStream ()); //читаем с сервера

                nameUser.add ((String)input.readObject ());
                String nameAll = "";
                for (String empty : nameUser) {
                    nameAll += empty + "\n";
                }
                Constant.userInChat = true;
                output.writeObject (nameAll);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace ( );
        } catch (ClassNotFoundException e) {
            e.printStackTrace ( );
        } catch (IOException e) {
            e.printStackTrace ( );
        }
    }
}
