package com.gdx.jgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdx.jgame.JGame;
import com.gdx.jgame.ScreenManager;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = GameProperties.TITLE;
		config.width = GameProperties.WIDTH;
		config.height = GameProperties.HEIGHT;
		config.foregroundFPS = GameProperties.FOREGROUND_FPS;
		config.backgroundFPS = GameProperties.BACKGROUND_FPS;		
		
		//new LwjglApplication(new JGame(), config);
		new LwjglApplication(new ScreenManager(), config);
	}
}
