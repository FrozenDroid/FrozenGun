package com.frozendroid.frozengun;

import net.md_5.bungee.api.ChatColor;

public class Messenger {

    public static String infoMsg(String msg)
    {
        return ChatColor.DARK_AQUA+""+ChatColor.BOLD+
                "["+ ChatColor.AQUA +"FrozenGun"+ ChatColor.DARK_AQUA+ChatColor.BOLD+"] " + ChatColor.YELLOW+msg;
    }

}
