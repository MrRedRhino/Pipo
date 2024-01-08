package org.pipeman.pipo.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class LastTimePlayed {
    private File file;
    private Properties properties;

    public LastTimePlayed(String filePath) throws IOException {
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


    public Map<UUID, Long> getHashMap() {
        Map<UUID, Long> hashMap = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            UUID uuid = UUID.fromString(key);
            long lastTimePlayed = Long.parseLong(value);
            hashMap.put(uuid, lastTimePlayed);
        }
        return hashMap;
    }

    public void setHashMap(Map<UUID, Long> hashMap) throws IOException {
        for (Map.Entry<UUID, Long> entry : hashMap.entrySet()) {
            properties.setProperty(entry.getKey().toString(), entry.getValue().toString());
        }
        properties.store(new FileOutputStream(file), null);
    }

    public void addElement(UUID playerID, long lastTimePlayed) throws IOException {
        Map<UUID, Long> hashMap = getHashMap();
        hashMap.put(playerID, lastTimePlayed);
        setHashMap(hashMap);
    }

    public long getElement(UUID playerID) {
        return getHashMap().containsKey(playerID) ? getHashMap().get(playerID) : 0;
    }
}

