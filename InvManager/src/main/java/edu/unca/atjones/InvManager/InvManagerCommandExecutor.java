package edu.unca.atjones.InvManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvManagerCommandExecutor implements CommandExecutor{
	
	private final InvManager plugin;
	
    public InvManagerCommandExecutor(InvManager plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player p = (Player)sender;
    		String name = cmd.getName();
    		
        	if(name.equalsIgnoreCase("invcreate")) {
        		if( p.hasPermission("inventory.admin.create") ) {
        			try {
        				Player target = Bukkit.getServer().getPlayer(args[0]);
        				if(target == null) throw new InvManagerException("Player not found.");
        				plugin.createInventory(target,args[1],args[2]);
        				p.sendMessage("Inventory successfully created.");
        			}
        			catch(InvManagerException e) { p.sendMessage(e.getMessage()); }
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invcreate <player> <inventory> [<type>]");
        			}
        		}
        		else if( p.hasPermission("inventory.create")) {
        			try {
        				plugin.useUpgrade(p);
        				plugin.createInventory(p,args[0],"small");
        				p.sendMessage("Inventory successfully created.");
        			}
        			catch(InvManagerException e) { p.sendMessage(e.getMessage()); }
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invcreate <inventory>");
        			}
        		}
        		else {
        			p.sendMessage("You cannot execute this command.");
        		}
        		return true;
        	}
        	
    		if(name.equalsIgnoreCase("invhelp")) {
    			if( p.hasPermission("inventory.admin.help") ) {
    				return false;
    			} 
    			else if( p.hasPermission("inventory.help") ) {
    				return false;
    			}
    			else {
        			p.sendMessage("You cannot execute this command.");
    			}
        		return true;
    		}
    		
        	if(name.equalsIgnoreCase("invopen")) {
	        	if(p.hasPermission("inventory.admin.open")) {
        			try{ 
        				Player target = Bukkit.getServer().getPlayer(args[0]);
        				if(target == null) throw new InvManagerException("Player not found.");
        				plugin.showInventory(target,args[1]); 
        			}
        			catch(InvManagerException e) { p.sendMessage(e.getMessage()); }
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invopen <player> <inventory>");
        			}
	        	}
	        	else if( p.hasPermission("inventory.open") ) {
        			try{ 
        				plugin.showInventory(p,args[0]); 
        			}
        			catch(InvManagerException e) { p.sendMessage(e.getMessage()); }
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invopen <inventory>");
        			}
	        	}
	        	else p.sendMessage("You cannot execute this command.");
	        	return true;
        	}
        	
        	if(name.equalsIgnoreCase("invupgrade")) {
	        	if(p.hasPermission("inventory.admin.upgrade")) {
        			try{ 
        				Player target = Bukkit.getServer().getPlayer(args[0]);
        				if(target == null) throw new InvManagerException("Player not found.");
        				plugin.upgradeInventory(target,args[1]); 
        			}
        			catch(InvManagerException e) { p.sendMessage(e.getMessage()); }
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invopen <player> <inventory>");
        			}
	        	}
	        	else if( p.hasPermission("inventory.upgrade") ) {
        			try{ 
        				plugin.useUpgrade(p);
        				plugin.upgradeInventory(p,args[0]); 
        				p.sendMessage("Inventory Upgraded");
        			}
        			catch(InvManagerException e) { p.sendMessage(e.getMessage()); }
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invopen <inventory>");
        			}
	        	}
	        	else p.sendMessage("You cannot execute this command.");
	        	return true;
        	}
        	
        	if(name.equalsIgnoreCase("invgrant")) {
	        	if(p.hasPermission("inventory.admin.grant")) {
        			try{ 
        				Player target = Bukkit.getServer().getPlayer(args[0]);
        				if(target == null) throw new InvManagerException("Player not found.");
        				plugin.grantUpgrade(target,Integer.valueOf(args[1])); 
        			}
        			catch(InvManagerException e) { p.sendMessage(e.getMessage()); }
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invgrant <player> <number>");
        			}
	        	}
	        	else p.sendMessage("You cannot execute this command.");
	        	return true;
        	}

        	if(name.equalsIgnoreCase("invremove")) {
        		if( p.hasPermission("inventory.admin.remove") ) {
        			try{ 
        				Player target = Bukkit.getServer().getPlayer(args[0]);
        				if(target == null) throw new InvManagerException("Player not found.");
        				plugin.removeInventory(target,args[1]); 
        			}
        			catch(InvManagerException e) { p.sendMessage(e.getMessage()); }
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invremove <player> <inventory>");
        			} 
        		}
	        	else p.sendMessage("You cannot execute this command.");
	        	return true;
    		} 
        	
        	if(name.equalsIgnoreCase("invroute")) {
    			if( p.hasPermission("inventory.admin.route") ) {
        			try {
        				Player target = Bukkit.getServer().getPlayer(args[0]);
        				if(target == null) throw new InvManagerException("Player not found.");
        				plugin.addRoute(target,args[1],args[2]);
        				p.sendMessage("Route successfully created.");
        			}
        			catch(InvManagerException e) { p.sendMessage(e.getMessage()); }
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invroute <player> <item> <inventory>|default|ignore");
        			}
    			} 
    			else if( p.hasPermission("inventory.route") ) {
        			try {
        				plugin.addRoute(p,args[0],args[1]);
        				p.sendMessage("Route successfully created.");
        			}
        			catch(InvManagerException e) { p.sendMessage(e.getMessage()); }
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invroute <item> <inventory>|default|ignore");
        			}
    			}
    			else p.sendMessage("You cannot execute this command.");
        		return true;
	    	}
        	
        	if(name.equalsIgnoreCase("invlist")) {
    			if( p.hasPermission("inventory.admin.list") ) {
        			try {
        				Player target = Bukkit.getServer().getPlayer(args[0]);
        				if(target == null) throw new InvManagerException("Player not found.");
        				Set<String> names = plugin.listInventories(target);
        				Iterator<String> iterator = names.iterator();
        				p.sendMessage(String.format("%s have %d Inventories:",target.getName(),names.size()));
        				while(iterator.hasNext()) {
        					String str = iterator.next();
        					p.sendMessage(str);
        				}
        			}
        			catch(InvManagerException e) { p.sendMessage(e.getMessage()); }
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invlist <player>");
        			}
    			} 
    			else if( p.hasPermission("inventory.list") ) {
        			try {
        				Set<String> names = plugin.listInventories(p);
        				Iterator<String> iterator = names.iterator();
        				p.sendMessage(String.format("You have %d Inventories:",names.size()));
        				while(iterator.hasNext()) {
        					String str = iterator.next();
        					p.sendMessage(str);
        				}
        			}
        			catch(InvManagerException e) { p.sendMessage(e.getMessage()); }
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invlist");
        			}
    			}
    			else p.sendMessage("You cannot execute this command.");
        		return true;
	    	}
        	
        	if(name.equalsIgnoreCase("invupgrades")) {
    			if( p.hasPermission("inventory.admin.upgrades") ) {
        			try {
        				Player target = Bukkit.getServer().getPlayer(args[0]);
        				if(target == null) throw new InvManagerException("Player not found.");
        				p.sendMessage(String.format("%s has %d upgrades available.",args[0],plugin.numUpgrades(target)));
        			}
        			catch(InvManagerException e) { p.sendMessage(e.getMessage()); }
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invupgrades <player");
        			}
    			} 
    			else if( p.hasPermission("inventory.upgrades") ) {
        			try {
        				p.sendMessage(String.format("You have %d upgrades available.",plugin.numUpgrades(p)));
        			}
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invupgrades");
        			}
    			}
    			else p.sendMessage("You cannot execute this command.");
        		return true;
	    	}
        	
        	if(name.equalsIgnoreCase("invroutes")) {
    			if( p.hasPermission("inventory.admin.routes") ) {
        			try {
        				Player target = Bukkit.getServer().getPlayer(args[0]);
        				if(target == null) throw new InvManagerException("Player not found.");
        				String playerName = target.getName();
        				if(plugin.routes.containsKey(playerName)) {
        					HashMap<Integer,String> routeMap = plugin.routes.get(playerName);
        					p.sendMessage(String.format("%s has %d route(s):",playerName,routeMap.size()));
        					Set<Integer> keys = routeMap.keySet();
        					Iterator<Integer> iterator = keys.iterator();
        					while(iterator.hasNext()) {
        						int id = iterator.next();
        						String inventory = routeMap.get(id);
        						String str = Material.getMaterial(id).name();
        						p.sendMessage(String.format("  %s -> %s",str,inventory));
        					}
        				}
        				else p.sendMessage(String.format("%s has no routes.",playerName));
        			}
        			catch(InvManagerException e) { p.sendMessage(e.getMessage()); }
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invroutes <player>");
        			}
    			} 
    			else if( p.hasPermission("inventory.routes") ) {
        			try {
        				String playerName = p.getName();
        				if(plugin.routes.containsKey(playerName)) {
        					HashMap<Integer,String> routeMap = plugin.routes.get(playerName);
        					p.sendMessage(String.format("You have %d route(s):",routeMap.size()));
        					Set<Integer> keys = routeMap.keySet();
        					Iterator<Integer> iterator = keys.iterator();
        					while(iterator.hasNext()) {
        						int id = iterator.next();
        						String inventory = routeMap.get(id);
        						String str = Material.getMaterial(id).name();
        						p.sendMessage(String.format("  %s -> %s",str,inventory));
        					}
        				}
        				else p.sendMessage("You have no routes.");
        			}
        			catch(ArrayIndexOutOfBoundsException e) {
        				p.sendMessage("/invroutes");
        			}
    			}
    			else p.sendMessage("You cannot execute this command.");
        		return true;
	    	}        	
        	
    	} else
    	sender.sendMessage("Command must be executed by a player!");
    	return false;
    }
}
