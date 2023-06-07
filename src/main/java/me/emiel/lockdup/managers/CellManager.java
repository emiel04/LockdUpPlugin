package me.emiel.lockdup.managers;

import com.google.gson.Gson;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.emiel.lockdup.helper.MessageSender;
import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.model.Cell;
import me.emiel.lockdup.model.Size;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.*;

public class CellManager {
    private CellManager(){
    }
    private static ArrayList<Cell> cells = new ArrayList<>();
    public static List<Cell> getCells(){
        return cells;
    }
    public static Cell createCell(Region cellRegion, String cellName, Size size) {
        LockdUp plugin = LockdUp.getInstance();

        int seconds = plugin.getConfig().getInt("timeleftonbuy");
        ProtectedRegion pr =  convertRegionToProtectedRegion(cellRegion, cellName);

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
    private static ProtectedRegion convertRegionToProtectedRegion(Region cellRegion, String name) {
        return new ProtectedCuboidRegion(name,cellRegion.getMinimumPoint(), cellRegion.getMaximumPoint());
    }

    public static Cell findCell(String id){
        for (Cell cell : cells) {
            if(cell.getCellid().equalsIgnoreCase(id)){
                return cell;
            }
        }
        return null;
    }
    public static void deleteCell(String id){
        Cell cell = findCell(id);
        if(cell == null) return;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg remove -w " + cell.get_world() + " -f " + cell.getCellName());

        cells.remove(cell);

    }
    public static void deleteAllCells(){

        for (Cell cell : cells) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg remove -w " + cell.get_world() + " -f " + cell.getCellName());
        }
        cells.clear();

    }
    public static void deleteCellByName(String name){
        Cell cell = findCellByName(name);
        if(cell == null)return;
        deleteCell(cell.getCellid());
    }


    public static Cell updateCell(String id, Cell newCell){
        for(Cell cell : cells){
            if(cell.getCellid().equalsIgnoreCase(id)){
                cell.setCellName(newCell.getCellName());
                cell.setCellRegionID(newCell.getCellRegionID());
                cell.setOwner(newCell.getOwner());
                cell.setTrusted(newCell.getTrusted());
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
            if(cell.getCellName().equalsIgnoreCase(name)){
                return cell;
            }
        }
        return null;
    }


    public static void instantiateCellTemplate(World world) {
        Plugin plugin = LockdUp.getInstance();
        String templateName = plugin.getConfig().getString("cell-template.name");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg define " + "-w " + world.getName() + " -g " + templateName);

    }

    public static boolean isPlayerTrusted(String id, UUID uuid) {
        Cell cell = findCell(id);
        assert cell != null;
        if(cell.getOwner().toString().equalsIgnoreCase(uuid.toString())) {
            return true;
        }
        if(cell.getTrusted().contains(uuid)) return true;
        OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
        return p.isOp();
    }
    public static Size stringToSize(String string){
        if(string.equalsIgnoreCase(Size.Small.name())) return Size.Small;
        if(string.equalsIgnoreCase(Size.Medium.name())) return Size.Medium;
        if(string.equalsIgnoreCase(Size.Large.name())) return Size.Large;
        return null;

    }

    public static void removeSecond() {
        for (Cell cell : cells) {
            if (cell.getOwner() == null) continue;
            int timeleft = cell.get_timeLeft() - 1;
            if (timeleft > 1) {
                cell.set_timeLeft(timeleft);
                continue;
            }
            Player owner = Bukkit.getPlayer(cell.getOwner());
            //Cell has expired!
            MessageSender.broadcastMessageWithPrefix("Cell &b&l" + cell.getCellName() + "&r has expired! The previous owner was &b&l" + owner.getDisplayName());
            unOwn(cell);
        }
    }
    public static void unOwn(Cell cell) {

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg removemember -w "+ cell.get_world() + " " + cell.getCellName() + " " + cell.getOwner().toString());
        for (UUID trusted: cell.getTrusted()
        ) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg removemember -w "+ cell.get_world() + " " + cell.getCellName() + " " + trusted.toString());
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg flag " + cell.getCellName() + " -w " +  cell.get_world() + " -g nonmembers chest-access allow");


        cell.setOwner(null);
        cell.setTrusted(new ArrayList<UUID>());
        cell.set_timeLeft(LockdUp.getInstance().getConfig().getInt("timeleftonbuy"));

        updateCell(cell.getCellid(), cell);



    }

    public static boolean addTrusted(Cell cell, UUID uuid){
        if(!isFull(cell)){
            ArrayList<UUID> newTrusted = new ArrayList<>(cell.getTrusted());
            newTrusted.add(uuid);
            cell.setTrusted(newTrusted);
            updateCell(cell.getCellid(), cell);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg addmember -w "+ cell.get_world() + " " + cell.getCellName() + " " + uuid);
            return true;
        }else{
            return false;
        }
    }
    public static boolean removeTrusted(Cell cell, UUID uuid){
        List<UUID> newTrusted = cell.getTrusted();
        if(!newTrusted.contains(uuid)) return false;
        newTrusted.remove(uuid);
        cell.setTrusted(newTrusted);
        updateCell(cell.getCellid(), cell);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg removemember -w "+ cell.get_world() + " " + cell.getCellName() + " " + uuid);
        return true;
    }

    public static Cell getCellFromPlayer(UUID uniqueId) {
        for (Cell cell : cells) {
            if(cell.getOwner() == null) continue;
            if(cell.getOwner().toString().equalsIgnoreCase(uniqueId.toString())){
                return cell;
            }
        }
        return null;
    }

    public static boolean isFull(Cell cell) {
        int size = cell.getTrusted().size();
        int maxSize = LockdUp.getInstance().getConfig().getInt("cellsizes." + cell.get_size().name().toLowerCase());
        return size >= maxSize;
    }
}
