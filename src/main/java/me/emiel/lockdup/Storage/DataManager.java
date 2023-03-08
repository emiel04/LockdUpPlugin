package me.emiel.lockdup.Storage;

import me.emiel.lockdup.Model.PlayerData;
import me.emiel.lockdup.Model.SkillData;

import java.util.*;
import java.util.concurrent.CompletableFuture;
//7smile7 — Today at 22:55
//IO should be done async so it doesnt really matter if you need 0.5ms or 5ms to read/write files.
//Files are decent if you only need the data to exist on one server. It it needs to be shared between several
//instances then you need to use a Database.
//
//There is a general approach to data lifecycles. Let me explain a bit more.
//---------
//7smile7 — Today at 22:59
//1) Data exists in 2 states: Live and persisted. Or in other terms: In memory or on disk.
//2) Data has scopes. Usually you would differentiate between application scope and session scope.
//     This defines when data should be loaded from disk into memory.
//Application scoped data is loaded when the application starts and saved when the application stops.
//Session scoped data is loaded when a session starts and saved when a session stops.
//Now we just need to define a "session" in minecraft terms:
//A session of a player usually starts when he connects to the server and stops when he quits.
//The same goes for chunks for example. A chunks session starts when the chunk is loaded and stops when the chunk is unloaded again.
//
//Let me explain how to approach player sessions....

//BUCHARD
//I typically:
//
//Project startup:
//
//1) Keep data and only edit the cached values for said player (Typically you have a HashMap<UUID, PlayerDataClass>) inside of a DataManager class that handles all this loading/saving
//
//2) Have singleton timer that runs async every x minutes, where it loops through the values of that map, and writes them to file (use the async bukkit timer)
//
//Player join:
//
//1) Load data on async player join event, make sure to execute a blocking get no async or else the player logs in before data is loaded (This is the only non-async operation you should do, is the GET in an async player join)
//
//2) On player disconnect, write they're cache to file and remove from cache, repeat
//
//Basically what smile was saying but simpled down to minecraft terms
//
//This is just something basic, you can get more complex the more requirements you need, like with smile said when you need data synced between servers, or servers need to share data
//7smile7 — Today at 23:11
//First we need to define our data class.
//@Data
//public class PlayerData {
//  private long firstLogin;
//  private ClanUserData clanUserData;
//  private JobData jobData;
//}
//
//@Data just generates getter, setter, toString, equals and hashCode methods.
//
//Next we define an Interface which has the task to save/load this data. The implementation can be done with Files or whatever.
//public interface PlayerPersistenceHandler {
//  CompletableFuture<PlayerData> loadDataAsync(UUID playerId);
//  void saveDataAsync(UUID playerId, PlayerData playerData);
//}
//
//Dont be scared by the CompletableFuture. You can simplify this if you want.
//Next we define our manager class that holds the live player data. This is our single entry point for interacting with player data.
//@RequiredArgsConstructor
//public class PlayerDataManager {
//
//  private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
//  private final PlayerPersistenceHandler playerPersistenceHandler;
//
//  public void loadPlayerData(UUID playerId) {
//    PlayerData loadedData = playerPersistenceHandler.loadDataAsync(playerId).join();
//    playerDataMap.put(playerId, loadedData);
//  }
//
//  public void unloadPlayerData(UUID playerId) {
//    PlayerData liveData = playerDataMap.remove(playerId);
//    if(liveData == null) {
//      return;
//    }
//    playerPersistenceHandler.saveDataAsync(playerId, liveData);
//  }
//
//  public PlayerData getPlayerData(UUID playerId) {
//    return playerDataMap.get(playerId);
//  }
//
//}
//
//At last we are defining our listener. This couples the data with a players session:
//@RequiredArgsConstructor
//public class PlayerDataListener implements Listener {
//
//  private final PlayerDataManager playerDataManager;
//
//  @EventHandler
//  public void onAsyncLogin(AsyncPlayerPreLoginEvent event) {
//    playerDataManager.loadPlayerData(event.getUniqueId());
//  }
//
//  @EventHandler
//  public void onQuit(PlayerQuitEvent event) {
//    playerDataManager.unloadPlayerData(event.getPlayer().getUniqueId());
//  }
//
//}
//An example usage would look like this:
//public final class SpigotSandbox extends JavaPlugin implements Listener {
//
//  private PlayerDataManager playerDataManager;
//  private PlayerPersistenceHandler playerPersistenceHandler;
//
//  @Override
//  public void onEnable() {
//    this.playerPersistenceHandler = new PlayerFilePersistenceHandler();
//    this.playerDataManager = new PlayerDataManager(playerPersistenceHandler);
//    PlayerDataListener playerDataListener = new PlayerDataListener(playerDataManager);
//    Bukkit.getPluginManager().registerEvents(playerDataListener, this);
//  }
//
//}
//
//If you need the player data anywhere, then all you have to do is pass the PlayerDataManager instance there.
//https://paste.md-5.net/ipehixamic.java
//=
//package com.gestankbratwurst.spigotsandbox;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import lombok.RequiredArgsConstructor;
//import org.bukkit.plugin.java.JavaPlugin;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.UUID;
//import java.util.concurrent.CompletableFuture;
//
//@RequiredArgsConstructor
//public class PlayerJsonPersistenceHandler implements PlayerPersistenceHandler {
//
//  private final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
//  private final JavaPlugin plugin;
//
//  private File getPlayerDataFile(UUID playerId) {
//    File folder = plugin.getDataFolder();
//    File dataFolder = new File(folder, "playerdata");
//    File playerFile = new File(dataFolder, playerId.toString() + ".json");
//    if (!dataFolder.exists()) {
//      dataFolder.mkdirs();
//    }
//    return playerFile;
//  }
//
//  @Override
//  public CompletableFuture<PlayerData> loadDataAsync(UUID playerId) {
//    File playerFile = getPlayerDataFile(playerId);
//    return CompletableFuture.supplyAsync(() -> {
//      if (!playerFile.exists()) {
//        return new PlayerData();
//      }
//      try {
//        String json = Files.readString(playerFile.toPath());
//        return gson.fromJson(json, PlayerData.class);
//      } catch (IOException e) {
//        e.printStackTrace();
//        return new PlayerData();
//      }
//    });
//  }
//
//  @Override
//  public void saveDataAsync(UUID playerId, PlayerData playerData) {
//    File playerFile = getPlayerDataFile(playerId);
//    String json = gson.toJson(playerData);
//    CompletableFuture.runAsync(() -> {
//      try {
//        Files.writeString(playerFile.toPath(), json);
//      } catch (IOException e) {
//        throw new RuntimeException(e);
//      }
//    });
//  }
//}

