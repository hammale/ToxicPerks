package me.hammale.perk;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class perk extends JavaPlugin {
	
	public FileConfiguration config;
	public ArrayList<PPlayer> players = new ArrayList<PPlayer>();
	public HashMap<String, Integer> convert = new HashMap<String, Integer>();
	public HashMap<Integer, Integer> locked = new HashMap<Integer, Integer>();
		
	@Override
	public void onEnable(){
		System.out.println("[ToxicPerks] Enabled!");
		handleConfig();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PListener(this), this);
		for(Player p : getServer().getOnlinePlayers()){
			players.add(new PPlayer(this, p));
		}
	}
	
	@Override
	public void onDisable(){
		System.out.println("[ToxicPerks] Disabled!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			if(cmd.getName().equalsIgnoreCase("buyperk") && args.length == 1){
				Player p = (Player) sender;
				if(convert.get(args[0]) != null){
					if(getPlayer(p).charge(locked.get(convert.get(args[0])),convert.get(args[0]))){
						p.sendMessage(ChatColor.GREEN + "You have bought " + args[0] + "!");
					}else{
						p.sendMessage(ChatColor.RED + "You don't have enough money!");
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("perks")){
				//TODO: display perks
			}
		}
		return true;
	}
	
	public int configNum(){
		for(int i=1;i<1000;i++){
			if(config.get("Perks." + i + ".id") == null){
				return i-1;
			}
		}
		return 1000;
	}
	
	public void handleConfig(){
		if (!exists()) {
			config = getConfig();
			config.options().copyDefaults(false);
			config.addDefault("Perks.1.name", 368);
			config.addDefault("Perks.1.id", 368);
			config.addDefault("Perks.1.price", 25000);
			config.options().copyDefaults(true);
			saveConfig();
		}
		readConfig();
	}
	
	public void readConfig() {
		config = getConfig();
		for(int i=1;i<=configNum();i++){
			locked.put(config.getInt("Perks." + i + ".id"), config.getInt("Perks." + i + ".price"));
			convert.put(config.getString("Perks." + i + ".name"), config.getInt("Perks." + i + ".id"));
		}
	}

	private boolean exists() {
		try {
			File file = new File("plugins/ToxicPerks/config.yml");
			return file.exists();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return true;
	}
	
	public PPlayer getPlayer(Player p){
		for(PPlayer tp : players){
			if(tp.getPlayer().getName().equalsIgnoreCase(p.getName())){
				return tp;
			}
		}
		return null;
	}
	
}
