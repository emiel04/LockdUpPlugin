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
import me.emiel.lockdup.Managers.CellDoorManager;
import me.emiel.lockdup.Managers.CellManager;
import me.emiel.lockdup.Model.Cell;
import me.emiel.lockdup.Model.Size;
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
