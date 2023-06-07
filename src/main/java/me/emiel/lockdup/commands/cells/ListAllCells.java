package me.emiel.lockdup.commands.cells;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.util.WorldEditRegionConverter;
import me.emiel.lockdup.commandmanagerlib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.Managers.CellManager;
import me.emiel.lockdup.Model.Cell;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListAllCells implements SubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Command used to list all cells";
    }

    @Override
    public String getSyntax() {
        return "/cells list";
    }

    @Override
    public String getPermission() {
        return "cells.list";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can execute this command!");
            return;
        }
        Player p = (Player) sender;
        MessageSender.sendMessage(p, "-------------- All current cells --------------");
        ArrayList<Cell> cells = CellManager.getCells();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        for (Cell c :
                cells) {

            World world = Bukkit.getServer().getWorld(c.get_world());


            RegionManager rm = container.get( BukkitAdapter.adapt(world));
            Region region = WorldEditRegionConverter.convertToRegion(rm.getRegion(c.get_cellRegionID()));

            MessageSender.sendMessage(p, "Name: &l&9" + c.get_cellName());
            MessageSender.sendMessage(p, "Size: &l&9" + c.get_size().name());
            MessageSender.sendMessage(p, "Corner 1: &l&9" + region.getMinimumPoint());
            MessageSender.sendMessage(p, "Corner 2: &l&9" + region.getMaximumPoint());
            if(c.get_owner() == null){
                MessageSender.sendMessage(p, "Owner: &l&9" + "none");
            }else{
                MessageSender.sendMessage(p, "Owner: &l&9" + Bukkit.getServer().getOfflinePlayer(c.get_owner()).getName());
            }
            MessageSender.sendMessage(p, "Trusted:");

            for (UUID uuid : c.get_trusted()) {
                MessageSender.sendMessage(p, "   - &l&9" + Bukkit.getServer().getOfflinePlayer(uuid));
            }
        }
    }
}
