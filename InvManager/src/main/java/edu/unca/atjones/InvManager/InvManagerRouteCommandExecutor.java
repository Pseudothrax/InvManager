package edu.unca.atjones.InvManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * This is a sample CommandExectuor
 */
public class InvManagerRouteCommandExecutor implements CommandExecutor {
    private final InvManager plugin;

    /*
     * This command executor needs to know about its plugin from which it came from
     */
    public InvManagerRouteCommandExecutor(InvManager plugin) {
        this.plugin = plugin;
    }

    /*
     * On command set the sample message
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	Player p = (Player)sender;
    	p.sendMessage("route command used");
    	return false;
    }

}
