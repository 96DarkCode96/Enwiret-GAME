package me.darkcode.logger;

import java.text.SimpleDateFormat;

public class Logger {

    private static final SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");

    public static void info(String msg){
        System.out.println("[" + time.format(System.currentTimeMillis()) + "][INFO] " + msg);
    }

    public static void warn(String msg, Throwable...opt){
        System.out.println("[" + time.format(System.currentTimeMillis()) + "][WARN] " + msg);
        for (Throwable throwable : opt) {
            throwable.printStackTrace();
        }
    }

    public static void error(String msg, Throwable...opt){
        System.out.println("[" + time.format(System.currentTimeMillis()) + "][ERROR] " + msg);
        for (Throwable throwable : opt) {
            throwable.printStackTrace();
        }
    }

    public static void debug(String msg, Throwable...opt){
        System.out.println("[" + time.format(System.currentTimeMillis()) + "][DEBUG] " + msg);
        for (Throwable throwable : opt) {
            throwable.printStackTrace();
        }
    }

}