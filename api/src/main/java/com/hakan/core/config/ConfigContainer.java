package com.hakan.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.function.Supplier;

public class ConfigContainer<T> {

    final File file;
    T config;

    public ConfigContainer(File f) {
        this.file = f;
    }

    public static <T> ConfigContainer<T> of(File f, Class<T> type, Supplier<T> defaultConfig) {
        Gson GSON = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        ConfigContainer<T> container = new ConfigContainer<>(f);
        if(!f.getParentFile().exists()) f.getParentFile().mkdirs();

        if(f.exists()) {
            try {
                container.setConfig(GSON.fromJson(new FileReader(f), type));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            container.setConfig(defaultConfig.get());
            container.save();
        }

        ConfigHandler.getConfigs().add(container);

        return container;
    }

    public void save() {
        Gson GSON = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(GSON.toJson(config));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setConfig(T config) {
        this.config = config;
    }

    public T getConfig() {
        return config;
    }

    public File getFile() {
        return file;
    }

}