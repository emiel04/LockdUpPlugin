package me.emiel.lockdup.Commands.Coin;

import me.emiel.lockdup.CommandManagerLib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.Managers.CellManager;
import me.emiel.lockdup.Managers.CoinManager;
import me.emiel.lockdup.Model.Cell;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Balance implements SubCommand {
    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Command used to view your balance";
    }

    @Override
    public String getSyntax() {
        return "/coin balance";
    }

    @Override
    public String getPermission() {
        return "coin.balance";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;
        Player p = (Player) sender;
        MessageSender.sendMessageWithPrefix(p, "Your balance is: " + CoinManager.getPlayerCoinAmount(p));
    }
}
