/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import com.github.ragudos.kompeter.utilities.observer.Observer;

public class SceneNavigator implements Observer<String> {
	private static final Logger LOGGER = KompeterLogger.getLogger(SceneNavigator.class);
	private static SceneNavigator instance;

	public static synchronized SceneNavigator getInstance() {
		if (instance == null) {
			instance = new SceneNavigator();
		}

		return instance;
	}

	private List<Consumer<String>> subscribers = new ArrayList<>();
	private String currentFullSceneName;
	private SceneManager sceneManager;
	private AtomicBoolean initialized = new AtomicBoolean(false);

	private SceneNavigator() {
	}

	public void destroy() {
		sceneManager.destroy();
		sceneManager = null;
		initialized.set(false);
	}

	public void destroyScene(@NotNull final String name) {
		if (!initialized.get()) {
			throw new IllegalStateException("SceneNavigator is not yet initialized.");
		}

		SwingUtilities.invokeLater(() -> {
			sceneManager.destroyScene(sceneManager.scene(name));
		});
	}

	public String currentFullSceneName() {
		return currentFullSceneName;
	}

	public SceneManager sceneManager() {
		if (!initialized.get()) {
			throw new IllegalStateException("SceneNavigator is not yet initialized.");
		}

		return sceneManager;
	}

	public void initialize(@NotNull final SceneManager manager) {
		if (initialized.get()) {
			throw new IllegalStateException("SceneNavigator is already initialized");
		}

		sceneManager = manager;
		initialized.set(true);
	}

	/**
	 * This method is useful to globally navigate to a scene. It will throw an
	 * exception if the scene is not registered or if the scene is not a valid
	 * scene.
	 *
	 * <p>
	 * The <code>sceneName</code> has the format of
	 * <code>[parentSceneName]/[subSceneName]/...
	 * </code> and the scene name must be registered in the {@link SceneManager}.
	 * Each [parentSceneName]'s respective scene must handle the [subSceneName] in
	 * its own {@link SceneManager}.
	 *
	 * @param sceneName The name of the scene to navigate to.
	 * @throws IllegalArgumentException If the scene name is invalid.
	 * @throws WrongThreadException     If this method is called from a thread other
	 *                                  than the Event Dispatch Thread.
	 */
	public void navigateTo(@NotNull final String sceneName) {
		if (!initialized.get()) {
			throw new IllegalStateException("SceneNavigator is not initialized.");
		}

		SwingUtilities.invokeLater(() -> {
			ParsedSceneName parsedSceneName = new ParsedSceneName(sceneName);

			if (!sceneManager.navigateTo(parsedSceneName)) {
				return;
			}

			LOGGER.info("Navigated to scene: " + parsedSceneName.fullPath());

			synchronized (parsedSceneName) {
				currentFullSceneName = parsedSceneName.fullPath();
				notifySubscribers(parsedSceneName.fullPath());
			}
		});
	}

	@Override
	public void notifySubscribers(String value) {
		subscribers.forEach((subscriber) -> subscriber.accept(value));
	}

	@Override
	public void subscribe(Consumer<String> subscriber) {
		subscribers.add(subscriber);
	}

	@Override
	public void unsubscribe(Consumer<String> subscriber) {
		subscribers.remove(subscriber);
	}
}
