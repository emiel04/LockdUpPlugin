package me.emiel.lockdup.storage;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerDataListener implements Listener {

    private final DataManager playerDataManager;

    @EventHandler
    public void onAsyncLogin(AsyncPlayerPreLoginEvent event) {
        playerDataManager.loadPlayerData(event.getUniqueId());
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        playerDataManager.unloadPlayerData(event.getPlayer().getUniqueId());
    }

}