package Client1;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**

 */
public class CheckForOnline extends Thread {
    private PrintWriter    outputCheckForOnline;
    private BufferedReader inputCheckForOnline;
    private JTextArea      userChat;

    public CheckForOnline(PrintWriter outputCheckForOnline, BufferedReader inputCheckForOnline, JTextArea userChat) {
        this.outputCheckForOnline = outputCheckForOnline;
        this.inputCheckForOnline = inputCheckForOnline;
        this. userChat =  userChat;
    }

    @Override
    public void run(){
        try {
            String all;
            while (true){
                outputCheckForOnline.println (Constant.LOGIN);
                String[] allUser = inputCheckForOnline.readLine ().split ("\\p{Punct}");
                all = "";
                for (String s : allUser) {
                    if (s.length () > Constant.MIN_SYMBOL) all += s + "\n";
                }
                userChat.setText ("Участники беседы: " + "\n" + all);
                Thread.sleep (Constant.SLEEP_ONLINE);
            }

        } catch (IOException e1) {
            e1.printStackTrace ( );
        } catch (InterruptedException e) {
            e.printStackTrace ( );
        }
    }
}
