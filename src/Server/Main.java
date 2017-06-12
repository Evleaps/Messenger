package Server;

import Server.ServerCheckForOnline.ServerCheckForOnline;
import Server.ServerMessage.ServerMessage;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new Thread (new ServerMessage ()).start ();
        new Thread (new ServerCheckForOnline ()).start ();
    }
}
