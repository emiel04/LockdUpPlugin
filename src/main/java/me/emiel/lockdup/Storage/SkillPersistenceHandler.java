package me.emiel.lockdup.Storage;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public interface SkillPersistenceHandler {
    CompletableFuture<String[]> loadDataAsync();
    void saveDataAsync(ArrayList<String> skills);
}
