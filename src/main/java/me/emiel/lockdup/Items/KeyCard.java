package me.emiel.lockdup.Items;

import me.emiel.lockdup.LockdUp;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class KeyCard {
    private int level;
    private static final String pcbName = "keycard-level";

    public static ItemStack getKeyCard(int level){
        ItemStack is = new ItemStack(Material.TRIPWIRE_HOOK);
        is.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&fKeycard &6&l" + level));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        NamespacedKey key = new NamespacedKey(LockdUp.getInstance(), getPcbName());
        PersistentDataContainer pcb = meta.getPersistentDataContainer();
        pcb.set(key, PersistentDataType.INTEGER,level);
        is.setItemMeta(meta);
        return is;
    }

    public static String getPcbName(){
        return pcbName;
    }

    public static boolean canOpenDoor(Integer keyLevel, Integer doorLevel) {
        if(keyLevel >= doorLevel) return true;
        return false;
    }
}
