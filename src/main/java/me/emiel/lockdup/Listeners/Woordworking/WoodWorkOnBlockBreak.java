package me.emiel.lockdup.Listeners.Woordworking;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.Model.AxeTier;
import me.emiel.lockdup.Storage.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class WoodWorkOnBlockBreak implements Listener {
    private final StateFlag flag;
    private final Map<Material, AxeTier> tierMapping;
    private final Map<Material, Integer> tierExp;

    public WoodWorkOnBlockBreak(StateFlag flag, Map<Material, AxeTier> tierMapping, Map<Material, Integer> tierExp) {
        this.flag = flag;
        this.tierMapping = tierMapping;
        this.tierExp = tierExp;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (flag == null) {
            return;
        }
        Block block = event.getBlock();
        if (!isValidLog(block) && !block.getType().equals(Material.BEDROCK)) {
            return;
        }
        if (!blockHasFlag(flag, block.getLocation())) {
            return;
        }
        handleBlockBreak(event, block);
    }

    private void handleBlockBreak(BlockBreakEvent event, Block block) {
        event.setCancelled(true);
        if (block.getType().equals(Material.BEDROCK)) {
            return;
        }
        Player player = event.getPlayer();
        AxeTier requiredAxeTier = tierMapping.get(block.getType());
        if (requiredAxeTier == null) {
            String message = "You need an axe to do this.";
            MessageSender.sendErrorWithPrefix(player, message);
            return;
        }
        ItemStack handItem = player.getInventory().getItemInMainHand();
        AxeTier playerTier = getAxeTier(handItem);

        if (playerTier == null || !canBreakTier(playerTier, requiredAxeTier)) {
            String requiredTierName = requiredAxeTier.getDisplayName();
            String logType = getFormattedDisplayName(block.getType());
            String message = "You need " + ChatColor.GOLD + "&l" + requiredTierName + MessageSender.errorColor
                    + " axe to break " + ChatColor.GOLD + "&l" + logType + "&r" + MessageSender.errorColor + ".";
            MessageSender.sendErrorWithPrefix(player, message);
            return;
        }

        ItemStack droppedItem = new ItemStack(block.getType());
        player.getInventory().addItem(droppedItem);

        giveXP(player, getXP(block.getType()));
        convertBlockToBedrock(block);
    }

    private int getXP(Material material) {
        return tierExp.getOrDefault(material, 0);
    }

    private void giveXP(Player player, int xp) {
        DataManager dm = LockdUp.getDataManager();
        dm.getPlayerData(player.getUniqueId()).addExp("Woodworking", xp, player);
    }

    public String getFormattedDisplayName(Material material) {
        String[] words = material.name().toLowerCase().split("_");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            formattedName.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
        }

        return formattedName.toString().trim();
    }

    private AxeTier getAxeTier(ItemStack item) {
        if (item != null && item.getType().name().contains("_AXE")) {
            Material material = item.getType();
            boolean enchanted = item.getEnchantments().size() > 0;
            for (AxeTier tier : AxeTier.values()) {
                if (tier.getMaterial() == material && tier.isEnchanted() == enchanted) {
                    return tier;
                }
            }
        }
        return null;
    }

    private boolean canBreakTier(AxeTier playerTier, AxeTier requiredTier) {
        return playerTier.ordinal() >= requiredTier.ordinal();
    }

    private boolean isValidLog(Block block) {
        return Tag.LOGS.isTagged(block.getType());
    }

    private void convertBlockToBedrock(Block block) {
        LockdUp plugin = LockdUp.getInstance();
        BlockData originalState = block.getBlockData();
        block.setType(Material.BEDROCK);

        new BukkitRunnable() {
            @Override
            public void run() {
                block.setBlockData(originalState);
            }
        }.runTaskLater(plugin, 20 * 30);
    }

    private boolean blockHasFlag(StateFlag flag, Location location) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(location);
        ApplicableRegionSet set = query.getApplicableRegions(loc);
        return set.testState(null, flag);
    }


}
