package org.pipeman.pipo.offline;

import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class OfflinesStats {

    public static long getPlayerStat(String stat, UUID player) {
        File playerDataDirectory = new File("world/stats/");

        File[] statDataFiles = playerDataDirectory.listFiles();

        if (statDataFiles == null) return 0;

        for (File playerDataFile : statDataFiles) {
            String fileName = playerDataFile.getName();
            if (fileName.endsWith(".json")) {
                UUID id = UUID.fromString(fileName.substring(0, fileName.length() - 5));

                if (!player.equals(id)) continue;

                try {
                    String jsonContent = new String(Files.readAllBytes(Paths.get(playerDataFile.getAbsolutePath())));
                    JSONObject statData = new JSONObject(jsonContent);

                    JSONObject customStats = statData.getJSONObject("stats").getJSONObject("minecraft:custom");

                    return customStats.optLong("minecraft:" + stat.toLowerCase(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

}
