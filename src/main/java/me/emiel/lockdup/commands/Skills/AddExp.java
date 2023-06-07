package me.emiel.lockdup.commands.Skills;

import me.emiel.lockdup.commandmanagerlib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.Managers.SkillManager;
import me.emiel.lockdup.Storage.DataManager;
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
        switch (index)
        {
            case 0:
                /* Tab completes names of all online players on the first index. */
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());

            /* Tab completes names of all available worlds on the second index. */
            case 1:
                return new ArrayList<>(LockdUp.getSkillManager().getSkills());
        }

        return null;
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
