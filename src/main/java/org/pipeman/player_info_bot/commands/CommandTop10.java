package org.pipeman.player_info_bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.pipeman.player_info_bot.Leaderboard;
import org.pipeman.player_info_bot.Leaderboard.LeaderboardEntry;
import org.pipeman.player_info_bot.Utils;

import java.awt.*;
import java.text.MessageFormat;

public class CommandTop10 {
    public static void handle(SlashCommandInteractionEvent event) {
        int rank = 1;
        StringBuilder content = new StringBuilder();
        for (LeaderboardEntry le : Leaderboard.getLeaderboard(10)) {
            content.append(MessageFormat.format(
                    "**{0}:** {1} ({2}h)", rank, le.name(), Utils.round(le.playtime() / 3_600d, 1)
            )).append('\n');
            rank++;
        }
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Leaderboard")
                .addField("Top-10", content.toString(), false)
                .setColor(new Color(59, 152, 0))
                .build();
        event.replyEmbeds(embed).queue();
    }
}
