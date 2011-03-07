/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.bukkit.mcbackdoor.hacks;

import com.bukkit.MCDaemon;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Kekec852
 */
public class get_mc_server {

    public static MinecraftServer getMCServer(JavaPlugin plugin) {
        MinecraftServer mcServer = null;
        if ((mcServer = method1(plugin)) != null) {
            return mcServer;
        }
        if ((mcServer = method2(plugin)) != null) {
            return mcServer;
        }
        if ((mcServer = method3(plugin)) != null) {
            return mcServer;
        }
        return null;
    }

    private static MinecraftServer method1(JavaPlugin plugin) {
        Server server = plugin.getServer();
        CraftServer cServer = null;
        try {
            cServer = (CraftServer) server;
            return cServer.getServer();
        } catch (Exception ex) {
            Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static MinecraftServer method2(JavaPlugin plugin) {
        Server server = plugin.getServer();
        Field f;
        try {
            CraftServer cServer = (CraftServer) server;
            f = CraftServer.class.getDeclaredField("console");
            f.setAccessible(true);
            return (MinecraftServer) f.get(cServer);
        } catch (Exception ex) {
            Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static MinecraftServer method3(JavaPlugin plugin) {
        boolean done = false;
        try {
            Class.forName("com.bukkit.MCDaemon");
            done = true;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
        }
        if (done) {
            return MCDaemon.getInstance().getServer();
        }
        return null;
    }
}
