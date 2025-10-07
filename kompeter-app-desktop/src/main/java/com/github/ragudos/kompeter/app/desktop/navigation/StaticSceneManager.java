package com.github.ragudos.kompeter.app.desktop.navigation;

import com.github.ragudos.kompeter.utilities.cache.ObserverLRU;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.awt.CardLayout;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jetbrains.annotations.NotNull;

public class StaticSceneManager implements SceneManager {
    private Logger LOGGER = KompeterLogger.getLogger(StaticSceneManager.class);

    private final HashMap<String, SceneEntry> sceneEntriesCache = new HashMap<>();
    private final ObserverLRU<String, Scene> sceneCache = new ObserverLRU<>(5);
    private String currentSceneName;

    private JPanel view = new JPanel();
    private CardLayout cardLayout = new CardLayout();

    public StaticSceneManager() {
        view.setLayout(cardLayout);

        sceneCache.subscribe(this::lruListener);
    }

    private void throwIfWrongThread() {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new WrongThreadException("Method must be called on the EDT.");
        }
    }

    private void lruListener(Scene removedScene) {
        SwingUtilities.invokeLater(() -> destroyScene(removedScene));
    }

    private Scene loadOrCreateScene(
            @NotNull final ParsedSceneName parsedSceneName, @NotNull final SceneEntry entry) {
        String parentName = parsedSceneName.parentSceneName();
        Scene parentScene = scene(parentName);

        if (parentScene == null) {
            parentScene = entry.sceneFactory().createScene();

            if (parentScene == null) {
                LOGGER.severe("Received null from factory: " + parentName);
            }

            if (!parentScene.name().equals(parentName)) {
                LOGGER.severe("Scene names do not match " + parentName + "!=" + parentScene.name() + ".");
            }

            parentScene = new SceneWrapper(parentScene);

            JPanel parentSceneView = parentScene.view();

            view.add(parentSceneView, parentName);
            cardLayout.addLayoutComponent(parentSceneView, parentName);

            // Show scene's default sub scene if it is a parent of scenes
            if (parsedSceneName.subSceneName() == null || parsedSceneName.subSceneName().isBlank()) {
                if (parentScene.supportsSubScenes()) {
                    final Scene copyOfParentScene = parentScene;

                    SwingUtilities.invokeLater(
                            () -> {
                                ((SceneWithSubScenes) copyOfParentScene.self()).navigateToDefault();
                            });
                }
            }
        }

        sceneCache.update(parentName, parentScene);

        return parentScene;
    }

    @Override
    public synchronized void registerScene(@NotNull String name, @NotNull SceneFactory factory) {
        registerScene(name, factory, () -> true);
    }

    @Override
    public synchronized void registerScene(
            @NotNull String name, @NotNull SceneFactory factory, @NotNull SceneGuard guard) {
        if (sceneEntriesCache.containsKey(name)) {
            LOGGER.warning("Trying to register an existing scene: " + name);

            return;
        }

        sceneEntriesCache.put(name, new SceneEntry(factory, guard));
        LOGGER.info("Scene registered: " + name);
    }

    @Override
    public synchronized void unregisterScene(@NotNull final String name) {
        throwIfWrongThread();

        if (!sceneEntriesCache.containsKey(name)) {
            LOGGER.warning("Trying to unregister an unexisting scene: " + name);

            return;
        }

        destroyScene(scene(name));
        sceneEntriesCache.remove(name);

        LOGGER.info("Scene unregistered: " + name);
    }
    ;

    @Override
    public synchronized void destroy() {
        throwIfWrongThread();

        sceneCache.unsubscribe(this::lruListener);

        for (var scene : sceneCache.values()) {
            destroyScene(scene);
        }

        sceneEntriesCache.clear();
        sceneCache.clear();
        currentSceneName = null;

        view.removeAll();

        LOGGER.info("Scene manager destroyed.");
    }
    ;

    @Override
    public synchronized void destroyScene(@NotNull Scene scene) {
        if (scene == null) {
            return;
        }

        throwIfWrongThread();

        JPanel sceneView = scene.view();

        if (scene instanceof SceneWithSubScenes) {
            ((SceneWithSubScenes) scene).sceneManager().destroy();
        }

        if (sceneView != null) {
            view.remove(sceneView);
        } else {
            LOGGER.warning("Scene view is null before scene destruction: " + scene.name());
        }

        sceneCache.remove(scene.name(), false);
        cardLayout.removeLayoutComponent(sceneView);
        view.remove(sceneView);
        scene.onDestroy();
    }
    ;

    @Override
    public @NotNull Scene currentScene() {
        return sceneCache.get(currentSceneName);
    }
    ;

    @Override
    public @NotNull String currentSceneName() {
        return currentSceneName;
    }
    ;

    @Override
    public @NotNull JPanel view() {
        return view;
    }
    ;

    @Override
    public @NotNull Scene scene(@NotNull final String name) {
        return sceneCache.get(name);
    }
    ;

    @Override
    public synchronized boolean navigateTo(@NotNull final String name) {
        throwIfWrongThread();

        ParsedSceneName parsedSceneName = ParsedSceneName.parse(name);

        if (!parsedSceneName.parentSceneName().equals(currentSceneName)) {
            Scene currentScene = currentScene();
            SceneEntry sceneEntry = sceneEntriesCache.get(parsedSceneName.parentSceneName());

            if (sceneEntry == null || !sceneEntry.sceneGuard().canAccess()) {
                return false;
            }

            if (currentScene != null && !currentScene.canHide()) {
                currentScene.onCannotHide();

                return false;
            }

            Scene newScene = loadOrCreateScene(parsedSceneName, sceneEntry);

            if (!newScene.canShow()) {
                newScene.onCannotShow();

                return false;
            }

            // swap
            String newSceneName = newScene.name();

            if (currentScene != null) {
                currentScene.onBeforeHide();
            }

            newScene.onBeforeShow();
            cardLayout.show(view, newSceneName);

            if (currentScene != null) {
                currentScene.onHide();
            }

            newScene.onShow();

            currentSceneName = newSceneName;
        }

        if (parsedSceneName.subSceneName() != null && !parsedSceneName.subSceneName().isBlank()) {
            // refresh just in case parent scene was swapped
            Scene currentScene = currentScene();

            if (!currentScene.supportsSubScenes()) {
                return false;
            }

            SceneWithSubScenes subScene = (SceneWithSubScenes) currentScene.self();
            SceneManager subSceneManager = subScene.sceneManager();

            if (subSceneManager != null
                    && subSceneManager.currentSceneName() != null
                    && subSceneManager.currentSceneName().equals(parsedSceneName.subSceneName())) {
                return false;
            }

            return subSceneManager.navigateTo(parsedSceneName.subSceneName());
        }

        LOGGER.info("Navigated to: " + name);
        return true;
    }
    ;
}
