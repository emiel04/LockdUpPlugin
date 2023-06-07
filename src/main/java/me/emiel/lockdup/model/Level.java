package me.emiel.lockdup.model;

import me.emiel.lockdup.helper.MessageSender;
import me.emiel.lockdup.LockdUp;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Level {
    private String name;
    private int level = 1;
    private int currentXp = 0;

    public int getLevel() {
        return level;
    }

    public int getCurrentXp() {
        return currentXp;
    }

    public int calculateTotalExpNeeded(int level) {

        if (LockdUp.isExponentialCalculation()){
            return calculateExponential(level);
        }
        return  calculateLinear(level);
    }

    private int calculateExponential(int level) {
        return (int) (LockdUp.getXP_PER_LEVEL() * Math.pow(LockdUp.getXP_MULTIPLIER_PER_LEVEL(), level - 1));
    }
    private int calculateLinear(int level) {
        int startValue = LockdUp.getXP_PER_LEVEL();
        int increment = LockdUp.getExtraXpPerLevel();
        return startValue + increment * (level - 1);
    }

//    public int calculatePredictedLevel(int xp) {
//        return (int) (Math.log10((double) xp / LockdUp.getXP_PER_LEVEL()) / Math.log10(LockdUp.getXP_MULTIPLIER_PER_LEVEL()) + 1);
//    }

    public void addXp(int xp, String skill, Player player) {
        currentXp += xp;

        int xpToLevelUp = calculateTotalExpNeeded(level + 1);
        boolean leveledUp = false;
        while (currentXp >= xpToLevelUp) {
            leveledUp = true;
            levelUp(player);
            xpToLevelUp = calculateTotalExpNeeded(level + 1);
        }
        if(!leveledUp){
            sendXpActionBar(skill, xp, player);
        }
    }

    private void levelUp(Player player) {
        int oldLevel = level;
        int xpToLevelUp = calculateTotalExpNeeded(level + 1);

        level++;
        currentXp -= xpToLevelUp;

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 20, 1);
        String actionBarMessage = ChatColor.translateAlternateColorCodes('&',
                "Level up! " + ChatColor.GRAY + "| " + ChatColor.WHITE + "Level " + ChatColor.RESET + MessageSender.mainColor + oldLevel +
                        ChatColor.GRAY + " -> " + ChatColor.RESET + "Level " + MessageSender.mainColor + level);

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionBarMessage));

    }

    private void sendXpActionBar(String skill, int xp, Player player) {
        int currentLevel = getLevel();
        int xpToNextLevel = calculateTotalExpNeeded(currentLevel + 1);
        String c = MessageSender.mainColor;
        String actionBarMessage = ChatColor.translateAlternateColorCodes('&',
                "&f" + skill + " &l| &a+&f&l" + xp + " &r&f| " + ChatColor.GRAY + "[" + ChatColor.WHITE +
                        currentXp + ChatColor.GRAY + "/" + ChatColor.WHITE + xpToNextLevel + ChatColor.GRAY + "] " +
                        ChatColor.GRAY + "| Level: " + ChatColor.WHITE + currentLevel);

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionBarMessage));
    }
}