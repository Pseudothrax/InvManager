package edu.unca.atjones.InvManager;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import edu.unca.atjones.MoreEvents.InventoryAddEvent;

public class InvManagerListener implements Listener {
    private final InvManager plugin;

    public InvManagerListener(InvManager plugin) {
        // Register the listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
    	//increment the player's block count
    	Player p = event.getPlayer();
    	String playerName = p.getName();
    	if(p.hasPermission("inventory.reward")) {
    		if(plugin.blocks.containsKey(playerName)) {
        		int count = plugin.blocks.get(playerName) + 1;
        		if(count == 10) {
        			plugin.blocks.put(playerName, 0);
        			plugin.grantUpgrade(p,1);
        			p.sendMessage("You've received an upgrade for your hard work!");
        			p.sendMessage("  Type /invupgrade <inventory name> to upgrade the size of one of your inventories.");
        			p.sendMessage("  Type /invcreate <inventory name> to create a new small inventory.");
        			p.sendMessage("  Type /invhelp for additional help.");
        		}
        		else {
        			plugin.blocks.put(playerName, count);
        		}
        	} else {
        		plugin.blocks.put(playerName, 1);
        	}
    	}
    }
    
    @EventHandler
    public void onInventoryAdd(InventoryAddEvent event) {
    	//route the item according to assigned routing rules
    	Player p = (Player)event.getPlayer();
    	String player = p.getName();
    	ItemStack item = event.getItemStack();
    	int itemId = item.getData().getItemTypeId();
    	String itemName = item.getData().getItemType().toString();
    	
    	if(plugin.inventories.containsKey(player)) {
    		if(plugin.routes.containsKey(player)) {
    			HashMap<Integer,String> playerRoutes = plugin.routes.get(player);
        		HashMap<String,Inventory> playerInventories = plugin.inventories.get(player);
        		if(playerRoutes.containsKey(itemId)) {
        			String destName = playerRoutes.get(itemId);
        			if(playerInventories.containsKey(destName)) {
        				Inventory dest = playerInventories.get(destName);
        				if(dest.firstEmpty() != -1) {
            				event.setCancelled(true);
            				dest.addItem(item);
        				}
        			}
        		};
    		}
    	}
    }

}
