package me.emiel.lockdup.Commands.KeyDoor;

import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.Items.KeyCard;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GetKey implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        if(args.length < 1){
            MessageSender.sendErrorWithPrefix(sender, "&r&c&l Please use the command like this:");
            MessageSender.sendErrorWithPrefix(sender, "&r&c&l /keycard <1-5>");
            return true;
        }
        int level = 0;
        if (isNumeric(args[0])){
            int i = Integer.parseInt(args[0]);
            if (i >= 1 && i <= 5) {
                level = i;
            }else{
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lDoor &f&l|&r&c&l Please use the command like this:"));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lDoor &f&l|&r&c&l /keycard <1-5>"));
                return true;
            }

        }
        p.getInventory().addItem(KeyCard.getKeyCard(level));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lDoor &f&l|&r&f Given you a level &2&l" + level + "&r&f keycard!"));
        return true;
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Arrays.asList("1", "2", "3", "4", "5");
    }
}
