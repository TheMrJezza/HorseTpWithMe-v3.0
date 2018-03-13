package com.TheMrJezza.HorseTpWithMe.Events;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AnimalTeleportEvent extends Event implements Cancellable {
	private boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();
	private List<LivingEntity> leashEntities;
	private Player player;
	private Location from;
	private Location to;
	private boolean inVehicle;
	
	public AnimalTeleportEvent(List<LivingEntity> entities, Player player, Location from, Location to, boolean inVehicle) {
		this.leashEntities = entities;
		this.player = player;
		this.from = from;
		this.to = to;
		this.inVehicle = inVehicle;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean bool) {
		cancelled = bool;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public List<LivingEntity> getEntities() {
		return leashEntities;
	}

	public Player getPlayer() {
		return player;
	}

	public Location getFrom() {
		return from;
	}

	public Location getTo() {
		return to;
	}

	public boolean playerInVehicle() {
		return inVehicle;
	}
}