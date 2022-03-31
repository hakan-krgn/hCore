package com.hakan.core.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class HYaml {

    /**
     * Creates file and include resource to in it.
     *
     * @param file         File
     * @param resourceName resource from resources
     * @return HYaml class
     */
    public static HYaml create(JavaPlugin plugin, File file, String resourceName) {
        try {
            if (!file.exists()) {
                HYaml.createFile(file.getPath());

                InputStream inputStream = plugin.getClass().getResourceAsStream("/" + resourceName);
                if (inputStream != null) FileUtils.copyInputStreamToFile(inputStream, file);
            }

            return new HYaml(file);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Creates file and include resource to in it.
     *
     * @param fileURL      File url
     * @param resourceName resource from resources
     * @return HYaml class
     */
    public static HYaml create(JavaPlugin plugin, String fileURL, String resourceName) {
        return HYaml.create(plugin, new File(plugin.getDataFolder() + "/" + fileURL), resourceName);
    }

    /**
     * Creates file.
     *
     * @param file File
     * @return HYaml class
     */
    public static HYaml create(File file) {
        HYaml.createFile(file.getPath());
        return new HYaml(file);
    }

    /**
     * Creates file.
     *
     * @param fileURL File url
     * @return HYaml class
     */
    public static HYaml create(String fileURL) {
        return HYaml.create(new File(fileURL));
    }

    public static File createFile(String url) {
        if (new File(url).exists()) return new File(url);
        url = url.replace("/", "\\");
        String[] sp = url.split(Pattern.quote("\\"));
        String folder = url.substring(0, url.length() - sp[sp.length - 1].length());

        try {
            new File(folder.replace("\\", "/")).mkdirs();
            new File(url.replace("\\", "/")).createNewFile();
            return new File(url.replace("\\", "/"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }


    private final File file;
    private final FileConfiguration fileConfiguration;

    /**
     * Create instance of this class
     *
     * @param file file
     */
    public HYaml(@Nonnull File file) {
        this.file = file;
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Gets file
     *
     * @return file
     */
    @Nonnull
    public File getFile() {
        return this.file;
    }

    /**
     * Gets file configuration
     *
     * @return file configuration
     */
    @Nonnull
    public FileConfiguration getFileConfiguration() {
        return this.fileConfiguration;
    }

    /**
     * Saves yml
     */
    public void save() {
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reloads yml from cache
     */
    public void reload() {
        try {
            this.fileConfiguration.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes yml.
     *
     * @return If yml removes successfully, returns true
     */
    public boolean delete() {
        return this.file.delete();
    }

    public Object get(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.get(path);
    }

    public Object get(@Nonnull String path, @Nonnull Object def) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.get(path, def);
    }

    public void set(@Nonnull String path, @Nullable Object value) {
        Validate.notNull(path, "path cannot be null!");
        this.fileConfiguration.set(path, value);
    }

    public boolean isSet(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.isSet(path);
    }

    public String getString(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getString(path);
    }

    public String getString(@Nonnull String path, @Nullable String def) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getString(path, def);
    }

    public boolean isString(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.isString(path);
    }

    public int getInt(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getInt(path);
    }

    public int getInt(@Nonnull String path, int def) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getInt(path, def);
    }

    public boolean isInt(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.isInt(path);
    }

    public boolean getBoolean(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getBoolean(path);
    }

    public boolean getBoolean(@Nonnull String path, boolean def) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getBoolean(path, def);
    }

    public boolean isBoolean(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.isBoolean(path);
    }

    public double getDouble(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getDouble(path);
    }

    public double getDouble(@Nonnull String path, double def) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getDouble(path, def);
    }

    public boolean isDouble(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.isDouble(path);
    }

    public long getLong(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getLong(path);
    }

    public long getLong(@Nonnull String path, long def) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getLong(path, def);
    }

    public boolean isLong(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.isLong(path);
    }

    public List<?> getList(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getList(path);
    }

    public List<?> getList(@Nonnull String path, @Nullable List<?> def) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getList(path, def);
    }

    public boolean isList(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.isList(path);
    }

    public List<String> getStringList(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getStringList(path);
    }

    public List<Integer> getIntegerList(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getIntegerList(path);
    }

    public List<Boolean> getBooleanList(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getBooleanList(path);
    }

    public List<Double> getDoubleList(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getDoubleList(path);
    }

    public List<Float> getFloatList(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getFloatList(path);
    }

    public List<Long> getLongList(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getLongList(path);
    }

    public List<Byte> getByteList(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getByteList(path);
    }

    public List<Character> getCharacterList(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getCharacterList(path);
    }

    public List<Short> getShortList(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getShortList(path);
    }

    public List<Map<?, ?>> getMapList(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getMapList(path);
    }

    public OfflinePlayer getOfflinePlayer(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getOfflinePlayer(path);
    }

    public OfflinePlayer getOfflinePlayer(@Nonnull String path, @Nullable OfflinePlayer def) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getOfflinePlayer(path, def);
    }

    public boolean isOfflinePlayer(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.isOfflinePlayer(path);
    }

    public ConfigurationSection getConfigurationSection(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.getConfigurationSection(path);
    }

    public boolean isConfigurationSection(@Nonnull String path) {
        Validate.notNull(path, "path cannot be null!");
        return this.fileConfiguration.isConfigurationSection(path);
    }
}