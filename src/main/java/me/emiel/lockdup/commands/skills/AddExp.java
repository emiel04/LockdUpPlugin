package me.emiel.lockdup.commands.skills;

import me.emiel.lockdup.commandmanagerlib.SubCommand;
import me.emiel.lockdup.helper.MessageSender;
import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.managers.SkillManager;
import me.emiel.lockdup.storage.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
        return "/skills addexp <player> <skill> <amount>";
    }

    @Override
    public String getPermission() {
        return "skills.addexp";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return switch (index) {
            case 0 -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            case 1 -> new ArrayList<>(LockdUp.getSkillManager().getSkills());
            default -> null;
        };
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 3){
            MessageSender.sendWrongCommandUsage(sender);
            MessageSender.sendMessage(sender, getSyntax());
            return;
        }
        int xp;
        try{
            xp = Integer.parseInt(args[2]);
        }catch (NumberFormatException e){
            MessageSender.sendErrorWithPrefix(sender, "Please provide a valid number!");
            return;
        }

        String playerName = args[0];
        String skillName = args[1];
        SkillManager skillManager = LockdUp.getSkillManager();
        DataManager dataManager = LockdUp.getDataManager();
        Player player = Bukkit.getPlayer(playerName);

        if (!skillManager.exists(skillName)){
            MessageSender.sendErrorWithPrefix(sender, "This skill does not exist!");
            return;
        }
        if (player == null){
            MessageSender.sendErrorWithPrefix(sender, "This player is not online!");
            return;
        }
        if (!skillManager.exists(skillName)){
            MessageSender.sendErrorWithPrefix(sender, "This skill does not exist!");
            return;
        }

        dataManager.getPlayerData(player.getUniqueId()).addExp(skillName, xp, player);
        MessageSender.sendMessageWithPrefix(sender, "Given user " + player.getName() + " " + xp + " exp for skill: &b" + skillName);

    }
}
