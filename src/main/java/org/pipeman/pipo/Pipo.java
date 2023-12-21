package org.pipeman.pipo;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.World;
import org.pipeman.pipo.commands.CommandListener;
import org.pipeman.pipo.listener.discord.DownloadModsListener;
import org.pipeman.pipo.listener.minecraft.PlayerLogin;
import org.pipeman.pipo.listener.minecraft.PlayerQuit;
import org.pipeman.pipo.storage.LastTimePlayed;
import org.pipeman.pipo.storage.MinecraftToDiscord;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Timer;

public final class Pipo implements DedicatedServerModInitializer {
    private static final Timer KRYEITOR_TIMER = new Timer();
    private static final Timer COLLABORATOR_TIMER = new Timer();

    public static JDA JDA;
    public final static String KRYEIT_GUILD = "910626990468497439";
    public LastTimePlayed lastTimePlayed;
    public MinecraftToDiscord minecraftToDiscord;
    public static Pipo instance;

    @Override
    public void onInitializeServer() {
        instance = this;

        try {
            lastTimePlayed = new LastTimePlayed("mods/last_time_played");
            minecraftToDiscord = new MinecraftToDiscord("mods/minecraft_to_discord");

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

        scheduleTimers();
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

    public static Pipo getInstance() {
        return instance;
    }

    public void scheduleTimers() {
        long interval = Duration.ofMinutes(30).toMillis();
        KRYEITOR_TIMER.schedule(new Autorole(JDA.getRoleById(Autorole.KRYEITOR)), interval, interval);
        COLLABORATOR_TIMER.schedule(new Autorole(JDA.getRoleById(Autorole.COLLABORATOR)), interval, interval);
    }

}
