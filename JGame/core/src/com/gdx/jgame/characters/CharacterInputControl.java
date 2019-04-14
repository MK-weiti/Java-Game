package com.gdx.jgame.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.gdx.jgame.characters.Player;

public class CharacterInputControl {
	
	
	public void keyPressed(CharacterAdapter player, float deltaTime, float speed) {
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			if(player.getPosX()>0) player.move(-deltaTime*speed, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if(Gdx.graphics.getWidth()-player.getWidth()>player.getPosX()) player.move(deltaTime*speed, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			if(player.getPosY()>0) player.move(0, -deltaTime*speed);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			if(Gdx.graphics.getHeight()-player.getHeight()>player.getPosY()) player.move(0, deltaTime*speed);
		}
	}
	
	public void keyJustPressed(CharacterAdapter player, float deltaTime, float speed) {
		
	}
	
	public void keyMap(CharacterAdapter player, float deltaTime, float speed) {
		keyJustPressed(player, deltaTime, speed);
		keyPressed(player, deltaTime, speed);		
	}
}
