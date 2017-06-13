package Client;

/**
 1.
 2.PORT_MESSAGE - Порт для сервера;
 3.PORT_ONLINE - Порт для ConnectionMessage(Проверяет, в сети ли пользователь)
 4.SLEEP_ONLINE - время сна в бесконечном цикле UserOnline отправляющем на сервер список активных пользователей,
 разгружаем сервер от избытка проверочных операций.
 5.MAX_SYMBOL/MIN_SYMBOL - макс и мин значения длины логина в SelectionLogin
 6.WIDTH_GUI/HEIGHT_GUI - Устанавливаем размер всплывающего окна чата
 7.IP, LOGIN задаются при включении программы
 */
public abstract class Constant {
    public static final int  PORT_MESSAGE = 7000;
    public static final int  PORT_ONLINE  = 7001;
    public static final int  SLEEP_ONLINE = 1000;
    public static final int  MAX_SYMBOL   = 15;
    public static final int  MIN_SYMBOL   = 2;
    public static final int  WIDTH_GUI    = 700;
    public static final int  HEIGHT_GUI   = 600;
    protected static String  IP;
    protected static String  LOGIN;

}
