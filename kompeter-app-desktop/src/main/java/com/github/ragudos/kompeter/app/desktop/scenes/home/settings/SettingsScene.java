/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.settings;

import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.app.desktop.navigation.ParsedSceneName;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
import com.github.ragudos.kompeter.app.desktop.scenes.home.settings.scenes.MainSettingsScene;

import net.miginfocom.swing.MigLayout;

public final class SettingsScene implements SceneWithSubScenes {
	public static final String SCENE_NAME = "settings";

	private final JPanel view = new JPanel();

	private final SceneManager sceneManager = new StaticSceneManager();

	@Override
	public @NotNull String name() {
		return SCENE_NAME;
	}

	@Override
	public @NotNull JPanel view() {
		return view;
	}

	@Override
	public void onCreate() {
		view.setLayout(new MigLayout("insets 0", "[grow]", "[grow]"));

		view.add(sceneManager.view(), "grow");

		sceneManager.registerScene(MainSettingsScene.SCENE_NAME, () -> new MainSettingsScene());
	}

	@Override
	public boolean navigateTo(@NotNull ParsedSceneName parsedSceneName) {
		return sceneManager.navigateTo(parsedSceneName);
	}

	@Override
	public String getDefaultScene() {
		return MainSettingsScene.SCENE_NAME;
	}

	@Override
	public SceneManager sceneManager() {
		return sceneManager;
	}
}
