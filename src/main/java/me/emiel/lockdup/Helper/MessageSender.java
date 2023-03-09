package me.emiel.lockdup.Helper;
import me.emiel.lockdup.LockdUp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageSender {

    public static void sendMessage(CommandSender sender, String message){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendMessageWithPrefix(CommandSender sender, String message){

        String prefix = LockdUp.getInstance().getConfig().getString("prefix");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }
    public static void sendErrorWithPrefix(CommandSender sender, String message){
        String prefix = LockdUp.getInstance().getConfig().getString("prefix");
        String code = "&c";
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix  +  code + message));
    }


    public static void broadcastMessageWithPrefix(String message) {
        String prefix = LockdUp.getInstance().getConfig().getString("prefix");
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }
}
