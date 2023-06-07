package me.emiel.lockdup.Commands.Skills;

import me.emiel.lockdup.CommandManagerLib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.Managers.SkillManager;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RemoveSkill implements SubCommand {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Command used to remove an existing skill";
    }

    @Override
    public String getSyntax() {
        return "/skills remove <name>";
    }

    @Override
    public String getPermission() {
        return "skills.remove";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        ArrayList<String> skills = LockdUp.getSkillManager().getSkills();

        switch (index){
            case 0:
                return skills;
            default:
                return null;
        }
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

        if (args.length < 1){
            MessageSender.sendErrorWithPrefix(sender, "This skill doesn't exists!");
            return;
        }

        String name = args[0];
        SkillManager skillManager = LockdUp.getSkillManager();

        if (!skillManager.exists(name)){
            MessageSender.sendErrorWithPrefix(sender, "This skill doesn't exists!");
            return;
        }

        skillManager.remove(name);
        MessageSender.sendMessageWithPrefix(sender, "Removed skill: " + name);
    }
}
