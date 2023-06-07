package me.emiel.lockdup.Commands.Cells;

import me.emiel.lockdup.CommandManagerLib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.Managers.CellManager;
import me.emiel.lockdup.Model.Cell;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AddTrust implements SubCommand {
    @Override
    public String getName() {
        return "trust";
    }

    @Override
    public String getDescription() {
        return "Command used to trust a person";
    }

    @Override
    public String getSyntax() {
        return "/cells trust <name>";
    }

    @Override
    public String getPermission() {
        return "cells.trust";
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
            MessageSender.sendMessageWithPrefix(p, "• /cells trust <name>");
            return ;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(!target.hasPlayedBefore()){
            MessageSender.sendMessageWithPrefix(p, "This player ("+ target.getName() +") was not found!");
            return ;
        }
        if(target.getUniqueId() == p.getUniqueId()){
            MessageSender.sendMessageWithPrefix(p, "You can't trust yourself!");
            return ;
        }
        Cell cell = CellManager.getCellFromPlayer(p.getUniqueId());
        if(cell == null){
            MessageSender.sendMessageWithPrefix(p, "You don't have a cell!");
            return ;
        }
        if(CellManager.isPlayerTrusted(cell.get_cellid(), target.getUniqueId())){
            MessageSender.sendMessageWithPrefix(p, "This player is already trusted!");
            return ;
        }
        if(CellManager.isFull(cell)){
            MessageSender.sendMessageWithPrefix(p, "You can't add any more players!");
            return ;
        }
        CellManager.addTrusted(cell, target.getUniqueId());
        MessageSender.sendMessageWithPrefix(p, "&fTrusted &b&l"+ target.getName() + "&r&f!");
    }
}
