package me.emiel.lockdup.commands.cells;

import me.emiel.lockdup.commandmanagerlib.SubCommand;
import me.emiel.lockdup.gui.Cells.RemoveAllCellsGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RemoveAllCells implements SubCommand {
    @Override
    public String getName() {
        return "removeall";
    }

    @Override
    public String getDescription() {
        return "Command used to remove all cells";
    }

    @Override
    public String getSyntax() {
        return "/cells removeall";
    }

    @Override
    public String getPermission() {
        return "cells.removeall";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return ;
        Player p = (Player) sender;
        RemoveAllCellsGUI gui = new RemoveAllCellsGUI();
        gui.openInventory(p);
    }
}
