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
        System.out.println ("Для прекращения работы сервера введите: \"exit\"." );
        System.out.println ("Внимание! \nКаждый раз, после отключения всех пользователей от сети история сообщений удаляется! " +
                "\nЕсли хотите оставить историю введите: \"false\" иначе: \"true\" (стоит по умолчанию)");

        while (true) {
            String command = scanner.nextLine ();
            switch (command) {
                case "exit":
                    serverMessage.closeAll ();
                    serverCheckForOnline.closeAll ();
                    System.exit (0);
                    break;
                case "true":
                    ConstantServer.clearHistory = true;
                    System.out.println ("Настройки применены, история будет удаляться." );
                    break;
                case "false":
                    ConstantServer.clearHistory = false;
                    System.out.println ("Настройки применены, история НЕ будет удаляться." );
                    break;
            }
        }
    }
}
