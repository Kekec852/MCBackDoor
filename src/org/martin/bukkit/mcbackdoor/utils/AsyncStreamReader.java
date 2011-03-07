package org.martin.bukkit.mcbackdoor.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * http://www.javalobby.org/java/forums/t53333.html
 * 
 * @author Venkat
 *
 */
class AsyncStreamReader extends Thread {

    private StringBuffer fBuffer = null;
    private InputStream fInputStream = null;
    private String fThreadId = null;
    private boolean fStop = false;
    private String fNewLine = null;
    private CommandSender sender;

    public AsyncStreamReader(InputStream inputStream, StringBuffer buffer, String threadId, CommandSender sender) {
        fInputStream = inputStream;
        fBuffer = buffer;
        fThreadId = threadId;
        this.sender = sender;
        fNewLine = System.getProperty("line.separator");
    }

    public String getBuffer() {
        return fBuffer.toString();
    }

    public void run() {
        try {
            readCommandOutput();
        } catch (Exception ex) {
            //ex.printStackTrace(); //DEBUG
        }
    }

    private void readCommandOutput() throws IOException {
        BufferedReader bufOut = new BufferedReader(new InputStreamReader(fInputStream));
        String line = null;
        while ((fStop == false) && ((line = bufOut.readLine()) != null)) {
            //fBuffer.append(line + fNewLine);
            printToDisplayDevice(line);
            printToConsole(line);
        }
        bufOut.close();
        //printToConsole("END OF: " + fThreadId); //DEBUG
    }

    public void stopReading() {
        fStop = true;
    }

    private void printToDisplayDevice(String line) {
        if (fThreadId.equalsIgnoreCase("ERROR")) {
            sender.sendMessage(ChatColor.RED + line);
        } else {
            sender.sendMessage(line);
        }
    }

    private synchronized void printToConsole(String line) {
        System.out.println(line);
    }
}
