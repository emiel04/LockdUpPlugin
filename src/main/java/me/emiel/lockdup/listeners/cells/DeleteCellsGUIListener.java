package me.emiel.lockdup.listeners.cells;

import me.emiel.lockdup.gui.cells.RemoveAllCellsGUI;
import me.emiel.lockdup.LockdUp;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DeleteCellsGUIListener implements Listener {
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (!isDeleteCellsGUI(e.getInventory())) return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        RemoveAllCellsGUI.clicked(p, e.getRawSlot());
    }


    private boolean isDeleteCellsGUI(Inventory inventory) {
        ItemStack item = inventory.getItem(0);
        if(item == null) return false;
        ItemMeta im = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(LockdUp.getInstance(), "gui-id");
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        if(pdc.has(key, PersistentDataType.STRING)){
            return pdc.get(key, PersistentDataType.STRING).equalsIgnoreCase("deleteallcells");
        }
        return false;
    }
}
