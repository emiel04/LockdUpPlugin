package me.emiel.lockdup.Commands.Skills;

import me.emiel.lockdup.CommandManagerLib.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class AddExp implements SubCommand {
    @Override
    public String getName() {
        return "addexp";
    }

    @Override
    public String getDescription() {
        return "Adds exp to a certain skill level of a player.";
    }

    @Override
    public String getSyntax() {
        return "/skills addexp <player> <skill>";
    }

    @Override
    public String getPermission() {
        return "lockdup.skills.addexp";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        switch (index)
        {
            case 0:
                /* Tab completes names of all online players on the first index. */
                return Bukkit.getOnlinePlayers().stream().map(player -> player.getName()).collect(Collectors.toList());

            /* Tab completes names of all available worlds on the second index. */
            case 1:
                return Bukkit.getWorlds().stream().map(world -> world.getName()).collect(Collectors.toList());
        }

        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Bukkit.broadcastMessage("adding exp");
    }
}
