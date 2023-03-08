package me.emiel.lockdup;

import me.emiel.lockdup.Storage.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class LockdUp extends JavaPlugin {
    public static DataManager getDataManager() {
        return dataManager;
    }
    private static DataManager dataManager;
    private PlayerPersistenceHandler playerPersistenceHandler;
    private SkillDataJsonPersistenceHandler skillDataJsonPersistenceHandler;
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.playerPersistenceHandler = new PlayerJsonPersistenceHandler(this);
        this.skillDataJsonPersistenceHandler = new SkillDataJsonPersistenceHandler(this);
        dataManager = new DataManager(playerPersistenceHandler, skillDataJsonPersistenceHandler);
        PlayerDataListener playerDataListener = new PlayerDataListener(dataManager);
        Bukkit.getPluginManager().registerEvents(playerDataListener, this);

        //load data for every player online
        for (Player player : Bukkit.getOnlinePlayers()) {
            dataManager.loadPlayerData(player.getUniqueId());
        }

        startScheduler();
    }

    @Override
    public void onDisable() {
        getLogger().info(this.getName() + "" + " Disabling LockdUp...");
        dataManager.saveAllData();
    }

    public void startScheduler(){
        new BukkitRunnable() {
            public void run() {
                dataManager.saveAllData();
            }
        }.runTaskTimerAsynchronously(this, 20L * 60 * 5, 20L * 60 * 5); //5min
    }
}
