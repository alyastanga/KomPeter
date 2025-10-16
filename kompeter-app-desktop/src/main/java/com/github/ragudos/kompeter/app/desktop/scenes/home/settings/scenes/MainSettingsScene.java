/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.settings.scenes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.components.factory.ButtonFactory;
import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.utilities.HtmlUtils;

import net.miginfocom.swing.MigLayout;

public final class MainSettingsScene implements Scene {
	public static final String SCENE_NAME = "main";

	private final JPanel view = new JPanel();

	private final JButton logoutButton = ButtonFactory.createButton("Sign out", "logout.svg", "", "ghost");

	private final ActionListener logoutActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				// Authentication.signOut();
				SceneNavigator.getInstance().navigateTo(SceneNames.AuthScenes.SIGN_IN_AUTH_SCENE);
			} /*
				 * catch (AuthenticationException e1) { SwingUtilities.invokeLater(() -> {
				 * JOptionPane.showMessageDialog(view, e1.getMessage(), "Failed to logout",
				 * JOptionPane.ERROR_MESSAGE); }); }
				 */ finally {
			}
		}
	};

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
		view.setLayout(new MigLayout("insets 0, flowy, fillx, gapy 9px", "[grow]"));

		JPanel logoutContainer = new JPanel();
		JPanel textContainer = new JPanel();
		JLabel title = new JLabel(HtmlUtils.wrapInHtml("Sign out of current session"));
		JLabel subtitle = new JLabel(
				HtmlUtils.wrapInHtml("Signing out will require you to login again in the future."));

		title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h6");
		subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
		subtitle.putClientProperty(FlatClientProperties.STYLE, "font:14 italic medium;");

		logoutContainer.setLayout(new MigLayout("insets 0, flowx, gapx 4px", "[grow]push[]"));

		textContainer.setLayout(new MigLayout("insets 0, flowy, gapy 2px", "[grow]"));

		textContainer.add(title, "growx");
		textContainer.add(subtitle, "growx");

		logoutContainer.add(textContainer, "growx");
		logoutContainer.add(logoutButton);

		view.add(logoutContainer, "growx");
	}

	@Override
	public void onShow() {
		logoutButton.addActionListener(logoutActionListener);
	}

	@Override
	public void onHide() {
		logoutButton.removeActionListener(logoutActionListener);
	}

	@Override
	public void onDestroy() {
		logoutButton.removeActionListener(logoutActionListener);
		view.removeAll();
	}
}
