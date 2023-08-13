package org.pipeman.player_info_bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.pipeman.player_info_bot.Utils;
import org.pipeman.player_info_bot.tps.Lag;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class CommandOnline {
    private static final DecimalFormat format = new DecimalFormat("#.##");

    public static void handle(SlashCommandInteractionEvent event) {
        StringBuilder players = new StringBuilder();
        List<String> names = Utils.map(Bukkit.getOnlinePlayers(), HumanEntity::getName);
        Collections.sort(names);

        for (String name : names) {
            players.append("- ").append(name.replace("_", "\\_")).append('\n');
        }

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Kryeit.com", "https://kryeit.com")
                .addField("There are " + names.size() + " players online:", players.toString(), false)
                .addField("TPS", format.format(Lag.getTPS()), false)
                .setColor(new Color(59, 152, 0))
                .build();

        event.replyEmbeds(embed).queue();
    }
}
