package me.emiel.lockdup.Model;

import me.emiel.lockdup.LockdUp;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class HelpModel {
    private String title;
    private Map<String, String> content;

    public HelpModel(String title, Map<String, String> content) {
        this.title = title;
        this.content = content;
    }

    public String toString(){
        String output = "";
        String mainColor = LockdUp.getInstance().getConfig().getString("main_color");
        String accentColor = LockdUp.getInstance().getConfig().getString("accent_color1");

        output +=
                ChatColor.translateAlternateColorCodes('&',
                        "&8&l&m                 [ &r"+ mainColor + title + "&8&l&m ]                  &r\n");
        for (Map.Entry<String, String> set :
                content.entrySet()) {
            output += ChatColor.translateAlternateColorCodes('&',
                    "   " + accentColor + set.getKey() + " w&f" + set.getValue() + "\n");
        }
        String spaces = "";
        for (int i = 0; i < title.length(); i++) {
            spaces += " ";
        }
        output += ChatColor.translateAlternateColorCodes('&',
                "&8&l&m                   " + spaces + "                    ");

        return output;
    }
}
