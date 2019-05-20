package com.gdx.jgame;

import com.gdx.jgame.gameObjects.characters.PlainCharacter;
import com.gdx.jgame.gameObjects.characters.PlayerInputControl;

public class GameInputControl {
	private static UIInGameInputControl m_game = new UIInGameInputControl();
	private static PlayerInputControl m_character = new PlayerInputControl();
	
	public static void update(JGame jGame, PlainCharacter gameCharacter) {
		m_game.keyMap(jGame);
		m_character.keyMap(jGame.getCamera(), gameCharacter);
	}
}
