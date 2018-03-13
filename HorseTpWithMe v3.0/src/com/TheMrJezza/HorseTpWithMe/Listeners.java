package com.TheMrJezza.HorseTpWithMe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.TheMrJezza.HorseTpWithMe.Events.AnimalTeleportEvent;

public class Listeners implements Listener {

	private HashMap<Player, AbstractHorse> map = new HashMap<Player, AbstractHorse>();
	private HashSet<UUID> uuidSet = new HashSet<UUID>();

	@EventHandler(priority = EventPriority.MONITOR)
	public void onVehicleEnter(VehicleEnterEvent evt) {
		if (evt.isCancelled() && evt.getEntered() instanceof Player) {
			uuidSet.add(evt.getEntered().getUniqueId());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onVehicleExit(VehicleExitEvent evt) {
		if (!(evt.getExited() instanceof Player))
			return;
		Player player = (Player) evt.getExited();
		if (uuidSet.contains(player.getUniqueId())) {
			uuidSet.remove(player.getUniqueId());
			return;
		}
		if (player.isSneaking()) {
			uuidSet.add(player.getUniqueId());
			return;
		}
		if (!(evt.getVehicle() instanceof AbstractHorse)) {
			uuidSet.add(player.getUniqueId());
			return;
		}
		AbstractHorse coreHorse = (AbstractHorse) evt.getVehicle();
		if (!coreHorse.isOnGround()) {
			Material type = coreHorse.getLocation().getBlock().getType();
			if (type == Material.WATER || type == Material.STATIONARY_WATER) {
				if (!Main.getInstance().external().isWaterGate(coreHorse.getLocation())) {
					uuidSet.add(player.getUniqueId());
					return;
				}
			}
		}
		if (!coreHorse.isTamed()) {
			uuidSet.add(player.getUniqueId());
			return;
		}
		if (Main.getInstance().getSettings().isRequiringSaddles()) {
			if (!hasSaddle(coreHorse) && !player.hasPermission("horsetpwithme.nosaddle")) {
				uuidSet.add(player.getUniqueId());
				return;
			}
		}
		if (Main.getInstance().getSettings().isUsingPermissions()) {
			if (coreHorse.getType().name().toLowerCase().contains("horse")) {
				if (!player.hasPermission("horsetpwithme.horse")) {
					uuidSet.add(player.getUniqueId());
					return;
				}
				// Yes, I have thought about using a loop here...
			} else if (coreHorse.getType().name().toLowerCase().equals("pig")) {
				if (!player.hasPermission("horsetpwithme.pig")) {
					uuidSet.add(player.getUniqueId());
					return;
				}
			} else if (coreHorse.getType().name().toLowerCase().equals("llama")) {
				if (!player.hasPermission("horsetpwithme.llama")) {
					uuidSet.add(player.getUniqueId());
					return;
				}
			} else if (coreHorse.getType().name().toLowerCase().equals("donkey")) {
				if (!player.hasPermission("horsetpwithme.donkey")) {
					uuidSet.add(player.getUniqueId());
					return;
				}
			} else if (coreHorse.getType().name().toLowerCase().equals("mule")) {
				if (!player.hasPermission("horsetpwithme.donkey")) {
					uuidSet.add(player.getUniqueId());
					return;
				}
			}
		}
		map.put(player, coreHorse);
	}

	protected Listeners() {
		// Not much here
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerTeleport(PlayerTeleportEvent evt) {
		Player player = evt.getPlayer();
		if (uuidSet.contains(player.getUniqueId())) {
			uuidSet.remove(player.getUniqueId());
			return;
		}
		List<LivingEntity> entities = new ArrayList<>();
		boolean onVehicle = false;
		// Regular Teleport
		if (map.containsKey(player)) {
			final AbstractHorse horse = map.get(player);
			entities.add(horse);
			map.remove(player);
			onVehicle = true;
		}

		// Leash Teleport
		for (LivingEntity e : getLeashedEntities(player)) {
			entities.add(e);
		}

		final boolean sru = onVehicle;
		new BukkitRunnable() {
			@Override
			public void run() {
				AnimalTeleportEvent event = new AnimalTeleportEvent(entities, player, player.getLocation(),
						evt.getFrom(), sru);
				Bukkit.getPluginManager().callEvent(event);
				if (event.isCancelled())

					return;
				if (!player.hasPermission("horsetpwithme.ignoreblockedworlds")) {
					if (Main.getInstance().getSettings().isWorldBlocked(event.getTo().getWorld())) {
						player.sendMessage(HTWM_Message.BLOCKED_WORLD.toString());
						return;
					}
				}
				if (!player.hasPermission("horsetpwithme.economy.free")) {
					String error = Main.getInstance().getEconomy().chargePlayer(player);
					if (error != null && error != "") {
						player.sendMessage(error);
						return;
					}
				}

				if (Main.getInstance().getSettings().isClearingChests()) {
					if (!player.hasPermission("horsetpwithme.dontclearchest")) {
						if (Main.getInstance().getSettings().isWorldBlocked(event.getFrom().getWorld())) {
							for (int i = 0; i < event.getEntities().size(); i++) {
								clearChest(event.getEntities().get(i));
							}
						}
					}
				}
				
				bulkTeleport(event.getEntities(), event.getTo());
				if (event.playerInVehicle()) {
					event.getEntities().get(0).addPassenger(event.getPlayer());
				}
				setLeadHolder(event.playerInVehicle(), event.getEntities(), evt.getPlayer());
			}
		}.runTaskLater(Main.getInstance(), 5l);
	}

	private boolean hasSaddle(AbstractHorse entity) {
		if (entity instanceof Llama)
			return true;
		ItemStack slot = entity.getInventory().getItem(0);
		return !(slot == null || slot.getType() == Material.AIR);
	}

	private void bulkTeleport(List<LivingEntity> entities, Location loc) {
		for (int i = 0; i < entities.size(); i++) {
			LivingEntity e = entities.get(i);
			e.teleport(loc);
			e.setFallDistance(-999999999);
		}
	}

	private void setLeadHolder(boolean vehicle, List<LivingEntity> entities, Player player) {
		for (int i = vehicle ? 1 : 0; i < entities.size(); i++) {
			LivingEntity e = entities.get(i);
			e.setLeashHolder(player);
		}
	}

	private Set<LivingEntity> getLeashedEntities(Player player) {
		Set<LivingEntity> set = new HashSet<>();
		for (Entity entity : player.getNearbyEntities(12, 12, 12)) {
			if (!(entity instanceof LivingEntity))
				continue;
			LivingEntity le = (LivingEntity) entity;
			if (!le.isLeashed())
				continue;
			if (le.getLeashHolder() == null)
				continue;
			if (!(le.getLeashHolder() instanceof Player))
				continue;
			if (!((Player) le.getLeashHolder()).equals(player))
				continue;
			set.add(le);
		}
		return set;
	}

	public void clearChest(LivingEntity entity) {
		if (entity instanceof ChestedHorse) {
			ChestedHorse chested = (ChestedHorse) entity;
			if (chested.isCarryingChest()) {
				for (int i = entity instanceof Llama ? 2:1; i < chested.getInventory().getSize(); i++) {
					ItemStack item = chested.getInventory().getItem(i);
					if (item == null)
						continue;
					item.setType(Material.AIR);
					item.setAmount(1);
				}
			}
		}
	}
}