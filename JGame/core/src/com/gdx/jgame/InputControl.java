package com.gdx.jgame;

import com.gdx.jgame.gameObjects.characters.PlainCharacter;
import com.gdx.jgame.gameObjects.characters.PlayerInputControl;

public class InputControl {
	public static GameInputControl game = new GameInputControl();
	public static PlayerInputControl character = new PlayerInputControl();
	
	public static void update(PlainCharacter gameCharacter) {
		game.keyMap();
		character.keyMap(gameCharacter);
	}
}
