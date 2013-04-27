package com.me.tft_02.everlastingweather;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    EverlastingWeather plugin;

    public PlayerListener(EverlastingWeather instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (plugin.updateAvailable && player.hasPermission("everlastingweather.updatecheck")) {
            player.sendMessage(ChatColor.GOLD + "EverlastingWeather is outdated!");
            player.sendMessage(ChatColor.AQUA + "http://dev.bukkit.org/server-mods/EverlastingWeather/");
        }
    }
}
