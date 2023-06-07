package me.emiel.lockdup.Commands.Skills;

import me.emiel.lockdup.CommandManagerLib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.Helper.StringValidator;
import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.Managers.SkillManager;
import me.emiel.lockdup.Model.Level;
import me.emiel.lockdup.Model.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SkillInfo implements SubCommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Command used to view skill info";
    }

    @Override
    public String getSyntax() {
        return "/skills info <skill>";
    }

    @Override
    public String getPermission() {
        return "skills.info";
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
            MessageSender.sendErrorWithPrefix(sender, "Please provide a valid skill name!");
            return;
        }
        String skill = args[0];
        SkillManager skillManager = LockdUp.getSkillManager();
        Player player = (Player) sender;
        PlayerData playerData = LockdUp.getDataManager().getPlayerData(player.getUniqueId());
        if (!skillManager.exists(skill)){
            MessageSender.sendErrorWithPrefix(sender, "This skill does not exists!");
            return;
        }

        Level level = playerData.getLevel(skill);
        MessageSender.sendLine(sender);
        MessageSender.sendMessage(sender, MessageSender.mainColor + "Skill: &f" + skill);
        MessageSender.sendMessage(sender, MessageSender.mainColor + "Level: &f" + level.getLevel());
        MessageSender.sendMessage(sender, MessageSender.mainColor +
                "Progress: &f" + "&r&b[&f" + level.getCurrentXp() +  "/" + playerData.getExpForNextLevel(skill) + "&r&b]");
        MessageSender.sendMessage(sender, MessageSender.mainColor + "XP needed: &f" + (level.calculateTotalExpNeeded(level.getLevel() + 1) - level.getCurrentXp()));
        MessageSender.sendLine(sender);
    }
}
