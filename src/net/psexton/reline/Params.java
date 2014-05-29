/*
 * Params.java, part of the reline project
 * Created on May 29, 2014, 12:40:36 PM
 * Copyright (C) 2014 PSexton
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.psexton.reline;

import java.util.prefs.Preferences;

/**
 * Holder for parameters we want to pass from GUI to Model.
 * Threadsafe because it's an immutable class.
 * @author PSexton
 */
public final class Params {
    private static final int DEFAULT_INTEGER = -1;
    private static final boolean DEFAULT_BOOLEAN = false;
    
    
    private final int intervalInSeconds;
    private final boolean writeImagesToDisk;
    private final int restartX;
    private final int restartY;
    private final int restartWidth;
    private final int restartHeight;
    private final int joinX;
    private final int joinY;
    private final int joinWidth;
    private final int joinHeight;
    
    public Params(int intervalInSeconds, boolean writeImagesToDisk, 
            int restartX, int restartY, int restartWidth, int restartHeight,
            int joinX, int joinY, int joinWidth, int joinHeight) {
        this.intervalInSeconds = intervalInSeconds;
        this.writeImagesToDisk = writeImagesToDisk;
        this.restartX = restartX;
        this.restartY = restartY;
        this.restartWidth = restartWidth;
        this.restartHeight = restartHeight;
        this.joinX = joinX;
        this.joinY = joinY;
        this.joinWidth = joinWidth;
        this.joinHeight = joinHeight;
    }

    /**
     * Get the value of writeImagesToDisk
     *
     * @return the value of writeImagesToDisk
     */
    public boolean isWriteImagesToDisk() {
        return writeImagesToDisk;
    }

    /**
     * Get the value of intervalInSeconds
     *
     * @return the value of intervalInSeconds
     */
    public int getIntervalInSeconds() {
        return intervalInSeconds;
    }
    
    /**
     * Get the value of restartX
     * 
     * @return the value of restartX
     */
    public int getRestartX() {
        return restartX;
    }
    
    /**
     * Get the value of restartY
     * 
     * @return the value of restartY
     */
    public int getRestartY() {
        return restartY;
    }
    
    /**
     * Get the value of restartWidth
     * 
     * @return the value of restartWidth
     */
    public int getRestartWidth() {
        return restartWidth;
    }
    
    /**
     * Get the value of restartHeight
     * 
     * @return the value of restartHeight
     */
    public int getRestartHeight() {
        return restartHeight;
    }
    
    /**
     * Get the value of joinX
     * 
     * @return the value of joinX
     */
    public int getJoinX() {
        return joinX;
    }
    
    /**
     * Get the value of joinY
     * 
     * @return the value of joinY
     */
    public int getJoinY() {
        return joinY;
    }
    
    /**
     * Get the value of joinWidth
     * 
     * @return the value of joinWidth
     */
    public int getJoinWidth() {
        return joinWidth;
    }
    
    /**
     * Get the value of joinHeight
     * 
     * @return the value of joinHeight
     */
    public int getJoinHeight() {
        return joinHeight;
    }
    
    public void writeToPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(Params.class);
        prefs.putInt("intervalInSeconds", intervalInSeconds);
        prefs.putBoolean("writeImagesToDisk", writeImagesToDisk);
        prefs.putInt("restartX", restartX);
        prefs.putInt("restartY", restartY);
        prefs.putInt("restartWidth", restartWidth);
        prefs.putInt("restartHeight", restartHeight);
        prefs.putInt("joinX", joinX);
        prefs.putInt("joinY", joinY);
        prefs.putInt("joinWidth", joinWidth);
        prefs.putInt("joinHeight", joinHeight);
    }
    
    public static Params readFromPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(Params.class);
        return new Params(
                prefs.getInt("intervalInSeconds", DEFAULT_INTEGER),
                prefs.getBoolean("writeImagesToDisk", DEFAULT_BOOLEAN),
                prefs.getInt("restartX", DEFAULT_INTEGER),
                prefs.getInt("restartY", DEFAULT_INTEGER),
                prefs.getInt("restartWidth", DEFAULT_INTEGER),
                prefs.getInt("restartHeight", DEFAULT_INTEGER),
                prefs.getInt("joinX", DEFAULT_INTEGER),
                prefs.getInt("joinY", DEFAULT_INTEGER),
                prefs.getInt("joinWidth", DEFAULT_INTEGER),
                prefs.getInt("joinHeight", DEFAULT_INTEGER));
    }

}
