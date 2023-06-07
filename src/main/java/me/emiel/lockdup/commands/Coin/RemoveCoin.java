package me.emiel.lockdup.commands.Coin;

import me.emiel.lockdup.commandmanagerlib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.Managers.CoinManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class RemoveCoin implements SubCommand {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Command used to remove coin";
    }

    @Override
    public String getSyntax() {
        return "/coin remove <player> <amount>";
    }

    @Override
    public String getPermission() {
        return "coin.get";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return switch(index){
            case 0 -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            case 1 -> List.of("1", "16", "32", "48", "64");
            default -> null;
        };
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return ;
        Player p = (Player) sender;

        if(args.length < 2){
            MessageSender.sendErrorWithPrefix(p, "Please use the command like this: ");
            MessageSender.sendErrorWithPrefix(p, "/coin remove <player> <amount>");
            return ;
        }

        int amount;
        try{
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            MessageSender.sendErrorWithPrefix(p, "Please use the command like this: ");
            MessageSender.sendErrorWithPrefix(p, "/coin remove <player> <amount>");
            return ;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if(target ==null){
            MessageSender.sendErrorWithPrefix(p, "This player was not found!");
            return ;
        }
        if(!CoinManager.HasEnoughCoins(target, amount)){
            MessageSender.sendErrorWithPrefix(p, "This player doesn't have enough coins!");
            return ;
        }
        CoinManager.removeCoins(target, amount);
        MessageSender.sendErrorWithPrefix(p, "Removed " + amount +" coins from player!");
    }
}
