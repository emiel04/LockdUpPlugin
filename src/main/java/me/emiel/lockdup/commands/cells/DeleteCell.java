package me.emiel.lockdup.commands.cells;

import me.emiel.lockdup.commandmanagerlib.SubCommand;
import me.emiel.lockdup.helper.MessageSender;
import me.emiel.lockdup.managers.CellManager;
import me.emiel.lockdup.model.Cell;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteCell implements SubCommand {
    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "Command used to delete a cell";
    }

    @Override
    public String getSyntax() {
        return "/cells delete <cell>";
    }

    @Override
    public String getPermission() {
        return "cells.delete";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return CellManager.getCells().stream()
                .map(Cell::getCellName)
                .collect(Collectors.toList());
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can execute this command!");
            return;
        }
        Player player = (Player) sender;

        if(args.length == 0) {
            MessageSender.sendErrorWithPrefix(player, "Please provide a cell name!");
            return;
        }
        if(CellManager.findCellByName(args[0]) == null){
            MessageSender.sendErrorWithPrefix(player, "This cell does not exist!");
            return;
        }
        CellManager.deleteCellByName(args[0]);
        MessageSender.sendMessageWithPrefix(player,"Cell successfully deleted!");
    }
}
