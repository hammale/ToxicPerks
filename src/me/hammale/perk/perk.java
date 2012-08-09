package me.hammale.perk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class perk extends JavaPlugin {
	
	public FileConfiguration config;
	public String message;
	public ArrayList<PPlayer> players = new ArrayList<PPlayer>();
	public Set<Integer> lockedIds = new HashSet<Integer>();
	public HashMap<String, Integer> convert = new HashMap<String, Integer>();
	public HashMap<Integer, Integer> locked = new HashMap<Integer, Integer>();
	public HashMap<Integer, String> names = new HashMap<Integer, String>();
		
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

	public void saveChanges(Player np) {
		PPlayer p = getPlayer(np);
		if(p.ints.size() > 0){
			File f = new File("plugins/ToxicPerks/data/" + p.getPlayer().getName() + ".dat");
			if(f.exists()){			
				f.delete();
			}
			try {
				f.createNewFile();
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));			
				oos.writeObject(p.ints);
				oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("buyperk") && args.length == 1){
				if(convert.get(args[0]) != null){
					if(!getPlayer(p).ints.contains(convert.get(args[0])) 
							&& getPlayer(p).charge(locked.get(convert.get(args[0])),convert.get(args[0]))){
						p.sendMessage(ChatColor.GREEN + "You have bought " + args[0] + "!");
					}else if(getPlayer(p).ints.contains(convert.get(args[0]))){
						p.sendMessage(ChatColor.RED + "You already own this!");
					}else{
						p.sendMessage(ChatColor.RED + "You don't have enough money!");
					}
				}else{
					p.sendMessage(ChatColor.RED + "Invalid perk. Try /perks to view available perks.");
				}
			}else if(cmd.getName().equalsIgnoreCase("perks")){
				p.sendMessage(ChatColor.DARK_GREEN + "****** " + ChatColor.GREEN + "PERKS FOR SALE" + ChatColor.DARK_GREEN + " ******");
				for(int id : lockedIds){
					p.sendMessage(ChatColor.BLUE + names.get(id) + " -- " + ChatColor.DARK_AQUA + "$" + locked.get(id));
				}
				p.sendMessage(ChatColor.DARK_GREEN + "****** " + ChatColor.GREEN + "PERKS FOR SALE" + ChatColor.DARK_GREEN + " ******");				
			}
		}
		return true;
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
			config.addDefault("Perks.Message", "&cSorry you cant use this item!");
			config.addDefault("Perks.1.name", "enderpearl");
			config.addDefault("Perks.1.id", 368);
			config.addDefault("Perks.1.price", 25000);
			config.options().copyDefaults(true);
			saveConfig();
		}
		File file = new File("plugins/ToxicPerks/data/");
		if(!file.exists()){
			file.mkdir();
		}
		readConfig();
	}
	
	public void readConfig() {
		config = getConfig();
		message = Colorize(config.getString("Perks.Message"));
		for(int i=1;i<=configNum();i++){
			locked.put(config.getInt("Perks." + i + ".id"), config.getInt("Perks." + i + ".price"));
			convert.put(config.getString("Perks." + i + ".name"), config.getInt("Perks." + i + ".id"));
			lockedIds.add(config.getInt("Perks." + i + ".id"));
			names.put(config.getInt("Perks." + i + ".id"), config.getString("Perks." + i + ".name"));
		}
	}

	private boolean exists() {
		try {
			File file = new File("plugins/ToxicPerks/config.yml");
			return file.exists();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return false;
	}
	
	public PPlayer getPlayer(Player p){
		for(PPlayer tp : players){
			if(tp.getPlayer().getName().equalsIgnoreCase(p.getName())){
				return tp;
			}
		}
		return null;
	}
	
	public boolean hasIds(Player p){
		try {
			File file = new File("plugins/ToxicPerks/data/" + p.getName() + ".dat");
			return file.exists();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public HashSet<Integer> getIds(Player p) {
		ObjectInputStream ois;
		HashSet<Integer> tmp = null;
		try {
			ois = new ObjectInputStream(new FileInputStream("plugins/ToxicPerks/data/" + p.getName() + ".dat"));
			tmp = (HashSet<Integer>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}
	
}
