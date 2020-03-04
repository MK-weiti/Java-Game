package com.gdx.jgame.gameObjects.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.Camera;
import com.gdx.jgame.gameObjects.missiles.Missile;
import com.gdx.jgame.movement.BasicMovements;

public class PlayerInputControl implements InputProcessor{
	
	private Camera m_camera;
	private PlainCharacter m_character;	// for input processor
	
	public void mouseInput(Camera camera, PlainCharacter character) {
		character.rotate(character.angleRadOnScreen(camera, new Vector2(Gdx.input.getX(), 
				 Gdx.input.getY()), ((float) Math.PI/2)));
	}
	
	public void keyPressed(PlainCharacter character) {
		Vector2 impulse = new Vector2();
		BasicMovements.movementV0(character, impulse);
		character.applyImpulse(impulse);
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
		Vector2 space = new Vector2(0.05f, 0.05f); 
		
		if(m_character instanceof Player) {
			Player player = (Player) m_character;
			if(button == Input.Buttons.LEFT) {
				player.spawnBullet(m_camera, space, Missile.MissileType.NormalBullet);
			}
			else if(button == Input.Buttons.RIGHT) {
				player.spawnBullet(m_camera, space, Missile.MissileType.BouncingBullet);
			}
		}
		
		return true;
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
