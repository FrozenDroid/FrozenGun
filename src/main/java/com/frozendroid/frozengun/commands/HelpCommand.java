package com.frozendroid.frozengun.commands;

import com.frozendroid.frozengun.FrozenGun;
import com.frozendroid.frozengun.utils.DefaultFontInfo;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;

public class HelpCommand {

    public static void run(CommandSender sender, int page)
    {
        ArrayList<String> helpMessage = new ArrayList<>();

        ArrayList<String> commands = new ArrayList<>();
        ArrayList<String> finalCommands = new ArrayList<>();
        HashMap<String, Integer> parsedCommands = new HashMap<>();

        commands.add("/frozengun help");
        commands.add("/frozengun help <page>");
        commands.add("/frozengun asdsaasd <page>");
        commands.add("/frozengun tqqw <page>");
        commands.add("/frozengun tqqw");

        int maxLength = 0;
        for (String command : commands) {
            String cmd = ChatColor.YELLOW + "" + command + ChatColor.RESET + "  ";
            cmd = ChatColor.translateAlternateColorCodes('&', cmd);
            int cmdLength = 0;

            boolean previousCode = false;
            boolean isBold = false;

            for (char c : cmd.toCharArray()) {
                if (c == '§') {
                    previousCode = true;
                    continue;
                } else if (previousCode) {
                    previousCode = false;
                    if(c == 'l' || c == 'L'){
                        isBold = true;
                        continue;
                    } else isBold = false;
                } else {
                    DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                    cmdLength += isBold ? dFI.getBoldLength() : dFI.getLength();
                    cmdLength++;
                }
            }

            if (cmdLength > maxLength)
                maxLength = cmdLength;

            FrozenGun.info("putting " + cmd);
            parsedCommands.put(cmd, cmdLength);
        }


        final int _maxLength = maxLength;
        FrozenGun.info("maxlength " + _maxLength);
        FrozenGun.info("items in parsecommands " + parsedCommands.size());

        parsedCommands.forEach((cmd, length) -> {
            int compensated = 0;
            StringBuilder cmdBuilder = new StringBuilder(cmd);
            int toCompensate = _maxLength - length;
            int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
            while (compensated < toCompensate) {
                FrozenGun.info("compensating " + cmd);
                cmdBuilder.append(" ");
                compensated += spaceLength;
            }
            cmd = cmdBuilder.toString();
            finalCommands.add(cmd + "test");
        });



//        helpMessage.add(ChatColor.DARK_AQUA + "         -====-" + ChatColor.AQUA + " FrozenGun " + ChatColor.DARK_AQUA + "-====-");
//        if (page == 1) {
//            helpMessage.add(
//                    ChatColor.YELLOW + "" + ChatColor.ITALIC + "/frozengun help               " +
//                    ChatColor.RESET + ChatColor.AQUA + "Brings up this help page."
//            );
//            helpMessage.add(
//                    ChatColor.YELLOW + "" + ChatColor.ITALIC + "/frozengun help <page>      " +
//                    ChatColor.RESET + ChatColor.AQUA + "Brings up the specified help page."
//            );
//        }

        sender.sendMessage(finalCommands.toArray(new String[]{}));
    }

    public static void run(CommandSender sender)
    {
        run(sender, 1);
    }

}
