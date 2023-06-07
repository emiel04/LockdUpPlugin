package me.emiel.lockdup.model;

import org.bukkit.Material;

public enum AxeTier {
    WOODEN(Material.WOODEN_AXE, false),
    STONE(Material.STONE_AXE, false),
    ENCHANTED_STONE(Material.STONE_AXE, true),
    IRON(Material.IRON_AXE, false),
    ENCHANTED_IRON(Material.IRON_AXE, true),
    ENCHANTED_DIAMOND(Material.DIAMOND_AXE, true),
    GOLD(Material.GOLDEN_AXE, false),
    ENCHANTED_GOLD(Material.GOLDEN_AXE, true),
    ENCHANTED_NETHERITE(Material.NETHERITE_AXE, true);

    private final Material material;
    private final boolean enchanted;

    AxeTier(Material material, boolean enchanted) {
        this.material = material;
        this.enchanted = enchanted;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isEnchanted() {
        return enchanted;
    }

    public String getDisplayName() {
        String displayName = material.name().toLowerCase().replace("_", " ");
        displayName = Character.toUpperCase(displayName.charAt(0)) + displayName.substring(1);
        if (enchanted) {
            displayName = "Enchanted " + displayName;
        }
        return displayName;
    }
}