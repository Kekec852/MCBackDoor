/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.martin.bukkit.mcbackdoor.utils;

import java.io.OutputStream;

/**
 *
 * @author martin
 */
public class AsyncCommandSender{

    public OutputStream out;
    
    public void setOutputStream(OutputStream out){
        this.out = out;
    }
}
