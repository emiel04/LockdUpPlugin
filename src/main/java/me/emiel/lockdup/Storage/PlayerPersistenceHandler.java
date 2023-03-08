package me.emiel.lockdup.Storage;

import me.emiel.lockdup.Model.PlayerData;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerPersistenceHandler {
    CompletableFuture<PlayerData> loadDataAsync(UUID playerId);
    void saveDataAsync(UUID playerId, PlayerData playerData);
}
