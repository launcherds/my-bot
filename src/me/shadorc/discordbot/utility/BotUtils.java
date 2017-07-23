package me.shadorc.discordbot.utility;

import me.shadorc.discordbot.CommandManager;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

public class BotUtils {

	private static CommandManager cmdManager = new CommandManager();

	public static void sendMessage(String message, IChannel channel) {
		try {
			if(!message.isEmpty()) {
				RequestBuffer.request(() -> {
					channel.sendMessage(message);
				});
			}
		} catch (MissingPermissionsException e) {
			Log.warn("Missing permissions for channel \"" + channel.getName() + "\" (ID: " + channel.getStringID() + ")");
		} catch (DiscordException e) {
			Log.error(e.getErrorMessage(), e);
		}
	}

	public static void sendEmbed(EmbedObject embed, IChannel channel) {
		try {
			RequestBuffer.request(() -> {
				channel.sendMessage(embed);
			});
		} catch (MissingPermissionsException e) {
			Log.warn("Missing permissions for channel \"" + channel.getName() + "\" (ID: " + channel.getStringID() + ")");
		} catch (DiscordException e) {
			Log.error(e.getErrorMessage(), e);
		}
	}

	public static void executeCommand(MessageReceivedEvent event) {
		cmdManager.manage(event);
	}
}
