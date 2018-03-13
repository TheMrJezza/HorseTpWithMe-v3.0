package com.TheMrJezza.HorseTpWithMe.External;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.TheMrJezza.HorseTpWithMe.Main;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardExternal {
	protected WorldGuardExternal() {/*Other Plugins shouldn't directly access this class.*/}

	public boolean areaBlocked(Location loc, Player player, boolean ignorePlayer) {
		if (!ignorePlayer && player.hasPermission("horsetpwithme.areablock.override")) return false;
		for (String string : getRegions(loc)) {
			if (contains(string)) return true;
		}
		return false;
	}

	public List<String> getRegions(Location loc) {
		List<String> result = new ArrayList<>();
		for (ProtectedRegion region : getRegionManagers(loc).getApplicableRegions(loc)) {
			result.add(region.getId().trim());
		}
		return result;
	}

	private RegionManager getRegionManagers(Location loc) {
		RegionContainer container = WGBukkit.getPlugin().getRegionContainer();
		return container.get(loc.getWorld());
	}
	
	private boolean contains(String str) {
		for (String string : Main.getInstance().getSettings().getBlockedRegions()) {
			if (str.trim().equalsIgnoreCase(string.trim())) {
				return true;
			}
		}
		return false;
	}
}