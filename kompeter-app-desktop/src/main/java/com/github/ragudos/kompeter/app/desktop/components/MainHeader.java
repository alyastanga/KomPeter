package com.github.ragudos.kompeter.app.desktop.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.navigation.ParsedSceneName;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneComponent;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class MainHeader implements SceneComponent {
    private static final Logger LOGGER = KompeterLogger.getLogger(MainHeader.class);

    private final JPanel view = new JPanel();

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private final JLabel sceneTitle = new JLabel();

    public MainHeader() {
        sceneTitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "h1");
        sceneTitle.setHorizontalAlignment(JLabel.RIGHT);
    }

    private void navigationListener(@NotNull String sceneName) {
        String[] splitName = sceneName.split(ParsedSceneName.SEPARATOR);

        if (splitName.length <= 1) {
            return;
        }

        sceneTitle.setText(SceneNames.toReadable(splitName[1]));
    }

    @Override
    public void destroy() {
        if (!initialized.get()) {
            return;
        }

        SceneNavigator.getInstance().unsubscribe(this::navigationListener);
        view.removeAll();
        sceneTitle.setText("");
        initialized.set(false);
    }

    @Override
    public void initialize() {
        if (initialized.get()) {
            return;
        }

        view.setLayout(new MigLayout("", "[grow, right]", "[grow, center]"));

        view.add(sceneTitle);

        SceneNavigator.getInstance().subscribe(this::navigationListener);
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
