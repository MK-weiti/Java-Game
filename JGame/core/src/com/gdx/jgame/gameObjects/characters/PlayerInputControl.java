package com.gdx.jgame.gameObjects.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.Camera;

public class PlayerInputControl {
	
	public void mouseInput(Camera camera, PlainCharacter character) {
		character.rotate(character.angleRadOnScreen(camera, new Vector2(Gdx.input.getX(), 
				Gdx.graphics.getHeight() - Gdx.input.getY())));
		
		if(character instanceof Player) {
			Player player = (Player) character;
			if(Gdx.input.isKeyPressed(Input.Buttons.LEFT)) {
				player.spawnNormalBullet();
			}
		}
	}
	
	public void keyPressed(PlainCharacter character) {
		float impulse = character.getImpulse();
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {	
			if(-character.getMaxVelocity() < character.getBody().getLinearVelocity().x) {
				if(character.getBody().getLinearVelocity().x - impulse < -character.getMaxVelocity())
					impulse = character.getBody().getLinearVelocity().x + character.getMaxVelocity();
				character.applyImpulse(-impulse, 0);
			}
		}
		impulse = character.getImpulse();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if(character.getMaxVelocity() > character.getBody().getLinearVelocity().x) {
				if(character.getBody().getLinearVelocity().x + impulse > character.getMaxVelocity())
					impulse = character.getMaxVelocity() - character.getBody().getLinearVelocity().x;
				character.applyImpulse(impulse, 0);
			}
		}
		impulse = character.getImpulse();
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {			
			if(-character.getMaxVelocity() < character.getBody().getLinearVelocity().y) {
				if(character.getBody().getLinearVelocity().y - impulse < -character.getMaxVelocity())
					impulse = character.getBody().getLinearVelocity().y + character.getMaxVelocity();
				character.applyImpulse(0, -impulse);
			}
		}
		impulse = character.getImpulse();
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			if(character.getMaxVelocity() > character.getBody().getLinearVelocity().y) {
				if(character.getBody().getLinearVelocity().y + impulse > character.getMaxVelocity())
					impulse = character.getMaxVelocity() - character.getBody().getLinearVelocity().y;
				character.applyImpulse(0, impulse);
			}
		}
	}
	
	public void keyJustPressed(PlainCharacter character) {
		if(Gdx.input.isKeyJustPressed(Input.Buttons.LEFT)) {
			// TODO for example shooting
		}
	}
	
	public void keyMap(Camera camera, PlainCharacter character) {
		mouseInput(camera, character);
		keyJustPressed(character);
		keyPressed(character);		
	}
}
