package me.emiel.lockdup.Storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import me.emiel.lockdup.Model.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
@RequiredArgsConstructor
public class PlayerJsonPersistenceHandler implements PlayerPersistenceHandler {
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    private final JavaPlugin plugin;

    private File getPlayerDataFile(UUID playerId) {
        File folder = plugin.getDataFolder();
        File dataFolder = new File(folder, "playerdata");
        File playerFile = new File(dataFolder, playerId.toString() + ".json");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        return playerFile;
    }

    @Override
    public CompletableFuture<PlayerData> loadDataAsync(UUID playerId) {
        File playerFile = getPlayerDataFile(playerId);
        return CompletableFuture.supplyAsync(() -> {
            if (!playerFile.exists()) {
                return new PlayerData();
            }
            try {
                String json = Files.readString(playerFile.toPath());
                PlayerData data =gson.fromJson(json, PlayerData.class);
                return data == null ? new PlayerData()  : data;
            } catch (IOException e) {
                Bukkit.getServer().broadcastMessage(e.getMessage());

                e.printStackTrace();
                return new PlayerData();
            }
        });
    }

    @Override
    public void saveDataAsync(UUID playerId, PlayerData playerData) {
        File playerFile = getPlayerDataFile(playerId);
        String json = gson.toJson(playerData);
        CompletableFuture.runAsync(() -> {
            try {
                Files.writeString(playerFile.toPath(), json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
