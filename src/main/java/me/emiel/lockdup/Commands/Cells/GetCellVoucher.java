package me.emiel.lockdup.Commands.Cells;

import me.emiel.lockdup.CommandManagerLib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.Items.CellVoucher;
import me.emiel.lockdup.Managers.CellDoorManager;
import me.emiel.lockdup.Managers.CellManager;
import me.emiel.lockdup.Model.Cell;
import me.emiel.lockdup.Model.Size;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class GetCellVoucher implements SubCommand {
    @Override
    public String getName() {
        return "voucher";
    }

    @Override
    public String getDescription() {
        return "Command used to get a cell voucher";
    }

    @Override
    public String getSyntax() {
        return "/cells voucher <cell>";
    }

    @Override
    public String getPermission() {
        return "cells.voucher";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        if (index == 0){
            return List.copyOf(Size.names());
        }
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;
        if(args.length > 0){
            Size size = CellManager.stringToSize(args[0]);
            if (size != null){
                player.getInventory().addItem(CellVoucher.getVoucher(size));
                MessageSender.sendMessageWithPrefix(player, "Successfully given a voucher for a &b&l" + size.name() +"&r&f cell!");
                return;
            }
        }
        MessageSender.sendErrorWithPrefix(player, "Please use the command like this: /cells voucher <small,medium,large>");

    }
}
