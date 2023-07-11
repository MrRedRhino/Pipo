package org.pipeman.player_info_bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;
import org.pipeman.player_info_bot.commands.CommandListener;

public final class PlayerInfoBot extends JavaPlugin {
    public static JDA JDA;

    @Override
    public void onEnable() {
        String[] arguments = System.getProperty("sun.java.command").split(" ");
        String token = getArgument("token", arguments);
        System.out.println(token);
        JDA = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        JDA.addEventListener(new CommandListener());
        JDA.addEventListener(new DownloadModsListener());

        try {
            JDA.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Guild guild = JDA.getGuildById(Long.parseLong(getArgument("guild", arguments)));
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
    }

    @Override
    public void onDisable() {
        JDA.shutdown();
        JDA = null;
    }

    public static boolean isMe(ISnowflake user) {
        return user != null && user.getIdLong() == JDA.getSelfUser().getIdLong();
    }

    private static String getArgument(String key, String[] arguments) {
        key = key + "=";
        for (String argument : arguments) {
            if (argument.startsWith(key)) {
                return argument.substring(key.length());
            }
        }
        throw new RuntimeException("Could not find argument " + key);
    }
}
