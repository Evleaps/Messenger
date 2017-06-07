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
    StringBuffer historyMassages = new StringBuffer ("История сообщений: ");;

    /**Сервер ждет подключение в бесконечном цикле, когда приходит сообщение, добавляем его в историю(буфер), отсылаем обратно всю историю.
     * P/S если больше 5000 символов в истории, удаляем лишнее, экономим память*/
    @Override
    public void run() {
        try {
            server = new ServerSocket (Constant.PORT_MESSAGE,Constant.userLimit);
            while (true) {
                if (connection != null) connection.close ();
                connection = server.accept ();//возвращает сокет который получил
                System.out.println ("К серверу подключен пользователь..." );
                output = new ObjectOutputStream (connection.getOutputStream ()); //Пишем в чат
                input = new ObjectInputStream (connection.getInputStream ()); //читаем с сервера

                String str = (String)input.readObject ();
                historyMassages.append (str);

                if (historyMassages.length () > 5000)
                    historyMassages.delete (5000,historyMassages.length ());

                output.writeObject (historyMassages.toString ());
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