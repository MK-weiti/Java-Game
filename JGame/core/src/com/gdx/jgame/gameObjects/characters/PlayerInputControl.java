package com.gdx.jgame.gameObjects.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.Camera;
import com.gdx.jgame.gameObjects.missiles.Missile;

public class PlayerInputControl implements InputProcessor{
	
	private Camera m_camera;
	private PlainCharacter m_character;	// for input processor
	
	public void mouseInput(Camera camera, PlainCharacter character) {
		character.rotate(character.angleRadOnScreen(camera, new Vector2(Gdx.input.getX(), 
				Gdx.graphics.getHeight() - Gdx.input.getY())));
	}
	
	public void keyPressed(PlainCharacter character) {
		float impulse = character.getImpulse();
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {	
			if(-character.getMaxLinearSpeed() < character.getBody().getLinearVelocity().x) {
				if(character.getBody().getLinearVelocity().x - impulse < -character.getMaxLinearSpeed())
					impulse = character.getBody().getLinearVelocity().x + character.getMaxLinearSpeed();
				character.applyImpulse(-impulse, 0);
			}
		}
		impulse = character.getImpulse();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if(character.getMaxLinearSpeed() > character.getBody().getLinearVelocity().x) {
				if(character.getBody().getLinearVelocity().x + impulse > character.getMaxLinearSpeed())
					impulse = character.getMaxLinearSpeed() - character.getBody().getLinearVelocity().x;
				character.applyImpulse(impulse, 0);
			}
		}
		impulse = character.getImpulse();
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {			
			if(-character.getMaxLinearSpeed() < character.getBody().getLinearVelocity().y) {
				if(character.getBody().getLinearVelocity().y - impulse < -character.getMaxLinearSpeed())
					impulse = character.getBody().getLinearVelocity().y + character.getMaxLinearSpeed();
				character.applyImpulse(0, -impulse);
			}
		}
		impulse = character.getImpulse();
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			if(character.getMaxLinearSpeed() > character.getBody().getLinearVelocity().y) {
				if(character.getBody().getLinearVelocity().y + impulse > character.getMaxLinearSpeed())
					impulse = character.getMaxLinearSpeed() - character.getBody().getLinearVelocity().y;
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
		m_camera = camera;
		m_character = character;
		
		if(character != null) {
			mouseInput(camera, character);
			keyJustPressed(character);
			keyPressed(character);	
		}
			
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		if(m_character instanceof Player) {
			Player player = (Player) m_character;
			if(button == Input.Buttons.LEFT) {
				player.spawnBullet(m_camera, new Vector2(0.05f, 0.05f), Missile.MissileType.NormalBullet);
			}
			else if(button == Input.Buttons.RIGHT) {
				player.spawnBullet(m_camera, new Vector2(0.05f, 0.05f), Missile.MissileType.BouncingBullet);
			}
		}
		
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
