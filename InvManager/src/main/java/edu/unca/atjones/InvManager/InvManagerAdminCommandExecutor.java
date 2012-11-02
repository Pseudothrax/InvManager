package edu.unca.atjones.InvManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/*
 * This is a sample CommandExectuor
 */
public class InvManagerAdminCommandExecutor implements CommandExecutor {
    private final InvManager plugin;

    /*
     * This command executor needs to know about its plugin from which it came from
     */
    public InvManagerAdminCommandExecutor(InvManager plugin) {
        this.plugin = plugin;
    }

    /*
     * On command set the sample message
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	Player p = (Player)sender;
    	if(p.hasPermission("inventory.admin")) {
    		if( args[0].equalsIgnoreCase("create")) {
    			try{
    				plugin.createInventory(p,args[1],args[2]);
    			} catch(InvManagerException e) {
    				p.sendMessage(e.getMessage());
    			}
    			p.sendMessage("Inventory successfully created.");
    		}
    		else {
    			p.sendMessage("Argument " + args[0] + " unrecognized.");
    		}
    	} else {
    		p.sendMessage("You lack the required permissions.");
    	}
    	return true;
    }

}
