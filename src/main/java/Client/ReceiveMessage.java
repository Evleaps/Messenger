package Client;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;

/** Класс в вечном цикле принимает сообщение с сервера, как только что-то получил, добавляет в чат. Ожидает следующее сообщ.*/
public class ReceiveMessage  extends Thread{
    private ObjectInputStream inputMessage;
    private JTextArea chat;

    public ReceiveMessage(ObjectInputStream inputMessage,JTextArea chat) {
        this.inputMessage = inputMessage;
        this.chat = chat;
    }

    public void run() {
        try {
            while (true) {
                String str = inputMessage.readObject ().toString ();
                chat.setText (str);
            }
        } catch (IOException e) {
            System.err.println ("Ошибка при получении сообщения.");
            e.printStackTrace ( );
        } catch (ClassNotFoundException e) {
            e.printStackTrace ( );
        }
    }
}

