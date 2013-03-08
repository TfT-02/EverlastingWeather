package com.me.tft_02.everlastingweather;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EverlastingWeather extends JavaPlugin {

    private WorldListener worldListener = new WorldListener(this);

    /**
     * Run things on enable.
     */
    @Override
    public void onEnable() {
        setupConfiguration();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(worldListener, this);
        worldListener.updateWeather();

        getCommand("eweather").setExecutor(new Commands(this));
    }

    private void setupConfiguration() {
        final FileConfiguration config = this.getConfig();
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
}