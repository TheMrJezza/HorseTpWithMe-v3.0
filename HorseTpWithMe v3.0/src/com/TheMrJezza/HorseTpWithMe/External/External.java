package com.TheMrJezza.HorseTpWithMe.External;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class External {

	private GriefPreventionExternal griefPrevention;
	private WorldGuardExternal worldGuard;
	private CreativeGatesExternal creativeGates;
	private IGatesExternal iGates;
	private FBasicsExternal fBasics;
	private MyPetsExternal myPets;

	public External() {
		griefPrevention = enabled("GriefPrevention") ? new GriefPreventionExternal() : null;
		worldGuard = enabled("WorldGuard") ? new WorldGuardExternal() : null;
		creativeGates = enabled("CreativeGates") ? new CreativeGatesExternal() : null;
		iGates = enabled("IGates") ? new IGatesExternal() : null;
		fBasics = enabled("FBasics") ? new FBasicsExternal() : null;
		myPets = enabled("MyPets") ? new MyPetsExternal() : null;
	}
	
	public boolean isWaterGate(Location loc) {
		boolean result = false;
		if (creativeGates != null) {
			result = creativeGates.isWaterGate(loc);
		}
		if (iGates != null && !result) {
			result = iGates.isWaterGate(loc);
		}
		return result;
	}
	
	public boolean isAreaBlocked(Location loc, Player player) {
		return isAreaBlocked(loc, player, false);
	}
	
	public boolean isAreaBlocked(Location loc, Player player, boolean ignorePlayer) {
		boolean result = false;
		if (griefPrevention != null) {
			result = griefPrevention.areaBlocked(loc, player, ignorePlayer);
		}
		if (worldGuard != null && !result) {
			result = worldGuard.areaBlocked(loc, player, ignorePlayer);
		}
		return result;
 	}
	
	public List<String> getAllRegionsAtLocation(Location loc) {
		if (worldGuard == null) {
			return new ArrayList<String>();
		}
		return worldGuard.getRegions(loc);
	}
	
	private boolean enabled(String name) {
		Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
		return plugin != null && plugin.isEnabled();
	}

	public boolean isClaim(Location loc) {
		if (griefPrevention == null) return false;
		return griefPrevention.isClaim(loc);
	}
	
	public long getClaimIDAt(Location location) {
		if (griefPrevention == null);
		return griefPrevention.getClaimID(location);
	}
}