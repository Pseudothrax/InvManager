package edu.unca.atjones.InvManager;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public final class InvManager extends JavaPlugin {
    
	HashMap<String,HashMap<String,Inventory>> inventories;
	HashMap<String,HashMap<Integer,String>> routes;
	HashMap<String,Integer> blocks;
	HashMap<String,Integer> upgrades;
	
	InvManagerLogger logger;
	
	public InvManagerDatabase database;
	
    @Override
    public void onEnable() {

		logger = new InvManagerLogger(this);
		logger.info("plugin enabled");
		
		database = new InvManagerDatabase(this);
		
		inventories = new HashMap<String,HashMap<String,Inventory>>();
		routes = new HashMap<String,HashMap<Integer,String>>();
		blocks = new HashMap<String,Integer>();
		upgrades = new HashMap<String,Integer>();
		
        saveDefaultConfig();
        
        // Create Listener
        new InvManagerListener(this);
        
        // set the command executors
        this.getCommand("invcreate").setExecutor(new InvManagerCommandExecutor(this));
        this.getCommand("invgrant").setExecutor(new InvManagerCommandExecutor(this));        
        this.getCommand("invhelp").setExecutor(new InvManagerCommandExecutor(this));
        this.getCommand("invlist").setExecutor(new InvManagerCommandExecutor(this));
        this.getCommand("invopen").setExecutor(new InvManagerCommandExecutor(this));        
        this.getCommand("invremove").setExecutor(new InvManagerCommandExecutor(this));
        this.getCommand("invrename").setExecutor(new InvManagerCommandExecutor(this));
        this.getCommand("invroute").setExecutor(new InvManagerCommandExecutor(this));
        this.getCommand("invroutes").setExecutor(new InvManagerCommandExecutor(this));
        this.getCommand("invupgrade").setExecutor(new InvManagerCommandExecutor(this));
        this.getCommand("invupgrades").setExecutor(new InvManagerCommandExecutor(this));
        this.getCommand("invsave").setExecutor(new InvManagerCommandExecutor(this)); 
        
    }
    
    @Override
    public void onDisable() {
    	database.close();
    }
    
    
    public void showInventory(Player owner,String name) throws InvManagerException {
    	if(name == null) {
    		throw new InvManagerException("Must provide an inventory name.");
    	}
    	if(!inventories.containsKey(owner.getName())) {
    		throw new InvManagerException("You have no inventories.");
    	}
    	HashMap<String,Inventory> invs = inventories.get(owner.getName());
    	if(!invs.containsKey(name)){
    		throw new InvManagerException("You have no inventory called " + name);
    	}
    	InvManagerInventory inv = (InvManagerInventory) invs.get(name);
    	owner.openInventory(inv);
    }
    
    public void createInventory(Player owner,String name,String type) throws InvManagerException {
    	if(name == null) throw new InvManagerException("Must provide a name for this inventory.");
    	int size = 0;
    	if(type != null) {
	    	if(type.equalsIgnoreCase("small")) size = 9;
	    	else if(type.equalsIgnoreCase("medium")) size = 18;
	    	else if(type.equalsIgnoreCase("large")) size = 27;
	    	else throw new InvManagerException(type + "is not a valid inventory type");
    	} else size = 9;
    	
    	HashMap<String,Inventory> invs;
    	if(inventories.containsKey(owner.getName())) {
    		invs = inventories.get(owner.getName());
    	} else {
    		invs = new HashMap<String,Inventory>();
    	}
		if(invs.containsKey(name)) throw new InvManagerException("An inventory by that name already exists");
    	InvManagerInventory newInv = new InvManagerInventory(owner, size, name);
		invs.put(name, newInv);
		inventories.put(owner.getName(), invs);
    }
    
    public void upgradeInventory(Player owner, String name) throws InvManagerException {
    	if(inventories.containsKey(owner.getName())) {
    		HashMap<String,Inventory> invs = inventories.get(owner.getName());
    		if(invs.containsKey(name)) {
    			Inventory i = invs.get(name);
    			if(i.getSize() != 27){
    				int size = i.getSize() + 9;
    				InvManagerInventory I = new InvManagerInventory(owner, size, name);
    				I.setContents(i.getContents());
    				invs.put(name, I);
    				inventories.put(owner.getName(), invs);
    			} else throw new InvManagerException("Inventory cannot be upgraded (Too large)");
    		}
    		else throw new InvManagerException("Inventory not found.");
    	} 
    	else throw new InvManagerException("Player has no inventories.");
    }
    
    public void removeInventory(Player owner, String name) throws InvManagerException {
    	if(inventories.containsKey(owner.getName())) {
    		HashMap<String,Inventory> invs = inventories.get(owner.getName());
    		if(invs.containsKey(name)) {
    			invs.remove(name);
    		}
    		else throw new InvManagerException("Inventory not found.");
    	} 
    	else throw new InvManagerException("Player has no inventories.");
    }
    
    public Set<String> listInventories(Player owner) throws InvManagerException {
    	if(inventories.containsKey(owner.getName())) {
    		HashMap<String,Inventory> invs = inventories.get(owner.getName());
    		Set<String> names = invs.keySet();
    		return names;
    	} 
    	else throw new InvManagerException("Player has no inventories.");
    }
    
    public void grantUpgrade(Player owner,int num) {
    	String playerName = owner.getName();
    	if(upgrades.containsKey(playerName)) {
    		int count = upgrades.get(playerName) + num;
    		upgrades.put(playerName,count);
    	}
    	else upgrades.put(playerName,num);
    }
    
    public boolean hasUpgrade(Player owner) {
    	String playerName = owner.getName();
    	if(upgrades.containsKey(playerName)) {
    		if(upgrades.get(playerName) > 0) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public int numUpgrades(Player owner) {
    	String playerName = owner.getName();
    	if(upgrades.containsKey(playerName)) {
    		return upgrades.get(playerName);
    	}
    	return 0;
    }
    
    public void useUpgrade(Player owner) throws InvManagerException {
    	String playerName = owner.getName();
    	if(upgrades.containsKey(playerName)) {
    		int count = upgrades.get(playerName);
    		if( count > 0) {
    			count--;
        		upgrades.put(playerName,count);
    		}
    		else throw new InvManagerException("Player has no upgrades");
    	}
    	else throw new InvManagerException("Player has no upgrades");
    }
    
    public void addRoute(Player owner, String item, String inventory) throws InvManagerException {
		HashMap<Integer,String> playerRoutes;
		HashMap<String,Inventory> playerInventories;
		String player = owner.getName();
		
		if(routes.containsKey(player)) {
			playerRoutes = routes.get(player);
		} else {
			playerRoutes = new HashMap<Integer,String>();
		}
		
		if(inventories.containsKey(player)) {
			playerInventories = inventories.get(player);
		} else throw new InvManagerException("No Inventories found!");
		
		if(playerInventories.containsKey(inventory)) {
			//First see if item is the item name string
			Material itemMaterial = Material.matchMaterial(item);
			
			//If that doesn't work try it as an integer item id
			if(itemMaterial == null) {
				try {
					itemMaterial = Material.getMaterial(Integer.parseInt(item));
				} catch(NumberFormatException e) {
					throw new InvManagerException("Unrecognized Item");
				}	
			}
			
			//If the item still hasn't been recognized throw an exception
			if(itemMaterial == null) throw new InvManagerException("Unrecognized Item");
			
			//Otherwise add route
			playerRoutes.put(itemMaterial.getId(),inventory);
			routes.put(player,playerRoutes);
		}
				
	}

}
