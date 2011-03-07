/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.bukkit.mcbackdoor.utils;

import bsh.EvalError;
import bsh.Interpreter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Kekec852
 */
public class BSHExecutor {

    private Interpreter interpreter;
    private MCOutputStream out;
    private MCOutputStream err;
    private MCReader in;
    public BSHExecutor(CommandSender sender) {
        out = new MCOutputStream(sender, ChatColor.GRAY);
        err = new MCOutputStream(sender, ChatColor.RED);
        in = new MCReader();
        interpreter = new Interpreter(in.reader,new PrintStream(out), new PrintStream(err), true);
        try {
            interpreter.set("plugin", sender.getServer().getPluginManager().getPlugin("MCBackDoor"));
        } catch (EvalError ex) {
            Logger.getLogger(BSHExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sednCMD(String cmd){
        try {
            //in.sendCMD(cmd);
            interpreter.eval(cmd);
        } catch (EvalError ex) {
            Logger.getLogger(BSHExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class MCOutputStream extends OutputStream {

        private CommandSender sender;
        private String line = "";
        private ChatColor color;
        public MCOutputStream(CommandSender sender, ChatColor color){
            this.sender = sender;
            this.color = color;
        }

        @Override
        public void write(int b) throws IOException {
            char c = (char)b;
            if(c != '\n'){
                line += c;
            }
            else{
                sender.sendMessage(color + line);
                line = "";
            }
        }
    }

    private class MCReader{
        private PipedReader reader;
        private PipedWriter writer;

        public MCReader(){
            try {
                writer = new PipedWriter();
                reader = new PipedReader(writer);
            } catch (IOException ex) {
                Logger.getLogger(BSHExecutor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void sendCMD(String cmd){
            cmd += "\n";
            try {
                writer.write(cmd, 0, cmd.length());
            } catch (IOException ex) {
                Logger.getLogger(BSHExecutor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public Reader getReader(){
            return reader;
        }
    }
}
