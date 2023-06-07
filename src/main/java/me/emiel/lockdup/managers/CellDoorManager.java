package me.emiel.lockdup.managers;

import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.model.Size;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CellDoorManager {
    private CellDoorManager(){}
    public static ItemStack getDoor(String cellId){
        ItemStack door = new ItemStack(Material.IRON_DOOR);
        door.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        ItemMeta doorMeta = door.getItemMeta();
        assert doorMeta != null;
        doorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        doorMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"Door for cell &b&l" + CellManager.findCell(cellId).getCellName()));

        PersistentDataContainer pdc = doorMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(LockdUp.getInstance(), "cell-door-id");
        pdc.set(key, PersistentDataType.STRING, cellId);
        door.setItemMeta(doorMeta);
        return door;
    }


    public static int getCellPrice(Size size) {
        LockdUp plugin = LockdUp.getInstance();
        return switch (size) {
            case Small -> plugin.getConfig().getInt("cellprices.small");
            case Medium -> plugin.getConfig().getInt("cellprices.medium");
            case Large -> plugin.getConfig().getInt("cellprices.large");
        };
    }
}
