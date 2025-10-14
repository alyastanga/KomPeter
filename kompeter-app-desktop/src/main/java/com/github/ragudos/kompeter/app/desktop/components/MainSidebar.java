package com.github.ragudos.kompeter.app.desktop.components;

import com.github.ragudos.kompeter.app.desktop.navigation.SceneComponent;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public class MainSidebar implements SceneComponent {
    private static final Logger LOGGER = KompeterLogger.getLogger(MainSidebar.class);

    private final JPanel view;

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public MainSidebar() {
        view = new JPanel();
    }

    @Override
    public void destroy() {
        if (!initialized.get()) {
            return;
        }
    }

    @Override
    public void initialize() {
        if (initialized.get()) {
            return;
        }

        initialized.set(true);
    }

    @Override
    public boolean isInitialized() {
        return initialized.get();
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }
}
