package Client1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


/**
 * Обеспечивает работу программы в режиме клиента
 *
 * @author Влад
 */
public class Client {

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        Scanner scan = new Scanner(System.in);
        new SelectionIP().IPButton ();//пользователь выбирает адресс подключения
        String activeLogin = "";
        try {
            // Подключаемся в серверу и получаем потоки(in и out) для передачи сообщений
            socket = new Socket(Constant.IP, Constant.PORT_MESSAGE);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            new SelectionLogin().Login (activeLogin);
            out.println(Constant.LOGIN);

            // Запускаем вывод всех входящих сообщений в консоль
            Resender resend = new Resender();
            resend.start();

            // Пока пользователь не введёт "exit" отправляем на сервер всё, что
            // введено из консоли
            String str = "";
            while (!str.equals("exit")) {
                str = scan.nextLine();
                out.println(str);
            }
            resend.setStop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    /**
     * Закрывает входной и выходной потоки и сокет
     */
    private void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты!");
        }
    }

    /**
     * Класс в отдельной нити пересылает все сообщения от сервера в консоль.
     * Работает пока не будет вызван метод setStop().
     *
     * @author Влад
     */
    private class Resender extends Thread {

        private boolean stoped;

        /**
         * Прекращает пересылку сообщений
         */
        public void setStop() {
            stoped = true;
        }

        /**
         * Считывает все сообщения от сервера и печатает их в консоль.
         * Останавливается вызовом метода setStop()
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            try {
                while (!stoped) {
                    String str = in.readLine();
                    System.out.println(str);
                }
            } catch (IOException e) {
                System.err.println("Ошибка при получении сообщения.");
                e.printStackTrace();
            }
        }
    }

}