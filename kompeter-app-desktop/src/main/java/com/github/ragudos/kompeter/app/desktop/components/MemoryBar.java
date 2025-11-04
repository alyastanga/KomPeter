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
    private int refreshRate = 2000;
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

    public void setFormat(final String format) {
        if (this.format != format) {
            this.format = format;
            updateMemoryUsage();
        }
    }

    public void setRefreshRate(final int refreshRate) {
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

    protected String formatSize(final long bytes) {
        final int unit = 1024;

        if (bytes < unit) {
            return bytes + " B";
        }

        final int exp = (int) (Math.log(bytes) / Math.log(unit));

        final String pre = "KMGTPE".charAt(exp - 1) + "";

        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    private void updateMemoryUsage() {
        final MemoryUsage mUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();

        setMaximum((int) mUsage.getCommitted());
        setValue((int) mUsage.getUsed());

        final String max = formatSize(mUsage.getCommitted());
        final String used = formatSize(mUsage.getUsed());

        value = String.format(format, used, max);
    }
}
