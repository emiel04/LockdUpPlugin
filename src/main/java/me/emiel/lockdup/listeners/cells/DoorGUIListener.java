package me.emiel.lockdup.listeners.cells;


import me.emiel.lockdup.gui.cells.DoorOwnedGUI;
import me.emiel.lockdup.gui.cells.DoorTrustedGUI;
import me.emiel.lockdup.gui.cells.DoorUnownedGUI;
import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.managers.CellManager;
import me.emiel.lockdup.model.Cell;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
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
        final ItemStack firstItem = e.getInventory().getItem(0);

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        ItemMeta im = firstItem.getItemMeta();
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(LockdUp.getInstance(), "cell-door-id");

        if(pdc.has(key, PersistentDataType.STRING)){
            Cell cell = CellManager.findCell(pdc.get(key, PersistentDataType.STRING));
            key = new NamespacedKey(LockdUp.getInstance(), "cell-door-type");

            if(pdc.has(key, PersistentDataType.STRING)){
                switch (pdc.get(key, PersistentDataType.STRING)) {
                    case "owned" -> DoorOwnedGUI.clicked(cell, p, e.getRawSlot());
                    case "trusted" -> DoorTrustedGUI.clicked(cell, p, e.getRawSlot());
                    case "unowned" -> DoorUnownedGUI.clicked(cell, p, e.getRawSlot());
                    default -> {
                    }
                }
        }
        }

    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (checkIfDoorGUI(e.getInventory())) {
            e.setCancelled(true);
        }
    }

    private boolean checkIfDoorGUI(Inventory e) {
        ItemStack item = e.getItem(0);
        if(item == null) return false;
        ItemMeta im = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(LockdUp.getInstance(), "cell-door-id");
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        return pdc.has(key, PersistentDataType.STRING);
    }

}
