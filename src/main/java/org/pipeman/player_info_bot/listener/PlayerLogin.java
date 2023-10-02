package org.pipeman.player_info_bot.listener;

import jdk.jshell.execution.Util;
import net.dv8tion.jda.api.entities.Activity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.pipeman.player_info_bot.PlayerInfoBot;
import org.pipeman.player_info_bot.Utils;

import java.io.IOException;

public class PlayerLogin implements ServerPlayConnectionEvents.Join {
    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        try {
            PlayerInfoBot.getInstance().lastTimePlayed.deleteElement(handler.player.getUuid());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (Utils.getOnlinePlayersSize() == 1) {
            PlayerInfoBot.JDA.getPresence().setActivity(Activity.watching("to 1 player"));
        } else {
            PlayerInfoBot.JDA.getPresence().setActivity(Activity.watching("to " + Utils.getOnlinePlayersSize() + " players"));
        }
    }
}
