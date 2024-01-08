package org.pipeman.pipo.listener.minecraft;

import net.dv8tion.jda.api.entities.Activity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.pipeman.pipo.Pipo;
import org.pipeman.pipo.Utils;

import java.io.IOException;

public class PlayerQuit implements ServerPlayConnectionEvents.Disconnect {
    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        try {
            Pipo.getInstance().lastTimePlayed.addElement(handler.player.getUuid(), System.currentTimeMillis() / 1000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (Utils.getOnlinePlayersSize() - 1 == 1) {
            Pipo.JDA.getPresence().setActivity(Activity.watching("1 player"));
        } else {
            Pipo.JDA.getPresence().setActivity(Activity.watching((Utils.getOnlinePlayersSize() - 1) + " players"));
        }
    }
}
