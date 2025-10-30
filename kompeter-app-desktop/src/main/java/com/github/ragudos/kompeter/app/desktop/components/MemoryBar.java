/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

import javax.swing.JProgressBar;
import javax.swing.Timer;

public class MemoryBar extends JProgressBar {

    private String format = "%s / %s";
    private int refreshRate = 500;
    private Timer timer;
    private String value = "";

    public MemoryBar() {
        setStringPainted(true);
    }

    public String getFormat() {
        return format;
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    @Override
    public String getString() {
        return value;
    }

    public void installMemoryBar() {
        uninstallMemoryBar();

        updateMemoryUsage();

        timer = new Timer(refreshRate, (e) -> updateMemoryUsage());
        timer.start();
    }

    public void setFormat(String format) {
        if (this.format != format) {
            this.format = format;
            updateMemoryUsage();
        }
    }

    public void setRefreshRate(int refreshRate) {
        if (this.refreshRate != refreshRate) {
            this.refreshRate = refreshRate;
            timer.setDelay(refreshRate);
        }
    }

    public void start() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    public void stop() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }

    public void uninstallMemoryBar() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    protected String formatSize(long bytes) {
        int unit = 1024;

        if (bytes < unit) {
            return bytes + " B";
        }

        int exp = (int) (Math.log(bytes) / Math.log(unit));

        String pre = "KMGTPE".charAt(exp - 1) + "";

        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    private void updateMemoryUsage() {
        MemoryUsage mUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();

        setMaximum((int) mUsage.getCommitted());
        setValue((int) mUsage.getUsed());

        String max = formatSize(mUsage.getCommitted());
        String used = formatSize(mUsage.getUsed());

        value = String.format(format, used, max);
    }
}
