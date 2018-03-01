package com.frozendroid.frozengun;

public class Messenger {

    private static String prefix;

    public static String infoMsg(String msg) {
        return prefix + " " + msg;
    }

    public static void setPrefix(String prefix) {
        Messenger.prefix = prefix;
    }

}
