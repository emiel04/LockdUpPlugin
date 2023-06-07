package me.emiel.lockdup.listeners.keydoor;


import me.emiel.lockdup.gui.keydoor.LevelSelectGUI;
import me.emiel.lockdup.LockdUp;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DoorGUIListener implements Listener {
    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!checkIfDoorGUI(e.getInventory())) return;
        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        LevelSelectGUI.clicked(p, e.getRawSlot());


    }
    @EventHandler
    public void onGUIClose(InventoryCloseEvent e){
        if(checkIfDoorGUI(e.getInventory())){
            LevelSelectGUI.removeFromOpen(e.getPlayer());
        }
    }
    private boolean checkIfDoorGUI(Inventory e) {
        ItemStack item = e.getItem(0);
        if(item == null) return false;
        ItemMeta im = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(LockdUp.getInstance(), "key-door-id");
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        return pdc.has(key, PersistentDataType.STRING);
    }
}
