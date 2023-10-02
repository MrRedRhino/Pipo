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
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.World;
import org.pipeman.player_info_bot.commands.CommandListener;
import org.pipeman.player_info_bot.listener.PlayerLogin;
import org.pipeman.player_info_bot.listener.PlayerQuit;
import org.pipeman.player_info_bot.storage.LastTimePlayed;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public final class PlayerInfoBot implements ModInitializer {
    public static JDA JDA;
    public final static String KRYEIT_GUILD = "1064545752103276544";
    public LastTimePlayed lastTimePlayed;
    public static PlayerInfoBot instance;
    @Override
    public void onInitialize() {
        instance = this;

        try {
            lastTimePlayed = new LastTimePlayed("config/last_time_played");

            InputStream in = this.getClass().getResourceAsStream("/secret.txt");
            if (in == null) {
                throw new FileNotFoundException("Resource not found: secret.txt");
            }
            String token = new String(in.readAllBytes()).trim();
            in.close();
            JDA = JDABuilder.createDefault(token)
                    .setActivity(Activity.watching("to 0 players"))
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                    .build();

            JDA.addEventListener(new CommandListener());
            JDA.addEventListener(new DownloadModsListener());

            JDA.awaitReady();
        } catch (InterruptedException | IOException e) {
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

        registerEvents();
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

    public void registerEvents() {
        ServerPlayConnectionEvents.JOIN.register(new PlayerLogin());
        ServerPlayConnectionEvents.DISCONNECT.register(new PlayerQuit());
    }

    public static boolean isMe(ISnowflake user) {
        return user != null && user.getIdLong() == JDA.getSelfUser().getIdLong();
    }

    public static PlayerInfoBot getInstance() {
        return instance;
    }

}
