package org.pipeman.player_info_bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.pipeman.player_info_bot.Leaderboard;
import org.pipeman.player_info_bot.Leaderboard.LeaderboardEntry;
import org.pipeman.player_info_bot.Utils;

import java.awt.*;
import java.text.MessageFormat;
import java.util.List;

public class CommandTopN {
    public static void handle(SlashCommandInteractionEvent event) {
        int limit = event.getOption("limit", 10, OptionMapping::getAsInt);
        int offset = event.getOption("offset", 0, OptionMapping::getAsInt);

        if (limit < 1) {
            event.replyEmbeds(Utils.createErrorEmbed("Limit must be greater than 0"))
                    .setEphemeral(true)
                    .queue();
            return;
        }

        if (offset < 0) {
            event.replyEmbeds(Utils.createErrorEmbed("Offset must be greater than or equal to 0"))
                    .setEphemeral(true)
                    .queue();
            return;
        }

        List<LeaderboardEntry> leaderboard = Leaderboard.getLeaderboard(limit, offset);
        if (leaderboard.isEmpty()) {
            event.replyEmbeds(Utils.createErrorEmbed("Offset must be less than the leaderboard's length"))
                    .setEphemeral(true)
                    .queue();
            return;
        }

        replyWithLeaderboard(leaderboard, offset + 1, event);
    }

    public static void handleTop10(SlashCommandInteractionEvent event) {
        replyWithLeaderboard(Leaderboard.getLeaderboard(10), 1, event);
    }

    private static void replyWithLeaderboard(List<LeaderboardEntry> entries, int startRank, SlashCommandInteractionEvent event) {
        StringBuilder content = new StringBuilder();
        int rank = startRank;
        for (LeaderboardEntry le : entries) {
            String newLine = MessageFormat.format(
                    "**{0}:** {1} ({2}h)\n",
                    rank, escapeName(le.name()), Utils.round(le.playtime() / 3_600d, 1)
            );
            if (newLine.length() + content.length() > MessageEmbed.VALUE_MAX_LENGTH) break;

            content.append(newLine);
            rank++;
        }
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Leaderboard")
                .addField("Leaderboard", content.toString(), false)
                .setColor(new Color(59, 152, 0))
                .build();
        event.replyEmbeds(embed).queue();
    }

    private static String escapeName(String name) {
        return name.replace("_", "\\_");
    }
}
