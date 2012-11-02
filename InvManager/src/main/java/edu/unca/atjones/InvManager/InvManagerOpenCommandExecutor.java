package edu.unca.atjones.InvManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * This is a sample CommandExectuor
 */
public class InvManagerOpenCommandExecutor implements CommandExecutor {
    private final InvManager plugin;

    /*
     * This command executor needs to know about its plugin from which it came from
     */
    public InvManagerOpenCommandExecutor(InvManager plugin) {
        this.plugin = plugin;
    }

    /*
     * On command set the sample message
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	Player p = (Player)sender;
    	if(p.hasPermission("inventory.open")) {
    		if( args.length == 1 ) {
    			//Open Chest
    			try{
    				plugin.showInventory(p,args[0]);
    			} catch(InvManagerException e) {
    				p.sendMessage(e.getMessage());
    			}
    		} else {
    			p.sendMessage("You must provide at least one argument");
    		}
    	} else {
    		p.sendMessage("You lack the required permissions.");
    	}
    	return true;
    }

}
