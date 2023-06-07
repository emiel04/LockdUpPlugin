package me.emiel.lockdup.Listeners.Cells;

import com.jeff_media.customblockdata.CustomBlockData;
import me.emiel.lockdup.LockdUp;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DoorPlaceListener implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        if(event.getBlock().getType() != Material.IRON_DOOR) return;
        ItemStack itemInHand = event.getItemInHand();
        ItemMeta itemInHandMeta = itemInHand.getItemMeta();
        PersistentDataContainer pcbInHand = itemInHandMeta.getPersistentDataContainer();
        LockdUp plugin = LockdUp.getInstance();
        NamespacedKey nk = new NamespacedKey(plugin, "cell-door-id");
        if(!pcbInHand.has(nk, PersistentDataType.STRING)) return;
        Block block = event.getBlockPlaced();
        PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);
        customBlockData.set(nk, PersistentDataType.STRING, pcbInHand.get(nk, PersistentDataType.STRING));
    }
}
