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
        if (!file.exists()) {
            file.createNewFile();
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

    public void deleteElement(UUID playerID) throws IOException {
        Map<UUID, Long> hashMap = getHashMap();
        hashMap.remove(playerID);
        setHashMap(hashMap);
    }
}

