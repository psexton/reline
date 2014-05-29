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
 *
 * @author PSexton
 */
public class Params {
    private static final int DEFAULT_INTERVAL_IN_SECONDS = -1;
    private static final boolean DEFAULT_WRITE_IMAGES_TO_DISK = false;
    
    private final int intervalInSeconds;
    private final boolean writeImagesToDisk;
    
    public Params(int intervalInSeconds, boolean writeImagesToDisk) {
        this.intervalInSeconds = intervalInSeconds;
        this.writeImagesToDisk = writeImagesToDisk;
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
    
    public void writeToPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(Params.class);
        prefs.putInt("intervalInSeconds", intervalInSeconds);
        prefs.putBoolean("writeImagesToDisk", writeImagesToDisk);
    }
    
    public static Params readFromPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(Params.class);
        return new Params(
                prefs.getInt("intervalInSeconds", DEFAULT_INTERVAL_IN_SECONDS),
                prefs.getBoolean("writeImagesToDisk", DEFAULT_WRITE_IMAGES_TO_DISK));
    }

}
