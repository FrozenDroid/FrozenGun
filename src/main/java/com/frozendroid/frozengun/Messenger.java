package com.frozendroid.frozengun;

import net.md_5.bungee.api.ChatColor;

public class Messenger {

    private static String prefix;

    public static String infoMsg(String msg) {
        return ChatColor.DARK_AQUA + "" + ChatColor.BOLD +
                "[" + ChatColor.AQUA + prefix + ChatColor.DARK_AQUA + ChatColor.BOLD + "] " + ChatColor.YELLOW + msg;
    }

    public static void setPrefix(String prefix) {
        Messenger.prefix = prefix;
    }

}
