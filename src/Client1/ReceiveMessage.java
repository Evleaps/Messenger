package Client1;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;

/** Класс в вечном цикле принимает сообщение с сервера, как только что-то получил, добавляет в чат. Ожидает следующее сообщ.*/
public class ReceiveMessage  extends Thread{
    private BufferedReader inputMessage;
    private JTextArea  chat;

    public ReceiveMessage(BufferedReader inputMessage,JTextArea chat) {
        this.inputMessage = inputMessage;
        this.chat = chat;
    }

    public void run() {
        System.out.println ("Работает ран" );
        try {
            while (true) {
                String str = inputMessage.readLine ( );
                chat.setText (str);
            }
        } catch (IOException e) {
            System.err.println ("Ошибка при получении сообщения.");
            e.printStackTrace ( );
        }
    }
}

