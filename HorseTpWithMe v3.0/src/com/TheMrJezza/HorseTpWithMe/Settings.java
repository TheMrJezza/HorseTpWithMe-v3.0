package com.TheMrJezza.HorseTpWithMe;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;

public class Settings {

	private Main instance = Main.getInstance();

	// General Configuration
	private File settingsFile = new File(instance.getDataFolder(), "Settings.yml");
	private YamlConfiguration settings;
	private boolean usePermissions;
	private boolean requireSaddle;
	private boolean leashTeleportation = true;
	private boolean clearChests;
	private List<String> disabledWorlds;
	private int maxOnLead = 5;

	// AreaBlock
	private File areaBlockFile = new File(instance.getDataFolder(), "AreaBlock");
	private YamlConfiguration areaBlock;

	// Messages
	private File messagesFile = new File(instance.getDataFolder(), "Messages.yml");
	private YamlConfiguration messages;
	protected String blockedWorldMessage = "§4This world is blocked!";
	protected String noPermissionMessage = "§4You do not have permission to do that!";
	protected String economySuccessMessage = "§7Animal Teleportation Fee§8: §a{FEE}\n§7Remaining Balance§8: §a{BALANCE}";
	protected String economyInsufficientFundsMessage = "§4You cannot afford to teleport any animals.\n§7Required§8: §a{FEE}";
	protected String tooManyOnLeadMessage = "§4You had too many leashed mobs/animals to teleport.\n§7obLimit§8: §a{LIMIT}";
	protected String areaIsBlocked = "§4This claim/region has animal teleportation blocked.";

	// Economy
	private File economyFile = new File(instance.getDataFolder(), "Economy.yml");
	private YamlConfiguration economy;
	private boolean useEconomy;
	private double globalFee = 0.00;
	private Set<String> permissions;

	protected Settings() {
		if (!settingsFile.exists())
			instance.saveResource("Settings.yml", false);
		if (!messagesFile.exists())
			instance.saveResource("Messages.yml", false);
		if (!economyFile.exists())
			instance.saveResource("Economy.yml", false);
		if (!areaBlockFile.exists())
			instance.saveResource("AreaBlock.yml", false);
		settings = YamlConfiguration.loadConfiguration(settingsFile);
		messages = YamlConfiguration.loadConfiguration(messagesFile);
		economy = YamlConfiguration.loadConfiguration(economyFile);
		areaBlock = YamlConfiguration.loadConfiguration(areaBlockFile);

		// Settings
		usePermissions = settings.getBoolean("UsePermissions");
		requireSaddle = settings.getBoolean("RequireSaddle");
		if (settings.get("LeashTeleportation") != null) {
			leashTeleportation = settings.getBoolean("LeashTeleportation");
		}
		if (settings.get("ClearChests") != null) {
			requireSaddle = settings.getBoolean("ClearChests");
		}
		disabledWorlds = settings.getStringList("DisabledWorlds");
		maxOnLead = settings.getInt("MaxOnLead", maxOnLead);

		// Messages
		blockedWorldMessage = ChatColor.translateAlternateColorCodes('&',
				messages.getString("BlockedWorldMessage", blockedWorldMessage));
		noPermissionMessage = ChatColor.translateAlternateColorCodes('&',
				messages.getString("NoPermissionMessage", noPermissionMessage));
		economySuccessMessage = ChatColor.translateAlternateColorCodes('&',
				messages.getString("EconomyTeleportSuccess", economySuccessMessage));
		economyInsufficientFundsMessage = ChatColor.translateAlternateColorCodes('&',
				messages.getString("EconomyInsufficientFunds", economyInsufficientFundsMessage));
		tooManyOnLeadMessage = ChatColor.translateAlternateColorCodes('&',
				messages.getString("TooManyOnLead", tooManyOnLeadMessage.replace("{LIMIT}", maxOnLead + "")));
		areaIsBlocked = ChatColor.translateAlternateColorCodes('&', messages.getString("BlockedArea", areaIsBlocked));

		// Economy
		useEconomy = economy.getBoolean("UseEconomy");
		if (useEconomy) {
			globalFee = economy.getDouble("GlobalTeleportFee", globalFee);
			Set<String> results = new HashSet<>();
			for (String str : economy.getKeys(true)) {
				if (!str.trim().startsWith("PermissionFees"))
					continue;
				if (str.trim().equals("PermissionFees") || str.equals("") || str == null)
					continue;
				results.add("horsetpwithme.economy." + str.replaceFirst("PermissionFees.", ""));
				instance.getServer().getPluginManager().addPermission(
						new Permission("horsetpwithme.economy." + str.replaceFirst("PermissionFees.", "")));
			}
			permissions = results;
		}
	}

	public boolean isUsingPermissions() {
		return usePermissions;
	}

	public boolean isUsingEconomy() {
		return useEconomy;
	}

	public boolean isRequiringSaddles() {
		return requireSaddle;
	}

	public boolean hasLeashTeleportationEnabled() {
		return leashTeleportation;
	}

	public boolean isClearingChests() {
		return clearChests;
	}

	public List<String> getDisabledWorlds() {
		return disabledWorlds;
	}

	public boolean isWorldBlocked(World world) {
		return isWorldBlocked(world.getName());
	}

	public boolean isWorldBlocked(String world) {
		return getDisabledWorlds().contains(world.trim());
	}

	public Set<String> getEconomyPermissions() {
		return permissions;
	}

	public double getGlobalFee() {
		return globalFee;
	}

	public double getFeeFromPermission(String perm) {
		return economy.getDouble("PermissionFees." + perm.replaceFirst("horsetpwithme.economy.", ""), 0.00);
	}
	
	public List<String> getBlockedRegions() {
		return areaBlock.getStringList("BlockedRegions");
	}
	public List<Long> getBlockedClaims() {
		return areaBlock.getLongList("BlockedClaims");
	}
	
	public boolean toggleClaim(long ID) {
		boolean result = false;
		List<Long> claims = areaBlock.getLongList("BlockedClaims");
		if (claims.contains(ID)) {
			claims.remove(ID);
			areaBlock.set("BlockedClaims", claims);
		} else {
			claims.add(ID);
			areaBlock.set("BlockedClaims", claims);
			result = true;
		}
		try {
			areaBlock.save(areaBlockFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean toggleRegion(String ID) {
		boolean result = false;
		List<String> claims = areaBlock.getStringList("BlockedRegions");
		if (claims.contains(ID)) {
			claims.remove(ID);
			areaBlock.set("BlockedRegions", claims);
		} else {
			claims.add(ID);
			areaBlock.set("BlockedRegions", claims);
			result = true;
		}
		try {
			areaBlock.save(areaBlockFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getLeashLimit() {
		return maxOnLead;
	}
}