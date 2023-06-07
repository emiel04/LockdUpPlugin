package me.emiel.lockdup.Jobs;

import com.sk89q.worldguard.protection.flags.StateFlag;
import me.emiel.lockdup.Listeners.Woordworking.WoodWorkOnBlockBreak;
import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.Model.AxeTier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Woodworking extends Job{
    private final Map<Material, AxeTier> tierMapping = new HashMap<>();
    private final Map<Material, Integer> tierExp = new HashMap<>();

    public Woodworking(String name, StateFlag woodworkingFlag) {
        super(name, woodworkingFlag);
        loadTierMapping();
    }

    
    protected List<Listener> getListeners() {
        return List.of(
                new WoodWorkOnBlockBreak(flag, tierMapping, tierExp)
        );

    }

    private void loadTierMapping() {
        FileConfiguration config = LockdUp.getInstance().getConfig();

        if (config.isConfigurationSection("woodworking-tier-mapping")) {
            ConfigurationSection tierMappingSection = config.getConfigurationSection("woodworking-tier-mapping");
            for (var key : tierMappingSection.getKeys(false)) {
                ConfigurationSection tierSection = tierMappingSection.getConfigurationSection(key);
                if (tierSection == null) {
                    LockdUp.getInstance().getLogger().severe("Invalid configuration section for key: " + key);
                    continue;
                }

                Material material = Material.matchMaterial(key);
                if (material == null) {
                    LockdUp.getInstance().getLogger().severe("Invalid material: " + key);
                    continue;
                }

                String tierName = tierSection.getString("tier");
                int exp = tierSection.getInt("exp");
                AxeTier tier = AxeTier.valueOf(tierName);

                tierMapping.put(material, tier);
                tierExp.put(material, exp);
            }
        } else {
            LockdUp.getInstance().getLogger().severe("Missing 'woodworking-tier-mapping' section in the configuration file.");
        }
    }



}

