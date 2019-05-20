package com.gdx.jgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class GameInputControl {
	
	public void keyPressed() {
		// TODO
	}
	
	public void keyJustPressed(JGame jGame) {
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) jGame.setMenu();
	}
	
	public void keyMap(JGame jGame) {
		keyJustPressed(jGame);
		keyPressed();		
	}
}
