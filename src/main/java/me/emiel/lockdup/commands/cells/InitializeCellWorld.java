package me.emiel.lockdup.commands.cells;

import me.emiel.lockdup.commandmanagerlib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.Managers.CellManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class InitializeCellWorld implements SubCommand {
    @Override
    public String getName() {
        return "init";
    }

    @Override
    public String getDescription() {
        return "Command used to initialize the cell world";
    }

    @Override
    public String getSyntax() {
        return "/cells init";
    }

    @Override
    public String getPermission() {
        return "cells.init";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            CellManager.InstantiateCellTemplate(p.getWorld());
            MessageSender.sendMessageWithPrefix(p, "Successfully initialized the cells in world: " + p.getWorld().getName());
        }else{
            sender.sendMessage("Be a player!");
        }
    }
}
