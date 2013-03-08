package com.me.tft_02.everlastingweather;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    EverlastingWeather plugin;

    public Commands(EverlastingWeather instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("eweather")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("update")) {
                    return updateWeather(sender);
                }
                else if (args[0].equalsIgnoreCase("info")) {
                    return info(sender, args);

                }
                else if (args[0].equalsIgnoreCase("set")) {
                    return setWeather(sender, args);

                }
                else if (args[0].equalsIgnoreCase("reset")) {
                    return resetWeather(sender, args);

                }
                else if (args[0].equalsIgnoreCase("reload")) {
                    return reloadConfiguration(sender);
                }
            }
            else {
                return printUsage(sender);
            }
        }
        return false;
    }

    private boolean printUsage(CommandSender sender) {
        sender.sendMessage("Usage: /eweather [update | info | set | reset | reload]");
        return false;
    }

    private boolean updateWeather(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Updated weather.");
        plugin.updateWeather();
        return true;
    }

    private boolean info(CommandSender sender, String[] args) {
        World world = null;
        switch (args.length) {
            case 2:
                world = Bukkit.getWorld(args[1]);
            default:
                if (sender instanceof Player) {
                    sender.sendMessage("sender = player");
                    world = ((Player) sender).getWorld();
                }
        }
        if (world == null) {
            sender.sendMessage(ChatColor.RED + "Invalid World.");
            sender.sendMessage(ChatColor.RED + "Usage: /eweather info [World]");
            return false;
        }
        String worldName = world.getName().toLowerCase();
        boolean sunny = plugin.getConfig().getBoolean(worldName + ".Always_Sunny");
        boolean rainy = plugin.getConfig().getBoolean(worldName + ".Always_Rainy");
        boolean thundering = plugin.getConfig().getBoolean(worldName + ".Always_Thundering");

        sender.sendMessage(ChatColor.GOLD + "-----[ " + ChatColor.DARK_RED + "EverlastingWeather Info" + ChatColor.GOLD + " ]-----");
        sender.sendMessage(ChatColor.DARK_AQUA + world.getName() + ChatColor.GRAY + ":");
        sender.sendMessage(ChatColor.GRAY + "  Always Sunny:      " + ChatColor.DARK_AQUA + sunny);
        sender.sendMessage(ChatColor.GRAY + "  Always Raining:    " + ChatColor.DARK_AQUA + rainy);
        sender.sendMessage(ChatColor.GRAY + "  Always Thundering: " + ChatColor.DARK_AQUA + thundering);
        return true;

    }

    private boolean setWeather(CommandSender sender, String[] args) {
        World world = null;
        switch (args.length) {
            case 4:
                world = Bukkit.getWorld(args[1]);
                String status = "false";

                if (world == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid World.");
                    return false;
                }

                if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("enabled") || args[2].equalsIgnoreCase("on")) {
                    status = "true";
                }
                else if (args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("disabled") || args[2].equalsIgnoreCase("off")) {
                    status = "true";
                }
                else {
                    sender.sendMessage("Usage: /eweather set [World] [Clear | Rain | Thunder] [true | false]");
                    return false;
                }

                String worldName = world.getName().toLowerCase();

                if (args[2].equalsIgnoreCase("clear") || args[2].equalsIgnoreCase("sun") || args[2].equalsIgnoreCase("sunny")) {
                    plugin.getConfig().set(worldName + ".Always_Sunny", status);
                    sender.sendMessage(ChatColor.GREEN + "Always_Sunny has been set to " + ChatColor.DARK_AQUA + status + ChatColor.GREEN + " for world: " + ChatColor.DARK_AQUA + worldName);
                }
                else if (args[2].equalsIgnoreCase("rain") || args[2].equalsIgnoreCase("rainy")) {
                    plugin.getConfig().set(worldName + ".Always_Rainy", status);
                    sender.sendMessage(ChatColor.GREEN + "Always_Rainy has been set to " + ChatColor.DARK_AQUA + status + ChatColor.GREEN + " for world: " + ChatColor.DARK_AQUA + worldName);

                }
                else if (args[2].equalsIgnoreCase("thunder") || args[2].equalsIgnoreCase("thundering")) {
                    plugin.getConfig().set(worldName + ".Always_Thundering", status);
                    sender.sendMessage(ChatColor.GREEN + "Always_Thundering has been set to " + ChatColor.DARK_AQUA + status + ChatColor.GREEN + " for world: " + ChatColor.DARK_AQUA + worldName);

                }
                else {
                    sender.sendMessage("Usage: /eweather set [World] [Clear | Rain | Thunder] [true | false]");
                    return false;
                }
                plugin.updateWeather();
                return true;

            default:
                sender.sendMessage("Usage: /eweather set [World] [Clear | Rain | Thunder] [true | false]");
                return false;
        }
    }

    private boolean resetWeather(CommandSender sender, String[] args) {
        World world = null;
        switch (args.length) {
            case 2:
                world = Bukkit.getWorld(args[1]);
            default:
                if (sender instanceof Player) {
                    world = ((Player) sender).getWorld();
                }
        }

        if (world == null) {
            sender.sendMessage(ChatColor.RED + "Invalid World.");
            return false;
        }

        String worldName = world.getName().toLowerCase();
        plugin.getConfig().set(worldName + ".Always_Sunny", "false");
        plugin.getConfig().set(worldName + ".Always_Rainy", "false");
        plugin.getConfig().set(worldName + ".Always_Thundering", "false");
        sender.sendMessage(ChatColor.GREEN + "Weather settings for " + ChatColor.DARK_AQUA + worldName + ChatColor.GREEN + " have been reset.");
        return true;
    }

    private boolean reloadConfiguration(CommandSender sender) {
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
        return false;
    }
}
