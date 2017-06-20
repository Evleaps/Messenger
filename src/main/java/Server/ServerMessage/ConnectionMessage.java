package Server.ServerMessage;

import Server.ConstantServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConnectionMessage extends Thread {
    private List<ConnectionMessage> allConnectionMessages;
    private ObjectOutputStream      out;
    private BufferedReader          in;
    private Socket                  socket;

    private SimpleDateFormat date = new SimpleDateFormat ("HH:mm:ss");
    private StringBuffer historyMessage;
    private String       loginUser = "";
    private String       message   = "";



    public ConnectionMessage(Socket socket, List<ConnectionMessage> allConnectionMessages, StringBuffer historyMessage) {
        this.allConnectionMessages = allConnectionMessages;
        this.historyMessage = historyMessage;
        this.socket = socket;
/**
 В случае разрыва соединения, потоки закрываются, сокет закрывается. История храниться в ServerMessage, записываем в
 начало истории каждое новое сообщение, что-бы в GUI новое сообщение было вверху,
 меняем кодировку на Windows-1251, для верного отображения при запуске из Windows*/
        try {
            in = new BufferedReader(new InputStreamReader (socket.getInputStream(),"Windows-1251"));
            out = new ObjectOutputStream (socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    @Override
    public void run() {
        try {
            loginUser = in.readLine();
            historyMessage.insert (0,"Подключился пользователь: \"" + loginUser + "\" \n");
            sendToAllUsers ();
/**1. Если содержит маркер на исправление "EDIT" то...(маркер устанавливается клиентом в GUI.editMessage())
 * удаляем всю историю и перезаписываем ее заного с учетом исправленного текста
 * иначе это обычное сообщение и мы его просто добавляем в историю*/
            while (true) {
                message = in.readLine();
                if (message.split (":")[0].contains ("EDIT")) {
                    String editCount = message.split ("EDIT:")[0];
                    String[] searchSMS = historyMessage.toString ().split ("\n");
                    historyMessage.delete (0,historyMessage.length ());
                    for (String entry : searchSMS) {
                        if (entry.split (" ")[0].equals (editCount)) {
                            String str = editCount
                                    + " " + loginUser + " "
                                    + date.format (new Date ( )).toString ( )
                                    + " - " + message.split ("EDIT:")[1];
                            historyMessage.append (str + "\n");
                        }else {
                            historyMessage.append (entry + "\n");
                        }
                    }
                } else {
                    historyMessage.insert (0, ConstantServer.countMessages
                            + " " + loginUser + " "
                            + date.format (new Date ( )).toString ( )
                            + " - " + message + "\n");
                    ConstantServer.countMessages++;
                }
                sendToAllUsers ();//в любом случае мы отправим сообщение пользователям
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.err.println ("Пользователь: \"" + loginUser + "\" отключился!" );
            historyMessage.insert (0,"Пользователь: \"" + loginUser + "\" отключился! \n");
            allConnectionMessages.remove (this);
            close();
            try {
                sendToAllUsers (); //сообщаем всем активным пользователям, что кто-то выбыл
            } catch (IOException e) {
                e.printStackTrace ( );
            }
        }
    }

    private void sendToAllUsers() throws IOException {
        // Отправляем всем клиентам очередное сообщение
        synchronized (allConnectionMessages) {
            for (ConnectionMessage connectionMessage : allConnectionMessages) {
                connectionMessage.out.writeObject (historyMessage.toString ());
            }
        }
    }
    /**Записывем в историю сообщение, об отключении пользователя, удаляем его нить, закрываем все потоки и сокет*/
   protected void close() {
        try {
            in.close();
            out.close();
            socket.close();

            if (allConnectionMessages.isEmpty () && ConstantServer.clearHistory == true)
                historyMessage.delete (0,historyMessage.length ()); //все вышли, можно и историю удалить
        } catch (Exception e) {
            System.err.println ("Потоки не были закрыты!");
            e.getStackTrace ();
        }
    }
}


