package com.TheMrJezza.HorseTpWithMe.External;

import org.bukkit.Location;

import com.ptibiscuit.igates.Plugin;

public class IGatesExternal {

	public boolean isWaterGate(Location loc) {
		return Plugin.instance.getPortalByPosition(loc, 3) != null;
	}
}