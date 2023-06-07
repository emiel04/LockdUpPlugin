package me.emiel.lockdup.Commands.Skills;

import me.emiel.lockdup.CommandManagerLib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.LockdUp;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ListSkills implements SubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Command used to list all existing skills";
    }

    @Override
    public String getSyntax() {
        return "/skills list";
    }

    @Override
    public String getPermission() {
        return "skills.list";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        ArrayList<String> skills = LockdUp.getSkillManager().getSkills();
        MessageSender.sendLine(sender);
        for (String skill :
                skills) {
            MessageSender.sendMessage(sender, "  • " + skill);
        }
        MessageSender.sendLine(sender);

    }
}
