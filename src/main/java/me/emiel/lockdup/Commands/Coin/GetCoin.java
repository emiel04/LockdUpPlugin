package me.emiel.lockdup.Commands.Coin;

import me.emiel.lockdup.CommandManagerLib.SubCommand;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.Managers.CoinManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GetCoin implements SubCommand {
    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescription() {
        return "Command used to get coin";
    }

    @Override
    public String getSyntax() {
        return "/coin get <coin, coin+> <amount>";
    }

    @Override
    public String getPermission() {
        return "coin.get";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return switch (index) {
            case 0 -> List.of("coin", "coin+");
            case 1 -> List.of("1", "16", "32", "48", "64");
            default -> null;
        };
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }

        Player p = (Player) sender;
        if (args.length == 0) {
            displayUsage(sender);
            return;
        }

        String arg = args[0].toLowerCase();
        if (arg.equals("coin") || arg.equals("c")) {
            if (args.length == 1) {
                CoinManager.giveCoin(p, 1);
                MessageSender.sendMessageWithPrefix(sender, "Given a coin");
                return;
            } else if (isNumeric(args[1])) {
                int amount = Integer.parseInt(args[1]);
                CoinManager.giveCoin(p, amount);
                MessageSender.sendMessageWithPrefix(sender, "Given " + amount + " coins");
                return;
            }
        } else if (arg.equals("coin+") || arg.equals("coinplus") || arg.equals("cp")) {
            if (args.length == 1) {
                CoinManager.giveCoinPlus(p, 1);
                MessageSender.sendMessageWithPrefix(sender, "Gave a coin plus");
                return;
            } else if (isNumeric(args[1])) {
                int amount = Integer.parseInt(args[1]);
                CoinManager.giveCoinPlus(p, amount);
                MessageSender.sendMessageWithPrefix(sender, "Given " + amount + " coin plus");
                return;
            }
        }
        displayUsage(sender);
    }

    private void displayUsage(CommandSender sender) {
        MessageSender.sendLine(sender);
        MessageSender.sendMessageWithPrefix(sender, "&c&lPlease use the correct format:");
        MessageSender.sendMessageWithPrefix(sender, "    &c/coin get <coin, coin+> <amount>");
        MessageSender.sendLine(sender);
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
