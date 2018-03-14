package com.TheMrJezza.HorseTpWithMe;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.TheMrJezza.HorseTpWithMe.Commands.AreaBlockCommands;
import com.TheMrJezza.HorseTpWithMe.External.External;

public class Main extends JavaPlugin {

	private static Main instance;
	private Settings settings;
	private HorseEconomy economy;
	private External external;

	public void onEnable() {
		instance = this;
		settings = new Settings();
		external = new External();
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		getCommand("areablock").setExecutor(new AreaBlockCommands());
	}

	public void onDisable() {
		getCommand("areablock").setExecutor(null);
		external = null;
		settings = null;
		instance = null;
	}

	protected Main() {
		// Not much here
	}
	
	public static Main getInstance() {
		return instance;
	}

	public Settings getSettings() {
		return settings;
	}
	
	public External external() {
		return external;
	}
	
	public HorseEconomy getEconomy() {
		return economy;
	}

	public void reloadFiles(Player player) {
		settings = new Settings();
		economy = new HorseEconomy();
		if (player != null) {
			getLogger().info(player.getName() + " has reloaded all files!");
			player.sendMessage("§7[§2HTpWM§7] §aFiles Reloaded!");
			return;
		}
		getLogger().info("All files have been reloaded!");
	}

	public void reloadFiles() {
		reloadFiles(null);
	}
}