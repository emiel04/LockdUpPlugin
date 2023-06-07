package me.emiel.lockdup.gui.cells;

import me.emiel.lockdup.helper.MessageSender;
import me.emiel.lockdup.items.CellVoucher;
import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.managers.CellManager;
import me.emiel.lockdup.model.Cell;
import me.emiel.lockdup.model.Size;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class DoorUnownedGUI {
    private Inventory inv;

    // You can call this whenever you want to put the items in
    public void initializeItems(Cell cell) {

        inv.addItem(createGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ", cell,true,""));
        inv.addItem(DoorTrustedGUI.createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ",""));
        inv.addItem(DoorTrustedGUI.createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ",""));
        inv.addItem(DoorTrustedGUI.createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ",""));
        inv.addItem(createGuiItem(Material.NAME_TAG, "&a&lBuy!", cell,false,"&fBuy this cell for: 1 &b"
                + CellVoucher.getVoucher(cell.get_size()).getItemMeta().getDisplayName().replace(".", "") + "&r&f voucher"));
        inv.addItem(DoorTrustedGUI.createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ",""));
        inv.addItem(DoorTrustedGUI.createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ",""));
        inv.addItem(DoorTrustedGUI.createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ",""));
        inv.addItem(DoorTrustedGUI.createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ",""));


    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, Cell cell,final boolean addCellId,final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        if(addCellId){
            NamespacedKey key = new NamespacedKey(LockdUp.getInstance(), "cell-door-id");
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(key, PersistentDataType.STRING, cell.getCellid());
            key = new NamespacedKey(LockdUp.getInstance(), "cell-door-type");
            pdc.set(key, PersistentDataType.STRING, "unowned");
        }

        for (int i = 0; i < lore.length; i++) {
            String str = lore[i];
            str = ChatColor.translateAlternateColorCodes('&', str);
            lore[i] = str;
        }

        // Set the name of the item
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent, Cell cell) {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, 9, "Cell " + ChatColor.BOLD + cell.getCellName());
        // Put the items into the inventory
        initializeItems(cell);

        ent.openInventory(inv);
    }

    public static boolean clicked(Cell cell, Player p, int slotIndex){
        //buy cell
        if(slotIndex == 4){
            Cell ownedCell = CellManager.getCellFromPlayer(p.getUniqueId());
            if(ownedCell != null){
                MessageSender.sendErrorWithPrefix(p, "You already own cell: &r&6" + ownedCell.getCellName());
                return false;
            }
            if(!HasCellVoucherAndRemove(p, cell.get_size())){
                MessageSender.sendErrorWithPrefix(p, "You do not have a voucher for a &b&l" + cell.get_size().name() + "&r&6 cell!");
                return false;
            }
            cell.setOwner(p.getUniqueId());
            cell.set_timeLeft(LockdUp.getInstance().getConfig().getInt("timeleftonbuy"));
            CellManager.updateCell(cell.getCellid(), cell);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg addmember -w "+ cell.get_world() + " " + cell.getCellName() + " " + p.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg flag " + cell.getCellName() + " -w " +  cell.get_world() + " -g " + "nonmembers" + " chest-access deny");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg flag " + cell.getCellName() + " -w " +  cell.get_world() + " -g members" + " chest-access allow");
            MessageSender.sendMessageWithPrefix(p, "You successfully bought &b&l" + cell.getCellName());
            p.closeInventory();
            return true;
        }
        return false;
    }

    private static boolean HasCellVoucherAndRemove(Player p, Size size) {
        NamespacedKey key = new NamespacedKey(LockdUp.getInstance(), "vouchertype");

        ItemStack[] contents = p.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if(item == null) continue;
            ItemMeta im = item.getItemMeta();
            PersistentDataContainer pcb = im.getPersistentDataContainer();
            if (pcb.has(key, PersistentDataType.STRING)) {
                Size size_item = CellManager.stringToSize(pcb.get(key, PersistentDataType.STRING));
                if (size_item == null) continue;
                if (size_item == size) {
                    item.setAmount(item.getAmount() - 1);
                    p.getInventory().setItem(i, item);
                    return true;
                } ;
            }
        }
        return false;
    }


}