package org.pipeman.pipo.commands;

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
                "Step 1: Install Fabric",
                "You can install fabric here",
                false);

        builder.addField(
                "Step 2: Download required mods",
                "Click [here](https://kryeit.com/mods) to download the mods.\n" +
                        "Or download the [Modrinth](https://modrinth.com/modpack/kryeit) modpack",
                false
        );

        builder.addField(
                "Suggested launcher",
                "[Modrinth app](https://modrinth.com/app)",
                false
        );
        return builder.build();
    }
}
