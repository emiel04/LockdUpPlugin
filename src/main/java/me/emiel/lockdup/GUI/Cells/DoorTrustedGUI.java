package me.emiel.lockdup.GUI.Cells;


import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.Helper.RentCalculator;
import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.Managers.CellManager;
import me.emiel.lockdup.Managers.CoinManager;
import me.emiel.lockdup.Model.Cell;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class DoorTrustedGUI {
    private Inventory inv;

    public static void clicked(Cell cell, Player p, int rawSlot) {
        switch(rawSlot){
            case 3:
                if(!CellManager.isPlayerTrusted(cell.get_cellid(),p.getUniqueId())){
                    MessageSender.sendErrorWithPrefix(p, "You are not part of this cell!");
                    break;
                }
                int cost = RentCalculator.GetRent(cell);
                if(CoinManager.HasEnoughCoins(p, cost)){
                    cell.set_timeLeft(cell.get_timeLeft() + 86400);
                    CellManager.updateCell(cell.get_cellid(), cell);
                    CoinManager.removeCoins(p,  cost);
                    new DoorTrustedGUI().openInventory(p, cell);
                }else{
                    MessageSender.sendErrorWithPrefix(p, "You do not have enough coins!");
                }
                break;
            case 5:
                int rent = LockdUp.getInstance().getConfig().getInt("rent." + cell.get_size().name().toLowerCase());
                //seconds ÷ 86,400 = days
                int returnAmount = (int) ((cell.get_timeLeft() / 86400f) * rent) - rent;
                if(returnAmount < 0) returnAmount = 0;
                if(cell.get_owner().toString().equalsIgnoreCase(p.getUniqueId().toString())){
                    CoinManager.giveCoins(p, returnAmount);
                    CellManager.unOwn(cell);
                    CellManager.updateCell(cell.get_cellid(), cell);
                    new DoorUnownedGUI().openInventory(p, cell);
                }else {
                    MessageSender.sendMessageWithPrefix(p, "You are not the owner of this cell!");
                }


        }



    }

    private static ItemStack getWalletItemStack(Cell cell){
        Date tLeft = new Date((long)(cell.get_timeLeft()* 1000L));
        String formattedDate = new SimpleDateFormat("HH:mm:ss").format(tLeft);
        Duration duration = Duration.ofSeconds(cell.get_timeLeft());
        formattedDate = formatDuration(duration);
        return createGuiItem(Material.SUNFLOWER, "&6&lWallet!", cell,false, "&fTime left: &6" + formattedDate);
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    // You can call this whenever you want to put the items in
    public void initializeItems(Cell cell) {

        inv.addItem(createGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ", cell,true,""));
        OfflinePlayer owner = Bukkit.getOfflinePlayer(cell.get_owner());
        inv.addItem(createGuiSkull(owner, "&b&lOwner", cell,"§a" + owner.getName()));
        inv.addItem(createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ",""));
        inv.addItem(createGuiItem(Material.GREEN_STAINED_GLASS_PANE, "&2&lAdd rent!", cell,false,"&fThis costs: &6&l+" + RentCalculator.GetRent(cell)));

        int timeLeft = cell.get_timeLeft();

        inv.addItem(getWalletItemStack(cell));
        int rent = LockdUp.getInstance().getConfig().getInt("rent." + cell.get_size().name().toLowerCase());
        //seconds ÷ 86,400 = days
        int returnAmount = (int) ((timeLeft / 86400f) * rent) - rent;
        if(returnAmount < 0) returnAmount = 0;

//        Bukkit.getServer().broadcastMessage(returnAmount + "");
//        Bukkit.getServer().broadcastMessage(("rent: " + rent) + "");
//        Bukkit.getServer().broadcastMessage(("timeleft: " + timeLeft) + "");
        inv.addItem(createGuiItem(Material.RED_STAINED_GLASS_PANE, "&4&lUnclaim Cell", cell,false,"&fThis refunds: &6&l" + returnAmount + "&r&f coins"));
        inv.addItem(createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ",""));

        if(cell.get_trusted() != null && !cell.get_trusted().isEmpty()){
            ArrayList<String> trustedPlayers = new ArrayList<>();
            for (UUID displayName:
                    cell.get_trusted()) {
                trustedPlayers.add("&f- &b" + Bukkit.getOfflinePlayer(displayName).getName());
            }
            inv.addItem(createGuiItem(Material.FROGSPAWN, "&3&lCell mates: ", cell,false,trustedPlayers));
        }else{
            inv.addItem(createGuiItem(Material.FROGSPAWN, "&3&lCell mates: ", cell,false,"&fNo cellmates!"));
        }

        inv.addItem(createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ",""));
    }
    public static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%dh:%02dm:%02ds",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }
    private ItemStack createGuiSkull(OfflinePlayer owner, String name, Cell cell, final String... lore) {
        final ItemStack item = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());


        for (int i = 0; i < lore.length; i++) {
            String str = lore[i];
            str = ChatColor.translateAlternateColorCodes('&', str);
            lore[i] = str;
        }
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(owner.getName());
        // Set the name of the item
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));


        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    protected static ItemStack createGuiItem(final Material material, final String name, Cell cell,final boolean addCellId,final ArrayList<String> lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        if(addCellId){
            NamespacedKey key = new NamespacedKey(LockdUp.getInstance(), "cell-door-id");
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(key, PersistentDataType.STRING, cell.get_cellid());
            key = new NamespacedKey(LockdUp.getInstance(), "cell-door-type");
            pdc.set(key, PersistentDataType.STRING, "trusted");
        }

        for (int i = 0; i < lore.size(); i++) {
            String str = lore.get(i);
            str = ChatColor.translateAlternateColorCodes('&', str);
            lore.set(i, str);
        }

        // Set the name of the item
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));


        // Set the lore of the item
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }


    // Nice little method to create a gui item with a custom name, and description
    protected static ItemStack createGuiItem(final Material material, final String name, Cell cell,final boolean addCellId,final String... lore) {
        return createGuiItem(material, name, cell, addCellId, new ArrayList<>(Arrays.asList(lore)));
    }
    public static ItemStack createRandomGuiItem(final Material material, final String name,final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();


        NamespacedKey key = new NamespacedKey(LockdUp.getInstance(), UUID.randomUUID().toString());
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(key, PersistentDataType.STRING, UUID.randomUUID().toString());

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }
    private Cell currentCell;
    // You can open the inventory with this
    public void openInventory(final HumanEntity ent, Cell cell) {
        currentCell = cell;
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, 9, "Cell " + ChatColor.BOLD + cell.get_cellName());
        // Put the items into the inventory
        initializeItems(cell);

        ent.openInventory(inv);
    }

    public static void updateInventory(DoorTrustedGUI gui) {
        //set the rent
        ItemStack previousRent = gui.inv.getItem(3);

        gui.inv.setItem(3, getWalletItemStack(gui.getCurrentCell()));
    }


}