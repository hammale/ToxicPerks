package me.hammale.perk;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class perk extends JavaPlugin {
	
	public FileConfiguration config;
		
	@Override
	public void onEnable(){
		System.out.println("[ToxicPerks] Enabled!");
		handleConfig();
		System.out.println(configNum());
		PluginManager pm = getServer().getPluginManager();
//		pm.registerEvents(new TListener(this), this);
//		for(Player p : getServer().getOnlinePlayers()){
//			players.add(new TPlayer(this, p));
//		}
	}
	
	@Override
	public void onDisable(){
		System.out.println("[ToxicPerks] Disabled!");
	}
	
	public String Colorize(String s) {
	    if (s == null) return null;
	    return s.replaceAll("&([0-9a-f])", "§$1");
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
			config.addDefault("Perks.1.id", 368);
			config.addDefault("Perks.1.price", 25000);
			config.options().copyDefaults(true);
			saveConfig();
		}
		readConfig();
	}
	
	public void readConfig() {
		config = getConfig();
		//TagMessage = config.getString("TagMessage");
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
	
//	public TPlayer getPlayer(Player p){
//		for(TPlayer tp : players){
//			if(tp.getPlayer().getName().equalsIgnoreCase(p.getName())){
//				return tp;
//			}
//		}
//		return null;
//	}
	
}
