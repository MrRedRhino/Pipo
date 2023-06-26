package org.pipeman.player_info_bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class CommandMods {
    public static void handle(SlashCommandInteractionEvent event) {
        event.replyEmbeds(createEmbed())
                .mentionRepliedUser(false)
                .queue();
    }

    public static MessageEmbed createEmbed() {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(new Color(59, 152, 0))
                .setTitle("Joining kryeit.com");

        builder.addField(
                "Step 1: Install Forge",
                "We recommend Forge version 40.1.60 for Minecraft 1.18.2",
                false);

        builder.addField(
                "Step 2: Download required mods",
                "Click [here](https://kryeit.com/mods) to download the mods.",
                false
        );
        return builder.build();
    }
}
