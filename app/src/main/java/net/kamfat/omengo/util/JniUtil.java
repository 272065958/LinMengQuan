package net.kamfat.omengo.util;

/**
 * Created by cjx on 2017/2/6.
 */
public class JniUtil {
    static {
        System.loadLibrary("JniUtil");
    }

    private static JniUtil instance;

    static {
        instance = new JniUtil();
    }

    public static JniUtil getInstance(){
        return instance;
    }

    JniUtil(){

    }

    public native String test();
}
