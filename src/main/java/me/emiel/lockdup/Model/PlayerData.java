package me.emiel.lockdup.Model;

import lombok.Data;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerData {
        private Map<String, Level> levelMap = new HashMap<>();

        public void addExp(String skill, int exp, Player player){
                levelMap.computeIfAbsent(skill, key -> new Level());
                levelMap.get(skill).addXp(exp, skill, player);
        }
        public int getExp(String skill){
                levelMap.computeIfAbsent(skill, key -> new Level());
                return levelMap.get(skill).getCurrentXp();
        }
        public int getExpForNextLevel(String data){
                levelMap.computeIfAbsent(data, key -> new Level());
                Level level = levelMap.get(data);
                return level.calculateTotalExpNeeded(level.getLevel() + 1);
        }
        public Level getLevel(String skill) {
                levelMap.computeIfAbsent(skill, key -> new Level());
                return levelMap.get(skill);
        }
        public void resetAll(){
                Map<String, Level> mapData = levelMap;
                mapData.forEach((key, value) -> {
                        value = new Level();
                        mapData.replace(key, value);
                });
                levelMap = mapData;
        }


}
