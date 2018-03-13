package com.TheMrJezza.HorseTpWithMe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {

	private HashMap<Player, AbstractHorse> map = new HashMap<Player, AbstractHorse>();
	private HashSet<UUID> uuidSet = new HashSet<UUID>();
	boolean enabled = false;

	@EventHandler(priority = EventPriority.MONITOR)
	public void onVehicleExit(VehicleExitEvent evt) {
		if (!(evt.getExited() instanceof Player))
			return;
		Player player = (Player) evt.getExited();
		if (uuidSet.contains(player.getUniqueId())) {
			uuidSet.remove(player.getUniqueId());
			return;
		}
		if (player.isSneaking()) return;
		if (!(evt.getVehicle() instanceof AbstractHorse)) return;
		AbstractHorse coreHorse = (AbstractHorse) evt.getVehicle();
		if (!coreHorse.isOnGround()) {
			Material type = coreHorse.getLocation().getBlock().getType();
			if (type == Material.WATER || type == Material.STATIONARY_WATER) {
				if (type != Material.AIR) {
					if (!Main.getInstance().external().isWaterGate(coreHorse.getLocation())) return;
				}
			}
		}
		if (!coreHorse.isTamed()) return;
		if (Main.getInstance().getSettings().isRequiringSaddles()) {
			if (!hasSaddle(coreHorse) && !player.hasPermission("horsetpwithme.nosaddle")) return;
		}
		if (Main.getInstance().getSettings().isUsingPermissions()) {
			if (coreHorse.getType().name().toLowerCase().contains("horse")) {
				if (!player.hasPermission("horsetpwithme.horse"))
					return;
			// Yes, I have thought about using a loop here...
			} else if (coreHorse.getType().name().toLowerCase().equals("pig")) {
				if (!player.hasPermission("horsetpwithme.pig"))
					return;
			} else if (coreHorse.getType().name().toLowerCase().equals("llama")) {
				if (!player.hasPermission("horsetpwithme.llama"))
					return;
			} else if (coreHorse.getType().name().toLowerCase().equals("donkey")) {
				if (!player.hasPermission("horsetpwithme.donkey"))
					return;
			} else if (coreHorse.getType().name().toLowerCase().equals("mule")) {
				if (!player.hasPermission("horsetpwithme.donkey"))
					return;
			}
		}
		map.put(player, coreHorse);
	}

	protected Listeners() {
		// Not much here
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent evt) {
		if (!map.containsKey(evt.getPlayer())) return;
		final Player player = evt.getPlayer();
		final AbstractHorse horse = map.get(player);
		Location loc = evt.getTo();
		int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
		String locString = x + " " + y + " " + z + " " + loc.getWorld().getName();
		player.sendMessage(locString + " ONE");
		
		String locString2 = player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ() + " " + player.getWorld().getName();
		player.sendMessage(locString2 + " TWO");
		
		player.sendMessage(evt.getCause().name());
	}

	public boolean hasSaddle(AbstractHorse entity) {
		if (entity instanceof Llama) return true;
		ItemStack slot = entity.getInventory().getItem(0);
		if (slot == null || slot.getType() == Material.AIR) return false;
		return true;
	}
}