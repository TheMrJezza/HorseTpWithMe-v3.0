package com.TheMrJezza.HorseTpWithMe.Commands;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.TheMrJezza.HorseTpWithMe.HTWM_Message;
import com.TheMrJezza.HorseTpWithMe.Main;

public class AreaBlockCommands implements CommandExecutor {

	private Main instance = Main.getInstance();

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
		if (!(cs instanceof Player)) {
			cs.sendMessage("§cOnly In-Game Players can do that!");
			return true;
		}
		Player player = (Player) cs;
		if (!player.hasPermission("horsetpwithme.areablock.use")) {
			cs.sendMessage(HTWM_Message.NO_PERMISSION + "");
			return true;
		}
		if (args[0].equalsIgnoreCase("block")) {
			Set<String> results = new HashSet<>();
			for (String region : instance.external().getAllRegionsAtLocation(player.getLocation())) {
				for (String blockedRegion : instance.getSettings().getBlockedRegions()) {
					if (!blockedRegion.trim().equalsIgnoreCase(region.toLowerCase())) {
						results.add(region.toLowerCase().trim());
					}
				}
			}
			if (!results.isEmpty()) {
				player.sendMessage("Affected Regions: " + results.size());
				for (String name : results) {
					player.sendMessage("- " + name);
					instance.getSettings().toggleRegion(name);
				}
			}
			if (instance.external().isClaim(player.getLocation())) {
				if (!instance.getSettings().getBlockedClaims()
						.contains(instance.external().getClaimIDAt(player.getLocation()))) {
					player.sendMessage("This Claim has been blocked!");
					instance.getSettings().toggleClaim(instance.external().getClaimIDAt(player.getLocation()));
				}
			}
		} else if (args[0].equalsIgnoreCase("unblock")) {
			Set<String> results = new HashSet<>();
			for (String region : instance.external().getAllRegionsAtLocation(player.getLocation())) {
				for (String blockedRegion : instance.getSettings().getBlockedRegions()) {
					if (blockedRegion.trim().equalsIgnoreCase(region.toLowerCase())) {
						results.add(region.toLowerCase().trim());
					}
				}
			}
			if (!results.isEmpty()) {
				player.sendMessage("Affected Regions: " + results.size());
				for (String name : results) {
					player.sendMessage("- " + name);
					instance.getSettings().toggleRegion(name);
				}
			}
			if (instance.external().isClaim(player.getLocation())) {
				if (instance.getSettings().getBlockedClaims()
						.contains(instance.external().getClaimIDAt(player.getLocation()))) {
					player.sendMessage("This Claim has been unblocked!");
					instance.getSettings().toggleClaim(instance.external().getClaimIDAt(player.getLocation()));
				}
			}

		} else if (args[0].equalsIgnoreCase("toggle")) {
			Set<String> results = new HashSet<>();
			for (String region : instance.external().getAllRegionsAtLocation(player.getLocation())) {
				results.add(region.toLowerCase().trim());
			}
			if (!results.isEmpty()) {
				player.sendMessage("Affected Regions: " + results.size());
				for (String name : results) {
					player.sendMessage("- " + name);
					instance.getSettings().toggleRegion(name);
				}
			}
			if (instance.external().isClaim(player.getLocation())) {
				player.sendMessage("This Claim's Blocked status has been toggled!");
				instance.getSettings().toggleClaim(instance.external().getClaimIDAt(player.getLocation()));
			}
		}
		return false;
	}
}