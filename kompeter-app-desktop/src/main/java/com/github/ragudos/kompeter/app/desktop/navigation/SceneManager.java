/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.navigation;

import java.awt.CardLayout;

import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

public interface SceneManager {
	void destroy();

	void destroyScene(@NotNull Scene scene);

	@NotNull
	Scene currentScene();

	@NotNull
	String currentSceneName();

	@NotNull
	JPanel view();

	@NotNull
	Scene scene(@NotNull final String name);

	/**
	 * This will split the scene name by {@link ParsedSceneName#SEPARATOR} and first
	 * checks if the parent scene is already being displayed by the parent
	 * {@link SceneManager}.
	 *
	 * <p>
	 * If the parent scene is already being displayed, then it checks if scene name
	 * contains a child scene name. The following steps will then be performed:
	 *
	 * <ul>
	 * <li>If the child scene name is null, then it logs a warning and returns.
	 * <li>If the child scene name is not null, then it checks if the parent scene
	 * is a {@link SubSceneSupport}
	 * <li>If the parent scene is a {@link SubSceneSupport}, then it checks if the
	 * child scene is already being displayed by the parent scene's
	 * {@link SceneManager}.
	 * <li>If the child scene is already being displayed, then it logs a warning and
	 * returns.
	 * <li>If the child scene is not already being displayed, then it delegates the
	 * navigation to the parent scene's {@link SceneManager}.
	 * </ul>
	 *
	 * <p>
	 * If the parent scene is not already being displayed, then it first navigates
	 * to the parent scene. The following steps will then be performed:
	 *
	 * <ul>
	 * <li>Get the {@link SceneEntry} of the parent scene from the
	 * {@link SceneManager}.
	 * <li>If the {@link SceneEntry} is null, then it logs a warning and returns.
	 * <li>If the {@link SceneEntry} is not null, then it checks if the scene can be
	 * accessed by a boolean returned by the {@link SceneGuard}.
	 * <li>If the scene cannot be accessed, then it logs a warning and returns.
	 * <li>Gets the currently displayed {@link Scene} and checks if it can be hidden
	 * by calling the {@link Scene#canHide()} method.
	 * <li>If the scene cannot be hidden, then it calls the current scene's
	 * {@link Scene#onCannotHide()} method and returns.
	 * <li>Loads or creates the scene. The following steps are performed:
	 * <ul>
	 * <li>Get the {@link Scene} from cache using the parent scene name.
	 * <li>If the {@link Scene} is not null, then it returns it.
	 * <li>Otherwise, the following steps are performed:
	 * <ul>
	 * <li>Gets the {@link SceneFactory} of the parent scene from the
	 * {@link SceneEntry}.
	 * <li>If the name of the created {@link Scene} is not equal to the name we're
	 * trying to navigate to, then it logs a severe message while still continuing
	 * the operation.
	 * <li>Wraps the {@link Scene} in a {@link SceneWrapper}
	 * <li>Calls the {@link Scene#onCreate()} method.
	 * <li>Retrieves the view from the {@link Scene} and adds it to the root
	 * container and {@link CardLayout} manager.
	 * <li>If the {@link Scene} is a {@link SubSceneSupport}, then it calls the
	 * {@link SubSceneSupport#getDefaultScene()} method.
	 * <li>Updates the cache with the {@link Scene} using the parent scene name.
	 * </ul>
	 * </ul>
	 * <li>If the scene cannot be shown, then it calls the
	 * {@link Scene#onCannotShow()} method and returns.
	 * <li>Finally, it switches the old scene with the new scene and calling their
	 * respective hook lifecycle methods.
	 * <li>Lastly, if the parent scene is a {@link SubSceneSupport}, it tries to
	 * navigate to the child scene and repeats the same process as above.
	 * </ul>
	 *
	 * @param sceneName
	 */
	boolean navigateTo(@NotNull final ParsedSceneName parsedSceneName);

	void registerScene(@NotNull final String name, @NotNull final SceneFactory factory);

	void registerScene(@NotNull final String name, @NotNull final SceneFactory factory,
			@NotNull final SceneGuard guard);

	void unregisterScene(@NotNull final String name);
}
