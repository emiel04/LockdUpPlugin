package me.emiel.lockdup.Storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import me.emiel.lockdup.Model.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class SkillDataJsonPersistenceHandler implements SkillPersistenceHandler {
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    private final JavaPlugin plugin;

    private File getSkillDataFile() {
        File folder = plugin.getDataFolder();
        return new File(folder, "skills" + ".json");
    }
    @Override
    public CompletableFuture<String[]> loadDataAsync() {
        File skillFile = getSkillDataFile();
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!skillFile.exists()) {
                    return new String[0];
                }
                String json = Files.readString(skillFile.toPath());
                String[] skills = gson.fromJson(json, String[].class);
                return skills == null ? new String[0] : skills;
            } catch (IOException e) {
                Bukkit.getServer().broadcastMessage(e.getMessage());
                e.printStackTrace();
                return new String[0];
            }
        });
    }

    @Override
    public void saveDataAsync(ArrayList<String> skills) {
        File skillFile = getSkillDataFile();
        String json = gson.toJson(skills.toArray());
        CompletableFuture.runAsync(() -> {
            try {
                Files.writeString(skillFile.toPath(), json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
