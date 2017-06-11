package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CheckForOnline  {
    public static final int PORT_ONLINE = 7001;
    private List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
    private ServerSocket server;
    private String allUser = "";

    public static void main(String[] args) {
        new CheckForOnline ();
    }

    public CheckForOnline() {
        try {
            server = new ServerSocket(PORT_ONLINE);
            while (true) {
                Socket socket = server.accept();
                Connection con = new Connection(socket);
                connections.add(con);
                con.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    private void closeAll() {
        try {
            server.close();
            synchronized(connections) {
                Iterator<Connection> iter = connections.iterator();
                while(iter.hasNext()) {
                    ((Connection) iter.next()).close();
                }
            }
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты!");
        }
    }

    private class Connection extends Thread {
        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;

        public Connection(Socket socket) {
            this.socket = socket;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

            } catch (IOException e) {
                e.printStackTrace();
                close();
            }
        }

        @Override
        public void run() {
            try {
                String loginUser;
                while (socket.isConnected ()) {
                    loginUser = in.readLine();
                    String[] user = allUser.split ("\\p{Punct}");

                    boolean coincidence = false;
                    for (String s : user) {
                        if (s.equals (loginUser)) coincidence = true;
                    }
                    if (coincidence == false) allUser += (loginUser.concat ("/"));

                    // Отправляем всем клиентам список активных пользователей
                    synchronized(connections) {
                        Iterator<Connection> iter = connections.iterator();
                        while(iter.hasNext()) {
                            ((Connection) iter.next()).out.println(allUser);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        public void close() {
            try {
                in.close();
                out.close();
                socket.close();

                // Если больше не осталось соединений, закрываем всё, что есть и
                // завершаем работу сервера
                connections.remove(this);
                if (connections.size() == 0) {
                    CheckForOnline.this.closeAll();
                    System.exit(0);
                }
            } catch (Exception e) {
                System.err.println("Потоки не были закрыты!");
            }
        }
    }
}