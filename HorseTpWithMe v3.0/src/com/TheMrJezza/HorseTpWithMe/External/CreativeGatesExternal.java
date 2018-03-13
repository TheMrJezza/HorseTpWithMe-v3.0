package com.TheMrJezza.HorseTpWithMe.External;

import org.bukkit.Location;

import com.massivecraft.creativegates.EngineMain;
import com.massivecraft.creativegates.entity.UConfColls;
import com.massivecraft.massivecore.MassiveCore;

public class CreativeGatesExternal {
	protected CreativeGatesExternal() {/*Other Plugins shouldn't directly access this class.*/}

	public boolean isWaterGate(Location loc) {
		if (!UConfColls.get().getForWorld(loc.getWorld().getName()).get(MassiveCore.INSTANCE).isUsingWater())
			return false;
		return EngineMain.isGateNearby(loc.getBlock());
	}
}