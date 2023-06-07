package me.emiel.lockdup.storage;

import me.emiel.lockdup.model.PlayerData;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerPersistenceHandler {
    CompletableFuture<PlayerData> loadDataAsync(UUID playerId);
    void saveDataAsync(UUID playerId, PlayerData playerData);
}
