package Logic;

import GUI.App;
import GUI.SelectionIP;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {
    public Client(String nameWindow) throws HeadlessException, UnknownHostException {
        new SelectionIP ().IPButton ();
    }

    @Override
    public void run() {
        try {
            // Подключаемся в серверу и получаем потоки(in и out) для передачи сообщений
            while (true) {
                Constant.connection = new Socket (Constant.IP, Constant.PORT);
                Constant.input = new ObjectInputStream (Constant.connection.getInputStream ( ));//читаем с сервера
                Constant.output = new ObjectOutputStream (Constant.connection.getOutputStream ( )); //записываем на сервер

                new App ();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace ( );
        } catch (IOException e) {
            e.printStackTrace ( );
        }
    }
}
