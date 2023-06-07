package me.emiel.lockdup.items;

import me.emiel.lockdup.LockdUp;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Coin {
    private Coin(){}
    public static ItemStack getCoin(){
        return getCoin(1);
    }

    public static ItemStack getCoin(int amount){
        LockdUp plugin = LockdUp.getInstance();

        ItemStack itemStack = new ItemStack(Material.SUNFLOWER, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if(itemMeta != null){
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("coinname")));
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            NamespacedKey nk = new NamespacedKey(plugin, "cointype");
            itemMeta.getPersistentDataContainer().set(nk, PersistentDataType.STRING, "coin");
        }


        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ItemStack getCoinPlus(){
        return getCoin(1);
    }

    public static ItemStack getCoinPlus(int amount){
        LockdUp plugin = LockdUp.getInstance();

        ItemStack itemStack = new ItemStack(Material.SUNFLOWER, amount);
        itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if(itemMeta != null){
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("coinplusname")));
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            NamespacedKey nk = new NamespacedKey(plugin, "cointype");
            itemMeta.getPersistentDataContainer().set(nk, PersistentDataType.STRING, "coinplus");

        }
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
