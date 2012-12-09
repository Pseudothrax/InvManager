package edu.unca.atjones.InvManager;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityTracker;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Packet22Collect;
import net.minecraft.server.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;

import edu.unca.atjones.InvManager.InvManagerInventory.MinecraftInventory;

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
    public void onPickupItem(PlayerPickupItemEvent event) {
    	Player p = event.getPlayer();
    	String playerName = p.getName();
    	
    	Item item = event.getItem();
    	int typeId = item.getItemStack().getTypeId();
    	
    	if(plugin.inventories.containsKey(playerName)) {
    		if(plugin.routes.containsKey(playerName)) {
    			HashMap<Integer,String> playerRoutes = plugin.routes.get(playerName);
        		HashMap<String,Inventory> playerInventories = plugin.inventories.get(playerName);
        		if(playerRoutes.containsKey(typeId)) {
        			String destName = playerRoutes.get(typeId);
        			if(playerInventories.containsKey(destName)) {
        				InvManagerInventory dest = (InvManagerInventory) playerInventories.get(destName);
        				
    			    	String worldName = item.getWorld().getName();
    			    	CraftServer cserver = (CraftServer) Bukkit.getServer();
    			    	MinecraftServer mcserver = cserver.getServer();
    			    	
    			    	CraftWorld w = (CraftWorld) cserver.getWorld(worldName);
    			    	Location location = p.getLocation();
    			    	WorldServer worldserver = w.getHandle();
    			    	ItemInWorldManager iiw = new ItemInWorldManager(worldserver);
    					
    					EntityItem entity = (EntityItem) worldserver.getEntity(item.getEntityId());
    					EntityPlayer player = (EntityPlayer) worldserver.getEntity(p.getEntityId());
    							
    					entity.pickupDelay = 0;
    					p.sendMessage(String.format("Delay: %d",entity.pickupDelay));
    					if ( ( (MinecraftInventory) dest.getInventory()).pickup(entity.itemStack) ) {
    						p.sendMessage("yes");
    						Random random = new Random();
    						entity.world.makeSound(entity, "random.pop", 0.2F, ((random.nextFloat() - random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
    						EntityTracker entitytracker = worldserver.getTracker();
    						entitytracker.a(entity, new Packet22Collect(entity.id, player.id));
    			            if (entity.itemStack.count <= 0) {
    			            	entity.die();
    			            }
    			        }
    					event.setCancelled(true);
        			}
        		};
    		}
    	}	
    }
}
