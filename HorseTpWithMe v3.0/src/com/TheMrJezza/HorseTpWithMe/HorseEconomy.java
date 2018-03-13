package com.TheMrJezza.HorseTpWithMe;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class HorseEconomy {
	private Economy econ = null;
	private Main instance = Main.getInstance();

	public HorseEconomy() {
		if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
			return;
		}
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return;
		}
		econ = rsp.getProvider();
	}
	
	public String chargePlayer(Player player, double price) {
		if (econ == null) {
			instance.getLogger().info("Economy is Enabled, however there is an issue accessing Vault.");
			instance.getLogger().info("Is Vault installed?");
			return null;
		}
		if (econ.getBalance(player) < price) {
			return HTWM_Message.INSUFFICIENT_FUNDS.toString().replace("{FEE}", price + "");
		}
		EconomyResponse r = econ.withdrawPlayer(player, price);
		if (r.transactionSuccess()) {
			return HTWM_Message.ECONOMY_SUCCESS.toString().replace("{FEE}", price+"").replace("{BALANCE}", r.balance+"");
		} return r.errorMessage;
		
	}
}
