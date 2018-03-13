package com.TheMrJezza.HorseTpWithMe.External;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.TheMrJezza.HorseTpWithMe.Main;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionExternal {
	
	private DataStore dStore;
	protected GriefPreventionExternal() {
		dStore = GriefPrevention.instance.dataStore;
	}

	public boolean areaBlocked(Location loc, Player player, boolean ignorePlayer) {
		if (!isClaim(loc)) return false;
		Claim claim = dStore.getClaimAt(loc, true, null);
		if (!ignorePlayer && claim.ownerID.equals(player.getUniqueId())) return false;
		return Main.getInstance().getSettings().getBlockedClaims().contains(claim.getID());
	}

	public boolean isClaim(Location loc) {
		return dStore.getClaimAt(loc, true, null) != null;
	}

	public long getClaimID(Location loc) {
		return dStore.getClaimAt(loc, true, null).getID();
	}
}