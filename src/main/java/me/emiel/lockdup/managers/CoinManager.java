package me.emiel.lockdup.managers;

import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.items.Coin;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class CoinManager {
    private CoinManager(){}
    public static void giveCoin(Player player, int amount) {
        player.getInventory().addItem(Coin.getCoin(amount));
    }

    public static void giveCoinPlus(Player player, int amount) {
        player.getInventory().addItem(Coin.getCoinPlus(amount));
    }
    public static void giveCoin(Player player, int amount, boolean drop) {
        HashMap<Integer, ItemStack> didnotfit = player.getInventory().addItem(Coin.getCoin(amount));
        if(drop){
            for (Map.Entry<Integer, ItemStack> map : didnotfit.entrySet()) {
                player.getWorld().dropItemNaturally(player.getLocation(), map.getValue());
            }
        }
    }
    public static void giveCoinPlus(Player player, int amount, boolean drop) {
        HashMap<Integer, ItemStack> didnotfit = player.getInventory().addItem(Coin.getCoinPlus(amount));
        if(drop){
            for (Map.Entry<Integer, ItemStack> map : didnotfit.entrySet()) {
                player.getWorld().dropItemNaturally(player.getLocation(), map.getValue());
            }
        }
    }
    public static void giveCoins(Player player, int amount, boolean drop) {
        int howMuch64Coins = (int) ( amount / 64f);
        int howMuchCoins = amount - howMuch64Coins * 64;

        giveCoinPlus(player, howMuch64Coins, drop);
        giveCoin(player, howMuchCoins, drop);
    }
    public static void giveCoins(Player player, int amount) {
        giveCoins(player, amount, false);
    }
    public static boolean hasEnoughCoins(Player player, int amount) {
        return getPlayerCoinAmount(player) >= amount;
        //if(player.isOp()) return true;
    }
    public static int getPlayerCoinAmount(Player player) {
        int amount = 0;
        LockdUp plugin = LockdUp.getInstance();
        for (ItemStack is:
             player.getInventory().getContents()) {
            if(is == null) continue;
            NamespacedKey nk = new NamespacedKey(plugin, "cointype");
            ItemMeta im = is.getItemMeta();
            assert im != null;
            PersistentDataContainer pdc = im.getPersistentDataContainer();
            if(!pdc.has(nk, PersistentDataType.STRING))continue;
            String type = pdc.get(nk, PersistentDataType.STRING);
            assert type != null;
            if(type.equalsIgnoreCase("coin")) amount += is.getAmount();
            if(type.equalsIgnoreCase("coinplus")) amount += is.getAmount() * 64;
        }

        return amount;
    }
    public static void removeCoins(Player player, int amount) {
        LockdUp plugin = LockdUp.getInstance();
        int toRemove = amount;
        ItemStack[] content = player.getInventory().getContents();
        int coinPlusamount = 0;
        int coinAmount = 0;

        int coinPlusToRemove = 0;
        int coinToRemove = 0;
        //Get coin amounts
        for (ItemStack item :
                content) {
            if (item == null) continue;
            NamespacedKey nk = new NamespacedKey(plugin, "cointype");
            ItemMeta im = item.getItemMeta();
            PersistentDataContainer pdc = im.getPersistentDataContainer();
            if (!pdc.has(nk, PersistentDataType.STRING)) continue;
            String type = pdc.get(nk, PersistentDataType.STRING);
            if(type.equalsIgnoreCase("coin")) {
                coinAmount += item.getAmount();
            }else if(type.equalsIgnoreCase("coinplus")){
                coinPlusamount += item.getAmount();
            }
        }
        //calc how much to remove
        int howMuch64Coins = (int) ( toRemove / 64f);
        int howMuchCoins = toRemove - howMuch64Coins * 64;

        //remove 64
        while (howMuch64Coins != 0){
            if(coinPlusamount > 0){
                coinPlusamount --;
                coinPlusToRemove ++;
            }else if (coinAmount >= 64){
                coinAmount -= 64;
                coinToRemove += 64;
            }
            howMuch64Coins--;
        }
        while (howMuchCoins != 0){
            coinAmount --;
            coinToRemove ++;
            howMuchCoins--;
        }

        //Remove
        HashMap<Integer, ItemStack> couldNotRemove = player.getInventory().removeItem(Coin.getCoin(coinToRemove));
        player.getInventory().removeItem(Coin.getCoinPlus(coinPlusToRemove));

        int toReturn = 0;
        int cpToRemoveAfter = 0;
        //add coins?
        for (Map.Entry<Integer, ItemStack> set :
                couldNotRemove.entrySet()) {

            if(set.getValue().isSimilar(Coin.getCoin())){
                cpToRemoveAfter++;
                toReturn += 64 - set.getValue().getAmount();
            }
            player.getInventory().removeItem(Coin.getCoinPlus(cpToRemoveAfter));
            player.getInventory().addItem(Coin.getCoin(toReturn));
        }

    }

}
