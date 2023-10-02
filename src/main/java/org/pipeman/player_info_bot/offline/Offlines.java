package org.pipeman.player_info_bot.offline;

import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Offlines {

    public static UUID getUUIDbyName(String name) {
        File playerDataDirectory = new File("world/playerdata/");

        File[] playerDataFiles = playerDataDirectory.listFiles();

        if (playerDataFiles == null) return null;

        for (File playerDataFile : playerDataFiles) {
            String fileName = playerDataFile.getName();
            if (fileName.endsWith(".dat")) {
                UUID id = UUID.fromString(fileName.substring(0, fileName.length() - 4));

                try {
                    String jsonContent = new String(Files.readAllBytes(Paths.get(playerDataFile.getAbsolutePath())));
                    JSONObject playerData = new JSONObject(jsonContent);

                    String playerName = playerData.getString("name");

                    if (playerName.equals(name)) return id;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static List<String> getPlayerNames() {
        List<String> players = new ArrayList<>();
        File playerDataDirectory = new File("world/playerdata/");

        File[] playerDataFiles = playerDataDirectory.listFiles();

        if (playerDataFiles == null) return List.of();

        for (File playerDataFile : playerDataFiles) {
            String fileName = playerDataFile.getName();
            if (fileName.endsWith(".dat")) {
                try {
                    String jsonContent = new String(Files.readAllBytes(Paths.get(playerDataFile.getAbsolutePath())));
                    JSONObject playerData = new JSONObject(jsonContent);

                    String playerName = playerData.getString("name");

                    players.add(playerName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return players;
    }

    public static List<UUID> getPlayerUUIDs() {
        List<UUID> ids = new ArrayList<>();
        File playerDataDirectory = new File("world/playerdata/");

        File[] playerDataFiles = playerDataDirectory.listFiles();

        if (playerDataFiles == null) return List.of();

        for (File playerDataFile : playerDataFiles) {
            String fileName = playerDataFile.getName();
            if (fileName.endsWith(".dat")) {
                UUID id = UUID.fromString(fileName.substring(0, fileName.length() - 4));

                ids.add(id);
            }
        }
        return ids;
    }
}
