package org.pipeman.player_info_bot.commands;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "online" -> CommandOnline.handle(event);
            case "playerinfo" -> CommandPlayerinfo.handle(event);
            case "top-10" -> CommandTopN.handleTop10(event);
            case "top-n" -> CommandTopN.handle(event);
            case "mods" -> CommandMods.handle(event);
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (!event.getName().equals("playerinfo")) return;
        String value = event.getOption("playername", "", OptionMapping::getAsString);
        event.replyChoiceStrings(getNameSuggestions(value.toLowerCase())).queue();
    }

    private List<String> getNameSuggestions(String input) {
        List<String> players = new ArrayList<>();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (players.size() >= 5) break;
            String playerName = player.getName();
            if (playerName != null && playerName.toLowerCase().contains(input) && !players.contains(playerName)) {
                players.add(playerName);
            }
        }
        return players;
    }
}
