package org.pipeman.player_info_bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.world.World;
import org.pipeman.player_info_bot.commands.CommandListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public final class PlayerInfoBot implements ModInitializer {
    public static JDA JDA;
    public final static String KRYEIT_GUILD = "910626990468497439";

    @Override
    public void onInitialize() {
        String token;

        try {
            InputStream in = this.getClass().getResourceAsStream("/secret.txt");
            if (in == null) {
                throw new FileNotFoundException("Resource not found: secret.txt");
            }
            token = new String(in.readAllBytes()).trim();
            in.close();
            JDA = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                    .setActivity(Activity.watching("to 0 players"))
                    .build()
                    .awaitReady();

            JDA.addEventListener(new CommandListener());
            JDA.addEventListener(new DownloadModsListener());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }


        try {
            JDA.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Guild guild = JDA.getGuildById(KRYEIT_GUILD);
        if (guild != null) {
            guild.upsertCommand("online", "Returns currently online players")
                    .queue();

            guild.upsertCommand("playerinfo", "Returns playtime information about the provided user")
                    .addOption(OptionType.STRING, "playername", "The player's name", true, true)
                    .queue();

            guild.upsertCommand("top-10", "Returns a top-10 list of the most active users")
                    .queue();

            guild.upsertCommand("mods", "Returns instructions on how to join the server")
                    .queue();

            guild.upsertCommand("top-n", "Returns a list of the most active users. Limit and offset can be specified")
                    .addOption(OptionType.INTEGER, "limit", "Limit of elements to return", true)
                    .addOption(OptionType.INTEGER, "offset", "Offset of the returned elements in the list", true)
                    .queue();

        } else {
            System.out.println("Guild is null!");
        }

        registerDisableEvent();
    }

    public void registerDisableEvent() {
        ServerWorldEvents.UNLOAD.register((server, world) -> {
            if (world.getRegistryKey() == World.OVERWORLD) {
                JDA.shutdown();
                JDA = null;
            }
        });
    }

    public static boolean isMe(ISnowflake user) {
        return user != null && user.getIdLong() == JDA.getSelfUser().getIdLong();
    }

}
