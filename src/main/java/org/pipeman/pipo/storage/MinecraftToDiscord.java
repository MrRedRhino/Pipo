package org.pipeman.pipo.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class MinecraftToDiscord {
    private File file;
    private Properties properties;

    public MinecraftToDiscord(String filePath) throws IOException {
        this.file = new File(filePath);

        // Check if the parent directories exist, create them if not
        if (!file.getParentFile().exists()) {
            boolean directoriesCreated = file.getParentFile().mkdirs();
            if (!directoriesCreated) {
                throw new IOException("Failed to create parent directories for: " + filePath);
            }
        }

        // Check if the file exists, create it if not
        if (!file.exists()) {
            boolean fileCreated = file.createNewFile();
            if (!fileCreated) {
                throw new IOException("Failed to create file: " + filePath);
            }
        }

        this.properties = new Properties();
        this.properties.load(new FileInputStream(file));
    }

    public Map<UUID, String> getHashMap() {
        Map<UUID, String> map = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            UUID uuid = UUID.fromString(key);
            map.put(uuid, value);
        }
        return map;
    }

    public void setHashMap(Map<UUID, String> map) throws IOException {
        for (Map.Entry<UUID, String> entry : map.entrySet()) {
            properties.setProperty(entry.getKey().toString(), entry.getValue());
        }
        properties.store(new FileOutputStream(file), null);
    }

    public void addElement(UUID playerID, String memberID) throws IOException {
        Map<UUID, String> uuidStringMap = getHashMap();
        uuidStringMap.put(playerID, memberID);
        setHashMap(uuidStringMap);
    }

    public String getElement(UUID playerID) {
        return getHashMap().get(playerID);
    }
}
