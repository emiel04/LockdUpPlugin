package me.emiel.lockdup.Helper;
import me.emiel.lockdup.LockdUp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageSender {

    public static String mainColor = LockdUp.getInstance().getConfig().getString("main_color");
    public static String prefix = mainColor + LockdUp.getInstance().getConfig().getString("prefix");
    public static String between = LockdUp.getInstance().getConfig().getString("between");
    public static String errorColor = LockdUp.getInstance().getConfig().getString("error_color");

    public static void sendMessage(CommandSender sender, String message){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendMessageWithPrefix(CommandSender sender, String message){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + between + message));
    }
    public static void sendErrorWithPrefix(CommandSender sender, String message){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + between + errorColor + message));
    }

    public static void sendWrongCommandUsage(CommandSender sender){
        sendErrorWithPrefix(sender, "Please use the command like this: ");
    }


    public static void broadcastMessageWithPrefix(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + between + message));
    }

    public static void sendLine(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', mainColor + "&m&l                                  "));
    }
}
