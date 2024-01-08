package org.pipeman.pipo.listener.discord;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.pipeman.pipo.Pipo;
import org.pipeman.pipo.commands.minecraft.CommandLinkDiscord;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DirectMessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {


        if (event.getAuthor().isBot()) return; // Ignore messages from other bots

        String message = event.getMessage().getContentRaw();

        int code = Integer.parseInt(message);

        if (!event.isFromType(ChannelType.PRIVATE)) return;

        if (CommandLinkDiscord.codes.containsKey(code)) {
            try {
                Pipo.getInstance().minecraftToDiscord.addElement(CommandLinkDiscord.codes.get(code), String.valueOf(event.getAuthor().getIdLong()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            event.getChannel().sendMessage("Your account has been linked to this UUID: " + CommandLinkDiscord.codes.get(code)).queue();
            CommandLinkDiscord.codes.remove(code);

        }
    }
}
