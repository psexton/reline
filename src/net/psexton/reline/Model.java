/*
 * Model.java, part of the reline project
 * Created on May 14, 2014, 12:14:22 PM
 */

package net.psexton.reline;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Calendar;
import javax.swing.JTextArea;
import javax.swing.Timer;

/**
 *
 * @author PSexton
 */
public class Model {
    private JTextArea console;
    private boolean isRunning;
    private Timer timer;
    DateFormat dateFormat;
    
    public Model() {
        console = null;
        isRunning = false;
        timer = null;
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
    }
    
    public void startMonitor(int intervalInSeconds, JTextArea console) {
        if(isRunning) {
            throw new IllegalStateException("Already monitoring");
        }
        isRunning = true;
        this.console = console;
        console.setText("");
        appendLine("Starting monitoring at " + intervalInSeconds + "s intervals");
        timer = new Timer(intervalInSeconds * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                monitor();
            }
        });
        timer.start();
    }
    
    public void stopMonitor() {
        if(!isRunning) {
            throw new IllegalStateException("Not monitoring");
        }
        timer.stop();
        isRunning = false;
        appendLine("Stopping monitoring");
    }
    
    private void monitor() {
        appendLine("Checking");
    }
    
    /**
     * Will be prefixed with date & time info, and suffixed with newline.
     * @param contents 
     */
    private void appendLine(String content) {
        String dateAndTimePrefix = "[" + dateFormat.format(Calendar.getInstance().getTime()) + "]";
        console.append(dateAndTimePrefix + " " + content + "\n");
    }
}
