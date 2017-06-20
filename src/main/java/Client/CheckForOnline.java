package Client;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

/**
Проверка на Online, отправляем на сервер логин, читаем с него строку с всеми активными логинами
 в данный момент.
 Проверка на s.length () > ConstantServer.MIN_SYMBOL нужна, что-бы точно исключить возможные лишние \n
 Ожидание Thread.sleep нужно для того, что-бы не нагружать ресурсы системы.
 */
public class CheckForOnline extends Thread {
    private PrintWriter    outputCheckForOnline;
    private ObjectInputStream inputCheckForOnline;
    private JTextArea      userChat;

    public CheckForOnline(PrintWriter outputCheckForOnline, ObjectInputStream inputCheckForOnline, JTextArea userChat) {
        this.outputCheckForOnline = outputCheckForOnline;
        this.inputCheckForOnline = inputCheckForOnline;
        this. userChat = userChat;
    }

    @Override
    public void run(){
        try {
            while (true){
                outputCheckForOnline.println (ConstantClient.LOGIN);
                String[] allUserOnline = inputCheckForOnline.readObject ().toString ().split ("\n");
                String all = "";
                for (String s : allUserOnline) {
                    if (s.length () > ConstantClient.MIN_SYMBOL) all +=  s + "\n";
                }

                userChat.setText ("Участники беседы: " + "\n" + all);
                Thread.sleep (ConstantClient.SLEEP_ONLINE);
            }

        } catch (IOException e1) {
            e1.printStackTrace ( );
        } catch (InterruptedException e) {
            e.printStackTrace ( );
        } catch (ClassNotFoundException e) {
            e.printStackTrace ( );
        }
    }
}
