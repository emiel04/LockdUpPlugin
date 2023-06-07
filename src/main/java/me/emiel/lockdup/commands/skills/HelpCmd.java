package me.emiel.lockdup.commands.skills;

import me.emiel.lockdup.commandmanagerlib.SubCommand;
import me.emiel.lockdup.helper.MessageSender;
import me.emiel.lockdup.LockdUp;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCmd implements SubCommand
{
    @Override
    public String getName ()
    {
        return "help";
    }

    @Override
    public String getDescription ()
    {
        return "Shows help for skills";
    }

    @Override
    public String getSyntax ()
    {
        return "/skills help";
    }

    @Override
    public String getPermission ()
    {
        return "skills.help";
    }

    @Override
    public List<String> getTabCompletion (int index, String[] args)
    {
        return null;
    }

    @Override
    public void perform (CommandSender sender, String[] args)
    {
        sender.sendMessage("LockdUp version " + LockdUp.getInstance().getDescription().getVersion());
        String mainColor = LockdUp.getInstance().getConfig().getString("main_color");
        String accentColor1 = LockdUp.getInstance().getConfig().getString("accent_color1");
        String prefix = LockdUp.getInstance().getConfig().getString("prefix");
        MessageSender.sendMessage(sender,
                "&8&l&m                  [ &r" +mainColor + "LockdUp&8&l&m ]                  \n"
        );

        for (SubCommand subCommand : LockdUp.getSkillsCommand().getSubCommands())
        {
            if (sender.hasPermission(subCommand.getPermission())){
                String msg = accentColor1 + subCommand.getSyntax() + " &8&l- &r" + subCommand.getDescription();
                if (sender.isOp())
                    msg += " (" + subCommand.getPermission();
                msg +=  ")";
                MessageSender.sendMessage(sender, msg);
            }
        }

        //add spaces
        StringBuilder spaces = new StringBuilder();
        spaces.append(" ".repeat(prefix.length()));
        MessageSender.sendMessage(sender,
                "&8&l&m                    "  + spaces + "                    "
        );
    }
}
