package me.hammale.perk;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PListener implements Listener {
	
	perk plugin;
	
	public PListener(perk perk) {
		this.plugin = perk;
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(plugin.lockedIds.contains(e.getItem().getTypeId())){
				if(!plugin.getPlayer(e.getPlayer()).ints.contains(e.getItem().getTypeId())){
					e.getPlayer().sendMessage(plugin.message);
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent e){
		plugin.players.add(new PPlayer(plugin, e.getPlayer()));
	}
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent e){
		PPlayer tp = plugin.getPlayer(e.getPlayer());
		plugin.players.remove(tp);
	}
}
