/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.martin.bukkit.mcbackdoor.scmd;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author martin
 */
public interface Module {
    public void enable();
    public void disable();
    public void setPlugin(JavaPlugin pl);
    public boolean onCommand(CommandSender sender, String[] args, String unclean);
    public String getCMD();
}
