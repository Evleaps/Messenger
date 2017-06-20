package Client;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;

/** Класс в вечном цикле принимает сообщение с сервера, как только что-то получил,
 * добавляет в чат. Ожидает следующее сообщ.*/
public class ReceiveMessage extends Thread{
    private ObjectInputStream inputMessage;
    private JList areaMessage;

    public ReceiveMessage(ObjectInputStream inputMessage, JList areaMessage) {
        this.inputMessage = inputMessage;
        this.areaMessage = areaMessage;
    }

    public void run() {
        try {
            areaMessage.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);//можно выбрать только 1 запись в JList
            while (true) {
                String str = inputMessage.readObject ().toString ();
                String[] allMessage = str.split ("\n");
                areaMessage.setListData (allMessage);
            }
        } catch (IOException e) {
            System.err.println ("Ошибка при получении сообщения.");
            e.printStackTrace ( );
        } catch (ClassNotFoundException e) {
            e.printStackTrace ( );
        }
    }
}


