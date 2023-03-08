package me.emiel.lockdup.Storage;

import me.emiel.lockdup.Model.PlayerData;
import me.emiel.lockdup.Model.SkillData;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface SkillPersistenceHandler {
    CompletableFuture<SkillData[]> loadDataAsync();
    void saveDataAsync(String skill, SkillData skillData);
}
