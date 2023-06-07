package me.emiel.lockdup.GUI.Cells;

import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.Managers.CellManager;
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
import java.util.UUID;

public class RemoveAllCellsGUI {
    private Inventory inv;


    // You can call this whenever you want to put the items in
    public void initializeItems(String id) {
        inv.addItem(createGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ",true,id,""));
        inv.addItem(createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " "));
        inv.addItem(createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " "));
        inv.addItem(createGuiItem(Material.GREEN_STAINED_GLASS_PANE, "&4&bYes",false,id,""));
        inv.addItem(createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " "));
        inv.addItem(createGuiItem(Material.RED_STAINED_GLASS_PANE, "&4&bNo",false,id,""));
        inv.addItem(createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " "));
        inv.addItem(createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " "));
        inv.addItem(createRandomGuiItem(Material.WHITE_STAINED_GLASS_PANE, " "));
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name,final boolean addId,final String id,final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        if(addId){
            NamespacedKey key = new NamespacedKey(LockdUp.getInstance(), "gui-id");
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(key, PersistentDataType.STRING, id);
        }

        // Set the name of the item
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        for (int i = 0; i < lore.length; i++) {
            String str = lore[i];
            str = ChatColor.translateAlternateColorCodes('&', str);
            lore[i] = str;
        }

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, 9, "Are you sure you want to remove all cells?");
        // Put the items into the inventory
        initializeItems("deleteallcells");

        ent.openInventory(inv);
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
    public static boolean clicked(Player p, int slotIndex){
        //buy cell
        if(slotIndex == 3){
            CellManager.deleteAllCells();
            MessageSender.sendErrorWithPrefix(p, "Successfully deleted all cells!");

            p.closeInventory();
            return true;
        }else if (slotIndex == 5){
            p.closeInventory();
        }
        return false;
    }


}