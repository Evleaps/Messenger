package Logic;

import GUI.App;
import java.net.UnknownHostException;

/**
 * Created by Ромчи on 02.06.2017.
 */
public class Main {
    public static void main(String[] args) throws UnknownHostException {
        new Thread (new Server ()).start ();
        new Thread (new App ()).start ();
    }
}
