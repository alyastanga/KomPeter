/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.navigation.ParsedSceneName;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneComponent;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class MainHeader implements SceneComponent {
    private final JPanel view =
            new JPanel(new MigLayout("insets 9", "[grow, fill]", "[grow, center][]"));

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private final JLabel sceneTitle = new JLabel();
    private final RefreshLine refreshLine = new RefreshLine();

    private final Consumer<String> navigationListenerClassConsumer =
            new Consumer<String>() {
                @Override
                public void accept(String sceneName) {
                    String[] splitName = sceneName.split(ParsedSceneName.SEPARATOR);

                    if (splitName.length <= 1) {
                        return;
                    }

                    refreshLine.refresh();
                    sceneTitle.setText(SceneNames.toReadable(splitName[1]));
                }
            };

    public MainHeader() {
        sceneTitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "h1");
        sceneTitle.setHorizontalAlignment(JLabel.RIGHT);
    }

    @Override
    public void destroy() {
        if (!initialized.get()) {
            return;
        }

        SceneNavigator.getInstance().unsubscribe(navigationListenerClassConsumer);
        view.removeAll();
        sceneTitle.setText("");
        initialized.set(false);
    }

    @Override
    public void initialize() {
        if (initialized.get()) {
            return;
        }

        view.add(sceneTitle, "cell 0 0");
        view.add(refreshLine, "cell 0 1, grow, height 3!");

        SceneNavigator.getInstance().subscribe(navigationListenerClassConsumer);
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
