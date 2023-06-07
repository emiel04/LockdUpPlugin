package me.emiel.lockdup.items;

import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.model.Size;
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

public class CellVoucher {
    private CellVoucher(){}
    public static ItemStack getVoucher (Size size){
        LockdUp plugin = LockdUp.getInstance();

        ItemStack itemStack = new ItemStack(Material.PAPER, 1);
        itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&fThis can be used to buy a &b&l" + size.name() + "&r&f cell."));
        if(itemMeta != null){
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&r&6&l" + size.name() + " &r&fcell." ));
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            NamespacedKey nk = new NamespacedKey(plugin, "vouchertype");
            itemMeta.getPersistentDataContainer().set(nk, PersistentDataType.STRING, size.name());

        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
