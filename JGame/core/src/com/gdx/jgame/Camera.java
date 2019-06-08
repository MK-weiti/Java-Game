package com.gdx.jgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.jgame.gameObjects.characters.PlainCharacter;
import com.gdx.jgame.managers.CharactersManager;
import com.gdx.jgame.managers.MapManager;

public class Camera extends OrthographicCamera{
	
	private Viewport virtualScreen;
	private Vector2 m_relative;
	private CharactersManager m_charManager;
	
	public Camera (CharactersManager characterManager){
		super(MapManager.V_WIDTH / MapManager.PIXELS_PER_METER, 
				MapManager.V_HEIGHT / MapManager.PIXELS_PER_METER);
		virtualScreen = new FitViewport(MapManager.V_WIDTH / MapManager.PIXELS_PER_METER, 
				MapManager.V_HEIGHT / MapManager.PIXELS_PER_METER, this);
		
		m_charManager = characterManager;
		m_relative = new Vector2(0, 0);
	}	
	
	public void setShift(float relativeX, float relativeY) {
		m_relative.x = relativeX;
		m_relative.y = relativeY;
	}
	
	public void setShift(Vector2 relative) {
		m_relative.x = relative.x;
		m_relative.y = relative.y;
	}
	
	public Vector2 getShift() {
		Vector2 tmp = new Vector2(m_relative.x, m_relative.y);
		return tmp;
	}
	
	public void update(SpriteBatch batch) {	
		if(m_charManager.getCameraFollower() != null) {
			virtualScreen.apply();
			batch.setProjectionMatrix(this.combined);
			position.set(m_charManager.getCameraFollower().getBody().getPosition().x - m_relative.x, 
					m_charManager.getCameraFollower().getBody().getPosition().y + m_relative.y, 0);
			update();
		}
		else {
			virtualScreen.apply();
			batch.setProjectionMatrix(this.combined);
			update();
		}
	}
	
	public void resize(int width, int height) {
		// Application, when it starts, at first call this method, so
		// any invalid arguments in OrthographicCamera constructor
		// won`t mess up.
		virtualScreen.update(width, height);
	}
	
	public Vector2 getPositionOnScreen() {
		/*
		 * Note
		 * There is visible offset when vlirtual screen proportions do not
		 * match with real screen proportion.
		 * The offset is perceptible as imprecise aiming.
		 */
		Vector3 v3 = new Vector3(m_charManager.getCameraFollower().getBody().getPosition().x,
				m_charManager.getCameraFollower().getBody().getPosition().y, 0);
		
		v3 = this.project(v3);
		Vector2 pos = new Vector2(v3.x, v3.y);
		return pos;
	}
	
	public int gutterWidth() {
		return virtualScreen.getRightGutterWidth();
	}
	
	public PlainCharacter getFollower() {
		return m_charManager.getCameraFollower();
	}
	
}
