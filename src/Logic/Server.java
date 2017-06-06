package Logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Ромчи on 01.06.2017.
 */
public class Server implements Runnable {
    private static Socket             connection;
    private static ServerSocket       server;
    private static ObjectInputStream  input;
    private static ObjectOutputStream output;

    @Override
    public void run() {
        try {
            server = new ServerSocket (Constant.PORT_MESSAGE,Constant.userLimit);
            while (true) {
                if (connection != null) connection.close ();
                connection = server.accept ();//возвращает сокет который получил
                output = new ObjectOutputStream (connection.getOutputStream ()); //Пишем в чат
                input = new ObjectInputStream (connection.getInputStream ()); //читаем с сервера
                output.writeObject ((String)input.readObject ());
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