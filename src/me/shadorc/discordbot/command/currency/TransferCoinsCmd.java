package me.shadorc.discordbot.command.currency;

import me.shadorc.discordbot.Config;
import me.shadorc.discordbot.Emoji;
import me.shadorc.discordbot.MissingArgumentException;
import me.shadorc.discordbot.Storage;
import me.shadorc.discordbot.User;
import me.shadorc.discordbot.command.Command;
import me.shadorc.discordbot.command.Context;
import me.shadorc.discordbot.utils.BotUtils;
import sx.blah.discord.util.EmbedBuilder;

public class TransferCoinsCmd extends Command {

	public TransferCoinsCmd() {
		super(false, "transfer", "transfert");
	}

	@Override
	public void execute(Context context) throws MissingArgumentException {
		if(context.getArg() == null) {
			throw new MissingArgumentException();
		}

		String[] splitCmd = context.getArg().split(" ", 2);
		if(splitCmd.length != 2 || context.getMessage().getMentions().size() != 1) {
			throw new MissingArgumentException();
		}

		try {
			int coins = Integer.parseInt(splitCmd[0]);
			User receiverUser = Storage.getUser(context.getGuild(), context.getMessage().getMentions().get(0));
			User senderUser = context.getUser();

			if(coins <= 0 || senderUser.equals(receiverUser)) {
				throw new MissingArgumentException();
			}

			if(senderUser.getCoins() < coins) {
				BotUtils.sendMessage(Emoji.BANK + " You don't have enough coins to do this.", context.getChannel());
				return;
			}

			senderUser.addCoins(-coins);
			receiverUser.addCoins(coins);

			BotUtils.sendMessage(Emoji.BANK + " " + senderUser.mention() + " has transfered " + coins + " coins to " + receiverUser.mention(), context.getChannel());
		} catch (NumberFormatException e1) {
			throw new MissingArgumentException();
		}
	}

	@Override
	public void showHelp(Context context) {
		EmbedBuilder builder = new EmbedBuilder()
				.withAuthorName("Help for " + this.getNames()[0] + " command")
				.withAuthorIcon(context.getClient().getOurUser().getAvatarURL())
				.withColor(Config.BOT_COLOR)
				.appendDescription("**Transfer of coins to the mentioned user.**")
				.appendField("Usage", context.getPrefix() + "transfer <coins> <@user>", false)
				.appendField("Restrictions", "The transferred amount must be strictly positive.\nYou can't transfer coins to yourself.", false);
		BotUtils.sendEmbed(builder.build(), context.getChannel());
	}
}
