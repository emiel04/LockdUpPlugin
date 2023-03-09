package me.emiel.lockdup.Commands.Skills;

import me.emiel.lockdup.CommandManagerLib.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CreateSkill implements SubCommand {
    @Override
    public String getName() {
        return "createskill";
    }

    @Override
    public String getDescription() {
        return "Command used to create a new skill";
    }

    @Override
    public String getSyntax() {
        return "skills create <name>";
    }

    @Override
    public String getPermission() {
        return "lockdup.skills.create";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

    }
}