public class DataManager {
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    private final ArrayList<SkillData> skillDataList = new ArrayList<SkillData>();
    private final PlayerPersistenceHandler playerPersistenceHandler;
    private final SkillPersistenceHandler skillPersistenceHandler;

    public DataManager(PlayerPersistenceHandler playerPersistenceHandler, SkillPersistenceHandler skillPersistenceHandler) {
        this.playerPersistenceHandler = playerPersistenceHandler;
        this.skillPersistenceHandler = skillPersistenceHandler;
    }

    public void loadPlayerData(UUID playerId) {
        PlayerData loadedData = playerPersistenceHandler.loadDataAsync(playerId).join();
        playerDataMap.put(playerId, loadedData);
    }

    public void unloadPlayerData(UUID playerId) {
        PlayerData liveData = playerDataMap.remove(playerId);
        if(liveData == null) {
            return;
        }
        playerPersistenceHandler.saveDataAsync(playerId, liveData);
    }
    public void saveAllPlayerData() {
        for (Map.Entry<UUID,PlayerData> data : playerDataMap.entrySet()) {
            if(data == null) {
                continue;
            }
            playerPersistenceHandler.saveDataAsync(data.getKey(), data.getValue());
        }
    }

    public PlayerData getPlayerData(UUID playerId) {
        if(playerDataMap.containsKey(playerId)){
            return playerDataMap.get(playerId);
        }
        loadPlayerData(playerId);
        if(playerDataMap.containsKey(playerId)){
            return playerDataMap.get(playerId);
        }
        return null;
    }
    public void resetPlayerData(UUID playerId) {
        PlayerData pd = getPlayerData(playerId);
        pd.resetAll();
        playerDataMap.replace(playerId, pd);
    }

    private void saveSkillData() {
        for (SkillData skill : skillDataList) {
            if(skill == null) {
                continue;
            }
            skillPersistenceHandler.saveDataAsync(skill.getName(), skill);
        }
    }

    public void saveAllData(){
        saveAllPlayerData();
        saveSkillData();
    }

    public void loadAllDate(){
        loadSkillData();
    }

    private void loadSkillData() {;
        SkillData[] skillData = skillPersistenceHandler.loadDataAsync().join();
        skillDataList.addAll(Arrays.asList(skillData));
    }

}
