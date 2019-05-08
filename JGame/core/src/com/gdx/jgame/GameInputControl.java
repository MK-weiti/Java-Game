package com.gdx.jgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class GameInputControl {
	
	public void keyPressed() {
		// TODO
	}
	
	public void keyJustPressed() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
	}
	
	public void keyMap() {
		keyJustPressed();
		keyPressed();		
	}
}
