package Server;

/**
 * Created by Ромчи on 20.06.2017.
 */
public abstract class ConstantServer {
    public static final int PORT_MESSAGE = 7000;
    public static final int PORT_ONLINE  = 7001;
    public static boolean   clearHistory = true; //удалять ли историю после выхода всех пользователей из сети?
    public static int       countMessages  = 1;
}
