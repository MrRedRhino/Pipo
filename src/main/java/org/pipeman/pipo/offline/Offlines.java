package org.pipeman.pipo.offline;

import com.mojang.authlib.GameProfile;
import net.minecraft.util.UserCache;
import org.pipeman.pipo.MinecraftServerSupplier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Offlines {

    public static UUID getUUIDbyName(String name) {
        UserCache userCache = MinecraftServerSupplier.getServer().getUserCache();
        if (userCache == null) return null;
        Optional<GameProfile> gameProfile = userCache.findByName(name);
        return gameProfile.map(GameProfile::getId).orElse(null);
    }

    public static String getNameByUUID(UUID id) {
        UserCache userCache = MinecraftServerSupplier.getServer().getUserCache();
        if (userCache == null) return "";
        Optional<GameProfile> gameProfile = userCache.getByUuid(id);
        return gameProfile.map(GameProfile::getName).orElse("");
    }

    public static List<String> getPlayerNames() {
        List<String> players = new ArrayList<>();
        File playerDataDirectory = new File("world/playerdata/");

        File[] playerDataFiles = playerDataDirectory.listFiles();

        if (playerDataFiles == null) return List.of();

        for (File playerDataFile : playerDataFiles) {
            String fileName = playerDataFile.getName();
            if (!fileName.endsWith(".dat")) continue;
            UUID id = UUID.fromString(fileName.substring(0, fileName.length() - 4));
            players.add(getNameByUUID(id));
        }
        return players;
    }
}
