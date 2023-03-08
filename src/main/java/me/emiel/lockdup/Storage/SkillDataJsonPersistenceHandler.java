package me.emiel.lockdup.Storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import me.emiel.lockdup.Model.PlayerData;
import me.emiel.lockdup.Model.SkillData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class SkillDataJsonPersistenceHandler implements SkillPersistenceHandler {
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    private final JavaPlugin plugin;

    private File getSkillDataFile(String skill) {
        File folder = plugin.getDataFolder();
        File dataFolder = new File(folder, "skills");
        File playerFile = new File(dataFolder, skill.toString() + ".json");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        return playerFile;
    }
    private File[] getAllSkillDataFiles() {
        File folder = plugin.getDataFolder();
        File dataFolder = new File(folder, "skills");
        File[] files = dataFolder.listFiles((dir, name) -> name.endsWith(".json"));
        return files;
    }
    @Override
    public CompletableFuture<SkillData[]> loadDataAsync() {
        File[] skillFiles = getAllSkillDataFiles();
        CompletableFuture<SkillData>[] futures = new CompletableFuture[skillFiles.length];

        for (int i = 0; i < skillFiles.length; i++) {
            File skillFile = skillFiles[i];
            futures[i] = CompletableFuture.supplyAsync(() -> {
                try {
                    if (!skillFile.exists()) {
                        return new SkillData("", "");
                    }
                    String json = Files.readString(skillFile.toPath());
                    SkillData data = gson.fromJson(json, SkillData.class);
                    return data == null ? new SkillData("", "") : data;
                } catch (IOException e) {
                    Bukkit.getServer().broadcastMessage(e.getMessage());
                    e.printStackTrace();
                    return new SkillData("", "");
                }
            });
        }

        return CompletableFuture.allOf(futures)
                .thenApply(v -> Arrays.stream(futures)
                        .map(CompletableFuture::join)
                        .toArray(SkillData[]::new));
    }

    @Override
    public void saveDataAsync(String skill, SkillData skillData) {
        File skillFile = getSkillDataFile(skill);
        String json = gson.toJson(skillData);
        CompletableFuture.runAsync(() -> {
            try {
                Files.writeString(skillFile.toPath(), json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
