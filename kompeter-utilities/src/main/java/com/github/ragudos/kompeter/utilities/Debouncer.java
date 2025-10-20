package com.github.ragudos.kompeter.utilities;

import javax.swing.Timer;

/**
 * Debouncer utility for Swing-based actions. Useful for limiting how often a
 * given Runnable is triggered, such as in response to button clicks or key
 * events.
 */
public class Debouncer {
    private final int delayMillis;
    private Timer timer;

    /**
     * Creates a new Debouncer with the specified delay.
     *
     * @param delayMillis Delay in milliseconds before the action is triggered.
     */
    public Debouncer(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    /**
     * Schedules the action to run after the debounce delay. If called again before
     * the delay elapses, the timer is reset.
     *
     * @param action Action to run after the delay.
     */
    public void call(Runnable action) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        timer = new Timer(delayMillis, v -> {
            timer = null; // Clean up
            action.run();
        });

        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Cancels any pending debounced action.
     */
    public void cancel() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
            timer = null;
        }
    }
}
