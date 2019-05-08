package com.gdx.jgame.gameObjects.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class PlayerInputControl {
	
	
	public void keyPressed(PlainCharacter character) {
		float impulse = character.getImpulse();
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {	
			if(-character.maxVelocity < character.body.getLinearVelocity().x) {
				if(character.body.getLinearVelocity().x - impulse < -character.maxVelocity)
					impulse = character.body.getLinearVelocity().x + character.maxVelocity;
				character.applyImpulse(-impulse, 0);
			}
		}
		impulse = character.getImpulse();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if(character.maxVelocity > character.body.getLinearVelocity().x) {
				if(character.body.getLinearVelocity().x + impulse > character.maxVelocity)
					impulse = character.maxVelocity - character.body.getLinearVelocity().x;
				character.applyImpulse(impulse, 0);
			}
		}
		impulse = character.getImpulse();
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {			
			if(-character.maxVelocity < character.body.getLinearVelocity().y) {
				if(character.body.getLinearVelocity().y - impulse < -character.maxVelocity)
					impulse = character.body.getLinearVelocity().y + character.maxVelocity;
				character.applyImpulse(0, -impulse);
			}
		}
		impulse = character.getImpulse();
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			if(character.maxVelocity > character.body.getLinearVelocity().y) {
				if(character.body.getLinearVelocity().y + impulse > character.maxVelocity)
					impulse = character.maxVelocity - character.body.getLinearVelocity().y;
				character.applyImpulse(0, impulse);
			}
		}
	}
	
	public void keyJustPressed(PlainCharacter character) {
		if(Gdx.input.isKeyJustPressed(Input.Buttons.LEFT)) {
			// TODO for example shooting
		}
	}
	
	public void keyMap(PlainCharacter character) {
		keyJustPressed(character);
		keyPressed(character);		
	}
}
