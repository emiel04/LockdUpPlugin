package me.emiel.lockdup.commands.coin;

import me.emiel.lockdup.commandmanagerlib.SubCommand;
import me.emiel.lockdup.helper.MessageSender;
import me.emiel.lockdup.managers.CoinManager;
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
