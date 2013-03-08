package com.me.tft_02.everlastingweather;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldListener implements Listener {
    EverlastingWeather plugin;

    public WorldListener(EverlastingWeather instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onWeatherChange (WeatherChangeEvent event) {
        World world = event.getWorld();
        String worldName = world.getName().toLowerCase();

        boolean changeToRain = event.toWeatherState();

        boolean sunny = plugin.getConfig().getBoolean(worldName + ".Always_Sunny");
        boolean rainy = plugin.getConfig().getBoolean(worldName + ".Always_Rainy");

        if (changeToRain && sunny) {
            event.setCancelled(true);
        }
        else if (!changeToRain && rainy) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onThunderChange (ThunderChangeEvent event) {
        World world = event.getWorld();
        String worldName = world.getName().toLowerCase();
        boolean changeToThunder = event.toThunderState();

        boolean thundering = plugin.getConfig().getBoolean(worldName + ".Always_Thundering");
        if (changeToThunder && !thundering) {
            event.setCancelled(true);
        }
    }
}
