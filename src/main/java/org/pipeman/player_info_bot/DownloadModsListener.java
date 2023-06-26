package org.pipeman.player_info_bot;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.pipeman.player_info_bot.commands.CommandMods;

public class DownloadModsListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String content = event.getMessage().getContentDisplay().toLowerCase();

        if (content.contains("download") && content.contains("mods")) {
            event.getMessage().replyEmbeds(CommandMods.createEmbed())
                    .mentionRepliedUser(false)
                    .queue(m -> m.addReaction(Emoji.fromFormatted("\uD83D\uDEAB")).queue());
        }
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        EmojiUnion emojiUnion = event.getReaction().getEmoji();
        if (emojiUnion.getType() != Emoji.Type.UNICODE) return;
        UnicodeEmoji emoji = emojiUnion.asUnicode();
        if (!emoji.getFormatted().equals("\uD83D\uDEAB") || PlayerInfoBot.isMe(event.getMember())) return;

        event.retrieveMessage().queue(m -> {
            if (PlayerInfoBot.isMe(m.getAuthor())) m.delete().queue();
        });
    }
}
