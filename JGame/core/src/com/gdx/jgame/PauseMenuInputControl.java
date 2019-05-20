package com.gdx.jgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class PauseMenuInputControl {
	
	public static void keyJustPressed(JGame jGame) {
		//if(Gdx.input.isKeyJustPressed(Input.Keys.R)) jGame.state = JGame.State.Run;
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
		//if(Gdx.input.isKeyJustPressed(Input.Keys.F5)) jGame.m_recordManager.save(name);
	}
	
	public static void keyMap(JGame jGame) {
		keyJustPressed(jGame);
	}
}
