package me.emiel.lockdup.listeners.keydoor;

import me.emiel.lockdup.helper.MessageSender;
import me.emiel.lockdup.items.KeyCard;
import me.emiel.lockdup.LockdUp;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class OnDoorClick implements Listener {
    @EventHandler
    public void onIronDoorClick(PlayerInteractEvent e){
        Block clickedBlock = e.getClickedBlock();
        if( clickedBlock == null ) return;
        Player p = e.getPlayer();


        if (clickedBlock.getType().equals(Material.IRON_DOOR) && e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(e.getItem() == null) return;
            if(e.getItem().getType().equals(Material.TRIPWIRE_HOOK)){
                extracted(clickedBlock, p, e);
            }
        } else if (clickedBlock.getType().equals(Material.IRON_DOOR) && e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if(p.getGameMode() == GameMode.CREATIVE){
                ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
                if(meta == null) return;
                PersistentDataContainer pcb = meta.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(LockdUp.getInstance(), KeyCard.getPcbName());
                if(!pcb.has(key, PersistentDataType.INTEGER)) return;
                e.setCancelled(true);
                int level = pcb.get(key, PersistentDataType.INTEGER);

                if(!p.hasPermission("lockdupkey.setkeycarddoor")|| !p.isOp()){
                    MessageSender.sendErrorWithPrefix(p, "Permission denied.");
                    return;
                }

                PersistentDataContainer pcbBlock = LockdUp.getCustomData(clickedBlock);
                pcbBlock.set(key, PersistentDataType.INTEGER, level);
                MessageSender.sendMessageWithPrefix(p, "Set this door to level " + level);
            }else{
                extracted(clickedBlock, p, e);
            }


        }

    }
    private void extracted(Block clickedBlock, Player p, PlayerInteractEvent e) {
        LockdUp plugin = LockdUp.getInstance();
        //get iron door if locked
        NamespacedKey key = new NamespacedKey(plugin, KeyCard.getPcbName());
        PersistentDataContainer pcb = LockdUp.getCustomData(clickedBlock);
        if(!pcb.has(key, PersistentDataType.INTEGER)) return;
        e.setCancelled(true);

        int doorLevel = pcb.get(key, PersistentDataType.INTEGER);
        ItemStack keyInHand = p.getInventory().getItemInMainHand();
        ItemMeta meta = keyInHand.getItemMeta();
        PersistentDataContainer pdbKey = meta.getPersistentDataContainer();

        if(!pdbKey.has(key, PersistentDataType.INTEGER) || !KeyCard.canOpenDoor(pdbKey.get(key, PersistentDataType.INTEGER), doorLevel)){
            MessageSender.sendMessageWithPrefix(p, "&r&c Permission denied. You need a &llevel &6&l" + doorLevel + " &r&ckey.");
            return;
        }

        Openable door = (Openable) clickedBlock.getBlockData() ;
        door.setOpen(true);
        clickedBlock.setBlockData(door);
        p.playSound(p.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 10, 29);
        //close the door after x amount of ticks
        new BukkitRunnable() {
            @Override
            public void run() {
                door.setOpen(false);
                p.playSound(p.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 10, 29);
                clickedBlock.setBlockData(door);
            }
        }.runTaskLater(plugin, plugin.getConfig().getInt("dooropentime"));   // Your plugin instance, the time to be delayed.
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        LockdUp plugin = LockdUp.getInstance();
        NamespacedKey key = new NamespacedKey(plugin, KeyCard.getPcbName());
        PersistentDataContainer pcb = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
        if(!pcb.has(key, PersistentDataType.INTEGER)) return;

        e.setCancelled(true);
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        LockdUp plugin = LockdUp.getInstance();
        NamespacedKey key = new NamespacedKey(plugin, KeyCard.getPcbName());
        PersistentDataContainer pcb = LockdUp.getCustomData(e.getBlock());
        if(!pcb.has(key, PersistentDataType.INTEGER)) return;
        pcb.remove(key);
    }
}
