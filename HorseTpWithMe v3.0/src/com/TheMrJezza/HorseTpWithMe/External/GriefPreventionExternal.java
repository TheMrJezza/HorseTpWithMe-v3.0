package com.TheMrJezza.HorseTpWithMe.External;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionExternal {
	protected GriefPreventionExternal() {/*Other Plugins shouldn't directly access this class.*/}

	public boolean areaBlocked(Location loc, Player player, boolean ignorePlayer) {
		return false;
	}

	public boolean isClaim(Location loc) {
		if (GriefPrevention.instance.dataStore.getClaimAt(loc, true, null) != null) return true;
		return false;
	}

	public long getClaimID(Location location) {
		return GriefPrevention.instance.dataStore.getClaimAt(location, true, null).getID();
	}
}