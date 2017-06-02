package Logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Ромчи on 02.06.2017.
 */
public abstract class Constant {
    public static Socket              connection;
    public static ObjectInputStream   input;
    public static ObjectOutputStream  output;
    public static final int           PORT = 1234;
    public static String              IP;
}
