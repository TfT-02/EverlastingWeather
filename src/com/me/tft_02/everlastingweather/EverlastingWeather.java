package com.me.tft_02.everlastingweather;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EverlastingWeather extends JavaPlugin {

    private WorldListener worldListener = new WorldListener(this);

    // Update Check
    public boolean updateAvailable;

    /**
     * Run things on enable.
     */
    @Override
    public void onEnable() {
        setupConfiguration();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(worldListener, this);
        updateWeather();

        getCommand("eweather").setExecutor(new Commands(this));

        checkForUpdates();

        if (getConfig().getBoolean("General.stats_tracking_enabled")) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            }
            catch (IOException e) {
                System.out.println("Failed to submit stats.");
            }
        }
    }

    private void setupConfiguration() {
        final FileConfiguration config = this.getConfig();
        config.addDefault("General.stats_tracking_enabled", true);
        config.addDefault("General.update_check_enabled", true);
        for (World world : getServer().getWorlds()) {
            config.addDefault(world.getName().toLowerCase() + ".Always_Sunny", false);
            config.addDefault(world.getName().toLowerCase() + ".Always_Rainy", false);
            config.addDefault(world.getName().toLowerCase() + ".Always_Thundering", false);
        }
        config.options().copyDefaults(true);
        saveConfig();
    }

    /**
     * Run things on disable.
     */
    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    private void checkForUpdates() {
        if (getConfig().getBoolean("General.update_check_enabled")) {
            try {
                updateAvailable = UpdateChecker.updateAvailable();
            }
            catch (Exception e) {
                updateAvailable = false;
            }

            if (updateAvailable) {
                this.getLogger().log(Level.INFO, ChatColor.GOLD + "EverlastingWeather is outdated!");
                this.getLogger().log(Level.INFO, ChatColor.AQUA + "http://dev.bukkit.org/server-mods/EverlastingWeather/");
            }
        }
    }

    public void updateWeather() {
        for (World world : this.getServer().getWorlds()) {
            String worldName = world.getName().toLowerCase();

            boolean sunny = this.getConfig().getBoolean(worldName + ".Always_Sunny");
            boolean rainy = this.getConfig().getBoolean(worldName + ".Always_Rainy");
            boolean thundering = this.getConfig().getBoolean(worldName + ".Always_Thundering");

            if (sunny) {
                world.setStorm(false);
                world.setThundering(false);
            }
            else if (rainy) {
                world.setStorm(true);
                if (thundering)
                    world.setThundering(true);
            }
            world.setWeatherDuration(20 * 60 * 10);
        }
    }
}