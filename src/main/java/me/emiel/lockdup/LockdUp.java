package me.emiel.lockdup;

import com.jeff_media.customblockdata.CustomBlockData;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import me.emiel.lockdup.commandmanagerlib.MainCommand;
import me.emiel.lockdup.commands.cells.CellCommand;
import me.emiel.lockdup.commands.coin.CoinCommand;
import me.emiel.lockdup.commands.keydoor.GetKey;
import me.emiel.lockdup.commands.skills.SkillCommand;
import me.emiel.lockdup.jobs.Job;
import me.emiel.lockdup.jobs.Woodworking;
import me.emiel.lockdup.listeners.cells.DeleteCellsGUIListener;
import me.emiel.lockdup.listeners.cells.DoorClickListener;
import me.emiel.lockdup.listeners.cells.DoorGUIListener;
import me.emiel.lockdup.listeners.cells.DoorPlaceListener;
import me.emiel.lockdup.listeners.keydoor.OnDoorClick;
import me.emiel.lockdup.managers.CellManager;
import me.emiel.lockdup.managers.SkillManager;
import me.emiel.lockdup.storage.*;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.List;

public final class LockdUp extends JavaPlugin {
    public static DataManager getDataManager() {
        return dataManager;
    }
    public static SkillManager getSkillManager() {
        return skillManager;
    }
    private static DataManager dataManager;
    private static SkillManager skillManager;
    private PlayerPersistenceHandler playerPersistenceHandler;
    private SkillDataJsonPersistenceHandler skillDataJsonPersistenceHandler;
    private static MainCommand skillsCommand;
    private static LockdUp instance;
    public static LockdUp getInstance() {
        return instance;
    }
    private static int XP_BASE;

    public static int getExtraXpPerLevel() {
        return EXTRA_XP_PER_LEVEL;
    }

    private static int EXTRA_XP_PER_LEVEL;
    private static double XP_MULTIPLIER_PER_LEVEL;
    private static boolean EXPONENTIAL_CALCULATION;
    public static boolean isExponentialCalculation() {
        return EXPONENTIAL_CALCULATION;
    }
    public static StateFlag WOODWORKING_FLAG;

    public static PersistentDataContainer getCustomData(Block block) {
        if(block.getBlockData() instanceof Bisected) {
            Bisected.Half half = ((Bisected)block.getBlockData()).getHalf();
            if(half == Bisected.Half.TOP) {
                return new CustomBlockData(block.getRelative(BlockFace.DOWN,1), getInstance());
            }
        }
        return new CustomBlockData(block,getInstance());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        addFlags();

    }

    private void addFlags() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "my-custom-flag", defaulting to true
            StateFlag flag = new StateFlag("woodworking", false);
            registry.register(flag);
            WOODWORKING_FLAG = flag; // only set our field if there was no error
        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("my-custom-flag");
            if (existing instanceof StateFlag) {
                WOODWORKING_FLAG = (StateFlag) existing;
            } else {
                getLogger().severe("Flag name conflict!");
                this.getServer().getPluginManager().disablePlugin(this);
            }
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        XP_BASE = getConfig().getInt("base-exp");
        EXTRA_XP_PER_LEVEL = getConfig().getInt("extra-xp-per-level");
        XP_MULTIPLIER_PER_LEVEL = getConfig().getDouble("multiplier-per-level");
        EXPONENTIAL_CALCULATION = getConfig().getBoolean("exponential");
        // Plugin startup logic
        this.playerPersistenceHandler = new PlayerJsonPersistenceHandler(this);
        this.skillDataJsonPersistenceHandler = new SkillDataJsonPersistenceHandler(this);
        dataManager = new DataManager(playerPersistenceHandler, skillDataJsonPersistenceHandler);
        skillManager = new SkillManager(dataManager);

        //load data for every player online
        for (Player player : Bukkit.getOnlinePlayers()) {
            dataManager.loadPlayerData(player.getUniqueId());
        }
        try {
            CellManager.loadCells();
        } catch (IOException e) {
            getLogger().severe(e.getMessage());
        }


        registerCommandsAndEvents();
        addJobs();
        startSchedulerLong();
        startSchedulerSecond();
    }

    private void addJobs() {
        List<Job> jobList = List.of(
                new Woodworking("Woodworking", WOODWORKING_FLAG)
        );

        for (Job job :
                jobList) {
            job.setListeners();
        }
    }

    private void registerCommandsAndEvents ()
    {
        CustomBlockData.registerListener(this);

        PlayerDataListener playerDataListener = new PlayerDataListener(dataManager);
        Bukkit.getPluginManager().registerEvents(playerDataListener, this);
        this.getServer().getPluginManager().registerEvents(new DoorClickListener(), this);
        this.getServer().getPluginManager().registerEvents(new DoorPlaceListener(), this);
        this.getServer().getPluginManager().registerEvents(new DoorGUIListener(), this);
        this.getServer().getPluginManager().registerEvents(new DeleteCellsGUIListener(), this);
        this.getServer().getPluginManager().registerEvents(new OnDoorClick(), this);

        skillsCommand = new SkillCommand();
        skillsCommand.registerMainCommand(this, "skills");

        new CellCommand().registerMainCommand(this, "cells");
        new CoinCommand().registerMainCommand(this, "coin");

        GetKey getKeyExecutor = new GetKey();
        this.getCommand("getkey").setExecutor(getKeyExecutor);
        this.getCommand("getkey").setTabCompleter(getKeyExecutor);

    }

    @Override
    public void onDisable() {
        getLogger().info(this.getName() + "" + "Disabling LockdUp...");
        dataManager.saveAllData();
        try {
            CellManager.saveCells();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startSchedulerLong(){
        new BukkitRunnable() {
            public void run() {
                getLogger().info( "Saving LockdUp...");
                dataManager.saveAllData();
                try {
                    CellManager.saveCells();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskTimerAsynchronously(this, 20L * 60 * 5, 20L * 60 * 5); //5min
    }
    private void startSchedulerSecond() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                CellManager.removeSecond();
            }
        }, 0L, 20L); //first is delay time, second is repeating time
    }

    public static MainCommand getSkillsCommand() {
        return skillsCommand;
    }
    public static int getXP_PER_LEVEL() {
        return XP_BASE;
    }

    public static double getXP_MULTIPLIER_PER_LEVEL() {
        return XP_MULTIPLIER_PER_LEVEL;
    }
}
