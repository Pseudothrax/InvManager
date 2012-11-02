package edu.unca.atjones.InvManager;

import org.bukkit.plugin.java.JavaPlugin;

/*
 * This is the main class of the sample plug-in
 */
public final class InvManager extends JavaPlugin {
    
	InvManagerLogger logger;
    @Override
    public void onEnable() {

		logger = new InvManagerLogger(this);
		logger.info("plugin enabled");
		
        saveDefaultConfig();
        
        // Create the SampleListener
        new InvManagerListener(this);
        
        // set the command executor for sample
        this.getCommand("sample").setExecutor(new InvManagerCommandExecutor(this));
    }
    
    /*
     * This is called when your plug-in shuts down
     */
    @Override
    public void onDisable() {
        
    }

}
