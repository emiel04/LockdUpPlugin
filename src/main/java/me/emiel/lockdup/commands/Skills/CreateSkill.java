package me.emiel.lockdup.commands.Skills;

import me.emiel.lockdup.commandmanagerlib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.Helper.StringValidator;
import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.Managers.SkillManager;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CreateSkill implements SubCommand {
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Command used to create a new skill";
    }

    @Override
    public String getSyntax() {
        return "/skills create <name>";
    }

    @Override
    public String getPermission() {
        return "skills.create";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 0 || args[0].length() < 2){
            MessageSender.sendErrorWithPrefix(sender, "Please provide a good skill name!");
            return;
        }
        String name = args[0];
        SkillManager skillManager = LockdUp.getSkillManager();

        if (skillManager.exists(name)){
            MessageSender.sendErrorWithPrefix(sender, "This skill already exists!");
            return;
        }
        if (!StringValidator.isValidFileName(name)){
            MessageSender.sendErrorWithPrefix(sender, "Don't use special characters!");
            return;
        }
        skillManager.add(name);
        MessageSender.sendMessageWithPrefix(sender, "Created skill: " + name);

    }
}
