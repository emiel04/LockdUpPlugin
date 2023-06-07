package me.emiel.lockdup.Commands.Cells;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.emiel.lockdup.CommandManagerLib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.Managers.CellManager;
import me.emiel.lockdup.Managers.CellDoorManager;
import me.emiel.lockdup.Model.Cell;
import me.emiel.lockdup.Model.Size;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateCell implements SubCommand {
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Command used to create a new cell";
    }

    @Override
    public String getSyntax() {
        return "/cells create <name>";
    }

    @Override
    public String getPermission() {
        return "cells.create";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        if (index == 1){
            return List.copyOf(Size.names());
        }
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can execute this command!");
            return ;
        }
        Player player = (Player) sender;

        //check if the arguments are right
        if(!CheckArgs(args)){
            MessageSender.sendErrorWithPrefix(player, "Please use the command like this: /cells create <cell name> <small,medium,large>");
            return;
        }

        //check if template has been initialized in the world where the player is.
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        ProtectedRegion pr = container.get(BukkitAdapter.adapt(player.getWorld())).getRegion(LockdUp.getInstance().getConfig().getString("cell-template.name"));
        if(pr == null){
            MessageSender.sendLine(sender);
            MessageSender.sendErrorWithPrefix(player, "The cells plugin has not been initialized in this world.");
            MessageSender.sendErrorWithPrefix(player, "&fPlease do &l&b/initializecells");
            MessageSender.sendLine(sender);

            return;
        }

        String name = args[0];
        String size = args[1];

        if(CellManager.findCellByName(name) != null){
            MessageSender.sendErrorWithPrefix(player, "This cell already exists!");
            return;
        }

        Actor actor = BukkitAdapter.adapt(player); // Worldedits player class
        SessionManager manager = WorldEdit.getInstance().getSessionManager();
        LocalSession localSession = manager.get(actor);

        //get the region
        Region region;
        try {
            region = localSession.getSelection();
            //check if valid region
            if(region == null){
                MessageSender.sendErrorWithPrefix(player, "Please select a valid region before executing this command!");
                return;
            }
        } catch (IncompleteRegionException e) {
            MessageSender.sendErrorWithPrefix(player, "Please select a valid region before executing this command!");
            return;
        }

        Cell createdCell;

        if(size.equalsIgnoreCase("small")){
            createdCell = CellManager.CreateCell(region, name, Size.Small);

        }else if(size.equalsIgnoreCase("medium")){
            createdCell = CellManager.CreateCell(region, name, Size.Medium);

        }else if(size.equalsIgnoreCase("large")){
            createdCell = CellManager.CreateCell(region, name, Size.Large);
        }
        else{
            MessageSender.sendErrorWithPrefix(player, "Please use the command like this: /cells create <cell name> <small,medium,large>");
            return;
        }



        MessageSender.sendMessageWithPrefix(player, "&aSuccessfully created cell!");
        //give door to player
        player.getInventory().addItem(CellDoorManager.getDoor(createdCell.get_cellid()));
    }
    private boolean CheckArgs(String[] args) {
        if(args.length != 2) return false;
        return args[1].equalsIgnoreCase("small") || args[1].equalsIgnoreCase("medium") || args[1].equalsIgnoreCase("large");
    }
}
