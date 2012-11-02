package edu.unca.atjones.InvManager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * This is the main class of the sample plug-in
 */
public final class InvManager extends JavaPlugin {
    
	InvManagerLogger logger;
	HashMap<Player,HashMap<String,Inventory>> inventories;
	
    @Override
    public void onEnable() {

		logger = new InvManagerLogger(this);
		logger.info("plugin enabled");
		
		inventories = new HashMap<Player,HashMap<String,Inventory>>();
		
        saveDefaultConfig();
        
        // Create the SampleListener
        new InvManagerListener(this);
        
        // set the command executor for sample
        this.getCommand("invopen").setExecutor(new InvManagerOpenCommandExecutor(this));
        this.getCommand("invroute").setExecutor(new InvManagerRouteCommandExecutor(this));
        this.getCommand("invadmin").setExecutor(new InvManagerAdminCommandExecutor(this));
        
    }
    
    @Override
    public void onDisable() {
        
    }
    
    public void showInventory(Player owner,String name) throws InvManagerException {
    	if(name == null) {
    		throw new InvManagerException("Must provide an inventory name.");
    	}
    	if(!inventories.containsKey(owner)) {
    		throw new InvManagerException("You have no inventories.");
    	}
    	HashMap<String,Inventory> invs = inventories.get(owner);
    	if(!invs.containsKey(name)){
    		throw new InvManagerException("You have no inventory called " + name);
    	}
    	Inventory inv = invs.get(name);
    	owner.openInventory(inv);
    }
    
    public void createInventory(Player owner,String type,String name) throws InvManagerException {
    	String title = name;
    	if(name == null) throw new InvManagerException("Must provide a name for this inventory.");
    	int size = 0;
    	if(type != null) {
	    	if(type.equalsIgnoreCase("small")) size = 9;
	    	else if(type.equalsIgnoreCase("medium")) size = 18;
	    	else if(type.equalsIgnoreCase("large")) size = 27;
	    	else throw new InvManagerException(type + "is not a valid inventory type");
    	} else throw new InvManagerException("Must provide an inventory type");
    	
    	Inventory newInv = Bukkit.getServer().createInventory(owner, size, title);
    	HashMap<String,Inventory> invs;
    	if(inventories.containsKey(owner)) {
    		invs = inventories.get(owner);
    	} else {
    		invs = new HashMap<String,Inventory>();
    	}
		if(invs.containsKey(title)) throw new InvManagerException("An inventory by that name already exists");
		invs.put(title, newInv);
		inventories.put(owner, invs);
    }

}
