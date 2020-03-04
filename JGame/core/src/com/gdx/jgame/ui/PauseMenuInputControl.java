package com.gdx.jgame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.gdx.jgame.JGame;

public class PauseMenuInputControl {
	
	public static void keyJustPressed(JGame jGame) {
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
	}
	
	public static void keyMap(JGame jGame) {
		keyJustPressed(jGame);
	}
}
