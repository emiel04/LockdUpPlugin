package me.emiel.lockdup.Model;

import lombok.Data;
import org.bukkit.entity.Player;

import java.util.*;

@Data
public class PlayerData {
        private int balance;
        public void addBalance(){
                balance++;
        }
        public void resetAll() {
        }
}
