package me.emiel.lockdup.commands.cells;

import me.emiel.lockdup.commandmanagerlib.SubCommand;
import me.emiel.lockdup.helper.MessageSender;
import me.emiel.lockdup.items.CellVoucher;
import me.emiel.lockdup.managers.CellManager;
import me.emiel.lockdup.model.Size;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

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
