package me.hammale.perk;

import java.util.HashSet;

import org.bukkit.entity.Player;

import com.earth2me.essentials.api.*;

public class PPlayer {
	
	Player p;
	perk plugin;
	
	public HashSet<Integer> ints = new HashSet<Integer>();
	
	public PPlayer(perk plugin, Player p){
		this.p = p;
		this.plugin = plugin;	
	}
	
	public Player getPlayer(){
		return this.p;
	}

	public boolean charge(int amnt, int id) {
		try {
			if(!Economy.playerExists(p.getName())){
				return false;
			}
			if(!Economy.hasEnough(p.getName(), amnt)){
				return false;
			}
			Economy.subtract(p.getName(), amnt);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
