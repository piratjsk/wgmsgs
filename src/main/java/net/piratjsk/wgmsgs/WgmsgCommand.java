package net.piratjsk.wgmsgs;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

final class WgmsgCommand implements CommandExecutor {

    private final Wgmsgs plugin;

    WgmsgCommand(Wgmsgs plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length>0 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("wgmsgs.reload")) {
            this.plugin.setMsgs();
            sender.sendMessage(ChatColor.GRAY + "Reloaded!");
            return true;
        }
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "arr..");
        return true;
    }

}
