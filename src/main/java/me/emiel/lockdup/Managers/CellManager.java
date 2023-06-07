package me.emiel.lockdup.Managers;

import com.google.gson.Gson;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.Model.Cell;
import me.emiel.lockdup.Model.Size;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.*;

public class CellManager {

    private static ArrayList<Cell> cells = new ArrayList<>();
    public static ArrayList<Cell> getCells(){
        return cells;
    }
    public static Cell CreateCell(Region cellRegion, String cellName, Size size) {
        LockdUp plugin = LockdUp.getInstance();

        int seconds = plugin.getConfig().getInt("timeleftonbuy");
        ProtectedRegion pr =  ConvertRegionToProtectedRegion(cellRegion, cellName);

        //save region
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        //Add to template region
        try {
            pr.setParent(container.get(cellRegion.getWorld()).getRegion(plugin.getConfig().getString("cell-template.name")));
        } catch (ProtectedRegion.CircularInheritanceException e) {
            throw new RuntimeException(e);
        }

        RegionManager regions = container.get(cellRegion.getWorld());
        assert regions != null;
        regions.addRegion(pr);
        Cell newCell = new Cell(pr.getId(),cellRegion.getWorld().getName(),size ,cellName, null,Collections.emptyList(), seconds ,0);


        cells.add(newCell);
        return newCell;
    }

    //only supports cuboid
    private static ProtectedRegion ConvertRegionToProtectedRegion(Region cellRegion, String name) {
        return new ProtectedCuboidRegion(name,cellRegion.getMinimumPoint(), cellRegion.getMaximumPoint());
    }

    public static Cell findCell(String id){
        for (Cell cell : cells) {
            if(cell.get_cellid().equalsIgnoreCase(id)){
                return cell;
            }
        }
        return null;
    }
    public static void deleteCell(String id){
        Cell cell = findCell(id);
        if(cell == null) return;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg remove -w " + cell.get_world() + " -f " + cell.get_cellName());

        cells.remove(cell);

    }
    public static void deleteAllCells(){

        for (Cell cell : cells) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg remove -w " + cell.get_world() + " -f " + cell.get_cellName());
        }
        cells.clear();

    }
    public static void deleteCellByName(String name){
        Cell cell = findCellByName(name);
        if(cell == null)return;
        deleteCell(cell.get_cellid());
    }


    public static Cell updateCell(String id, Cell newCell){
        for(Cell cell : cells){
            if(cell.get_cellid().equalsIgnoreCase(id)){
                cell.set_cellName(newCell.get_cellName());
                cell.set_cellRegionID(newCell.get_cellRegionID());
                cell.set_owner(newCell.get_owner());
                cell.set_trusted(newCell.get_trusted());
                cell.set_timeLeft(newCell.get_timeLeft());
                cell.set_coins(newCell.get_coins());
                cell.set_size(newCell.get_size());

                return cell;
            }
        }
        return null;


    }

    public static void saveCells() throws IOException {
        Gson gson = new Gson();
        File file = new File(LockdUp.getInstance().getDataFolder().getAbsolutePath() + "/cells.json");
        file.getParentFile().mkdir();
        file.createNewFile();
        Writer writer = new FileWriter(file, false);
        gson.toJson(cells, writer);
        writer.flush();
        writer.close();
        LockdUp.getInstance().getLogger().info("Cells have been saved!");
    }
    public static void loadCells() throws IOException{
        Gson gson = new Gson();
        File file = new File(LockdUp.getInstance().getDataFolder().getAbsolutePath() + "/cells.json");

        if(file.exists()){
            Reader reader = new FileReader(file);
            Cell[] c = gson.fromJson(reader, Cell[].class);
            if(c != null){
                cells = new ArrayList<>(Arrays.asList(c));
                LockdUp.getInstance().getLogger().info("Cells loaded!");
            }else{
                LockdUp.getInstance().getLogger().info("No cells found!");
            }
        }
    }
    public static Cell findCellByName(String name){
        for (Cell cell : cells) {
            if(cell.get_cellName().equalsIgnoreCase(name)){
                return cell;
            }
        }
        return null;
    }


    public static void InstantiateCellTemplate(World world) {
        Plugin plugin = LockdUp.getInstance();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        String templateName = plugin.getConfig().getString("cell-template.name");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg define " + "-w " + world.getName() + " -g " + templateName);

    }

    public static boolean isPlayerTrusted(String id, UUID uuid) {
        Cell cell = findCell(id);
        if(cell.get_owner().toString().equalsIgnoreCase(uuid.toString())) {
            return true;
        }
        if(cell.get_trusted().contains(uuid)) return true;
        OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
        if(p.isOp()) return true;
        return false;
    }
    public static Size stringToSize(String string){
        if(string.equalsIgnoreCase(Size.Small.name())) return Size.Small;
        if(string.equalsIgnoreCase(Size.Medium.name())) return Size.Medium;
        if(string.equalsIgnoreCase(Size.Large.name())) return Size.Large;
        return null;

    }

    public static void removeSecond() {
        for (Cell cell : cells) {
            if (cell.get_owner() == null) continue;
            int timeleft = cell.get_timeLeft() - 1;
            if (timeleft > 1) {
                cell.set_timeLeft(timeleft);
                continue;
            }
            Player owner = Bukkit.getPlayer(cell.get_owner());
            //Cell has expired!
            MessageSender.broadcastMessageWithPrefix("Cell &b&l" + cell.get_cellName() + "&r has expired! The previous owner was &b&l" + owner.getDisplayName());
            unOwn(cell);


        }
    }
    public static void unOwn(Cell cell) {

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg removemember -w "+ cell.get_world() + " " + cell.get_cellName() + " " + cell.get_owner().toString());
        for (UUID trusted: cell.get_trusted()
        ) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg removemember -w "+ cell.get_world() + " " + cell.get_cellName() + " " + trusted.toString());
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg flag " + cell.get_cellName() + " -w " +  cell.get_world() + " -g nonmembers chest-access allow");


        cell.set_owner(null);
        cell.set_trusted(new ArrayList<UUID>());
        cell.set_timeLeft(LockdUp.getInstance().getConfig().getInt("timeleftonbuy"));

        updateCell(cell.get_cellid(), cell);



    }

    public static boolean addTrusted(Cell cell, UUID uuid){
        if(!isFull(cell)){
            ArrayList<UUID> newTrusted = new ArrayList<>(cell.get_trusted());
            newTrusted.add(uuid);
            cell.set_trusted(newTrusted);
            updateCell(cell.get_cellid(), cell);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg addmember -w "+ cell.get_world() + " " + cell.get_cellName() + " " + uuid);
            return true;
        }else{
            return false;
        }
    }
    public static boolean removeTrusted(Cell cell, UUID uuid){
        List<UUID> newTrusted = cell.get_trusted();
        if(!newTrusted.contains(uuid)) return false;
        newTrusted.remove(uuid);
        cell.set_trusted(newTrusted);
        updateCell(cell.get_cellid(), cell);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg removemember -w "+ cell.get_world() + " " + cell.get_cellName() + " " + uuid);
        return true;
    }

    public static Cell getCellFromPlayer(UUID uniqueId) {
        for (Cell cell : cells) {
            if(cell.get_owner() == null) continue;
            if(cell.get_owner().toString().equalsIgnoreCase(uniqueId.toString())){
                return cell;
            }
        }
        return null;
    }

    public static boolean isFull(Cell cell) {
        int size = cell.get_trusted().size();
        int maxSize = LockdUp.getInstance().getConfig().getInt("cellsizes." + cell.get_size().name().toLowerCase());
        return size >= maxSize;
    }
}
