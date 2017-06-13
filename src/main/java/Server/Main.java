package Server;


import Server.ServerCheckForOnline.ServerCheckForOnline;
import Server.ServerMessage.ServerMessage;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerMessage serverMessage = new ServerMessage ();
        serverMessage.start();
        ServerCheckForOnline serverCheckForOnline = new ServerCheckForOnline ();
        serverCheckForOnline.start ();

        Scanner scanner = new Scanner (System.in);
        System.out.println ("Для прекращения работы сервера введите: \"exit\"" );

        while (true) {
            String exit = scanner.nextLine ();
            if (exit.equals ("exit")) {
                 serverMessage.closeAll ();
                 serverCheckForOnline.closeAll ();
                 System.exit (0);
            }
        }
    }
}
