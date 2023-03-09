package me.emiel.lockdup;

import com.sk89q.worldguard.config.YamlConfigurationManager;
import me.emiel.lockdup.CommandManagerLib.MainCommand;
import me.emiel.lockdup.Commands.BaseCommand;
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
    private static MainCommand mainCommand;
    private static LockdUp instance;
    public static LockdUp getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        this.playerPersistenceHandler = new PlayerJsonPersistenceHandler(this);
        this.skillDataJsonPersistenceHandler = new SkillDataJsonPersistenceHandler(this);
        dataManager = new DataManager(playerPersistenceHandler, skillDataJsonPersistenceHandler);


        //load data for every player online
        for (Player player : Bukkit.getOnlinePlayers()) {
            dataManager.loadPlayerData(player.getUniqueId());
        }

        registerCommandsAndEvents();

        startScheduler();
    }

    private void registerCommandsAndEvents ()
    {
        PlayerDataListener playerDataListener = new PlayerDataListener(dataManager);
        Bukkit.getPluginManager().registerEvents(playerDataListener, this);
        mainCommand = new BaseCommand();
        Bukkit.broadcastMessage("initliazed");
        mainCommand.registerMainCommand(this, "lockdup");
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
    public static MainCommand getMainCommand() {
        return mainCommand;
    }
}
