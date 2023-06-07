package me.emiel.lockdup.Commands.Cells;

import me.emiel.lockdup.CommandManagerLib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.Managers.CellDoorManager;
import me.emiel.lockdup.Managers.CellManager;
import me.emiel.lockdup.Model.Cell;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class GetCellDoor implements SubCommand {
    @Override
    public String getName() {
        return "door";
    }

    @Override
    public String getDescription() {
        return "Command used to get a cell door";
    }

    @Override
    public String getSyntax() {
        return "/cells door <cell>";
    }

    @Override
    public String getPermission() {
        return "cells.door";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return CellManager.getCells().stream()
                .map(Cell::get_cellName)
                .collect(Collectors.toList());
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;
        Player p = (Player) sender;
        if (args.length <= 0) {
            MessageSender.sendErrorWithPrefix(p, "Please use the command like this:");
            MessageSender.sendErrorWithPrefix(p, "&r&l/cells door <cellname>");
            return;
        }
        Cell cell = CellManager.findCellByName(args[0]);
        if (cell == null) {
            MessageSender.sendErrorWithPrefix(p, "This cell does not exist!");
            return;
        }
        ItemStack stack = CellDoorManager.getDoor(cell.get_cellid());
        p.getInventory().addItem(stack);
        MessageSender.sendMessageWithPrefix(p, "Given the door for cell: &l&b" + cell.get_cellName());

    }
}
