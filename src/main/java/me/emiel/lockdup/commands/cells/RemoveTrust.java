package me.emiel.lockdup.commands.cells;

import me.emiel.lockdup.commandmanagerlib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.Managers.CellManager;
import me.emiel.lockdup.Model.Cell;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RemoveTrust implements SubCommand {
    @Override
    public String getName() {
        return "untrust";
    }

    @Override
    public String getDescription() {
        return "Command used to untrust a person";
    }

    @Override
    public String getSyntax() {
        return "/cells untrust <name>";
    }

    @Override
    public String getPermission() {
        return "cells.untrust";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return ;
        Player p = (Player) sender;
        if(args.length < 1){
            MessageSender.sendMessageWithPrefix(p, "Please use the command like this: ");
            MessageSender.sendMessageWithPrefix(p, "• /cells untrust <name>");
            return ;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(!target.hasPlayedBefore()){
            MessageSender.sendMessageWithPrefix(p, "This player was not found!");
            return ;
        }

        Cell cell = CellManager.getCellFromPlayer(p.getUniqueId());
        if(cell == null){
            MessageSender.sendMessageWithPrefix(p, "You don't have a cell!");
            return ;
        }
        if(!CellManager.removeTrusted(cell, target.getUniqueId())){
            MessageSender.sendMessageWithPrefix(p, "This player isn't trusted!");
            return ;
        }
        MessageSender.sendMessageWithPrefix(p, "&fUntrusted &b&l"+ target.getName() + "&r&f!");
    }
}
