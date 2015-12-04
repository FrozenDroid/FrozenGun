package com.frozendroid.beargun;

import net.md_5.bungee.api.ChatColor;

public class Messenger {

    public static String infoMsg(String msg)
    {
        return ChatColor.DARK_AQUA+""+ChatColor.BOLD+
                "["+ ChatColor.AQUA +"BearGun"+ ChatColor.DARK_AQUA+ChatColor.BOLD+"] " + ChatColor.YELLOW+msg;
    }

}
