package me.emiel.lockdup.listeners.cells;

import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.gui.cells.DoorOwnedGUI;
import me.emiel.lockdup.gui.cells.DoorTrustedGUI;
import me.emiel.lockdup.gui.cells.DoorUnownedGUI;
import me.emiel.lockdup.helper.MessageSender;
import me.emiel.lockdup.managers.CellManager;
import me.emiel.lockdup.model.Cell;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DoorClickListener implements Listener {


    @EventHandler
    public void onDoorClick(PlayerInteractEvent e) {
        if (e.getHand() == null) return;
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (e.getClickedBlock() == null) return;
        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            LockdUp plugin = LockdUp.getInstance();
            PersistentDataContainer customBlockData = LockdUp.getCustomData(e.getClickedBlock());

            NamespacedKey nk = new NamespacedKey(plugin, "cell-door-id");
            if (!customBlockData.has(nk, PersistentDataType.STRING)) return;
            String cellId = customBlockData.get(nk, PersistentDataType.STRING);
            Cell cell = CellManager.findCell(cellId);
            if (cell == null) return;
            if (cell.getOwner() != null) {
                if (!CellManager.isPlayerTrusted(cellId, p.getUniqueId())) {
                    return;
                }
            }

            Block b = e.getClickedBlock();
            BlockData blockData = b.getBlockData();
            Openable door = (Openable) blockData;
            door.setOpen(!door.isOpen());
            b.setBlockData(door);

        } else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getPlayer().isSneaking()) {
                LockdUp plugin = LockdUp.getInstance();
                PersistentDataContainer customBlockData = LockdUp.getCustomData(e.getClickedBlock());
                NamespacedKey nk = new NamespacedKey(plugin, "cell-door-id");
                if (!customBlockData.has(nk, PersistentDataType.STRING)) return;
                String cellId = customBlockData.get(nk, PersistentDataType.STRING);
                Cell cell = CellManager.findCell(cellId);
                if (cell == null) return;

                MessageSender.sendMessageWithPrefix(p, "Broken cell door: &b" + cell.getCellName());
                customBlockData.remove(nk);
                nk = new NamespacedKey(plugin, "cell-door-id");
                customBlockData.remove(nk);

            } else {
                LockdUp plugin = LockdUp.getInstance();
                PersistentDataContainer customBlockData = LockdUp.getCustomData(e.getClickedBlock());
                NamespacedKey nk = new NamespacedKey(plugin, "cell-door-id");
                if (!customBlockData.has(nk, PersistentDataType.STRING)) return;
                String cellId = customBlockData.get(nk, PersistentDataType.STRING);
                Cell cell = CellManager.findCell(cellId);
                if (cell == null) return;
                e.setCancelled(true);
                if (cell.getOwner() == null) {
                    DoorUnownedGUI gui = new DoorUnownedGUI();
                    gui.openInventory(p, cell);
                } else if (CellManager.isPlayerTrusted(cellId, p.getUniqueId())) {
                    DoorTrustedGUI doorGUI = new DoorTrustedGUI();
                    doorGUI.openInventory(p, cell);
                } else {
                    DoorOwnedGUI gui = new DoorOwnedGUI();
                    gui.openInventory(p, cell);
                }


            }


        }
    }
}
