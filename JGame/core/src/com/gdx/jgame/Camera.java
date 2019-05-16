package com.gdx.jgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.jgame.gameObjects.PalpableObject;
import com.gdx.jgame.world.MapManager;

public class Camera extends OrthographicCamera{
	
	private Viewport virtualScreen;
	private Vector2 m_relative;
	public PalpableObject follower;
	
	public Camera (){
		super(MapManager.V_WIDTH / MapManager.PIXELS_PER_METER, 
				MapManager.V_HEIGHT / MapManager.PIXELS_PER_METER);
		virtualScreen = new FitViewport(MapManager.V_WIDTH / MapManager.PIXELS_PER_METER, 
				MapManager.V_HEIGHT / MapManager.PIXELS_PER_METER, this);
		
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
	
	public void update(SpriteBatch batch, PalpableObject object) {	
		batch.setProjectionMatrix(this.combined);
		position.set(follower.getBody().getPosition().x - m_relative.x, 
				follower.getBody().getPosition().y + m_relative.y, 0);
		update();
	}
	
	public void resize(int width, int height) {
		// Application, when it starts, at first call this method, so
		// any invalid arguments in OrthographicCamera constructor
		// won`t mess up.
		virtualScreen.update(width, height);
	}
	
	public Vector2 getPositionOnScreen() {
		Vector3 v3 = new Vector3(follower.getBody().getPosition().x,
				follower.getBody().getPosition().y, 0);
		//System.out.println(v3);
		v3 = this.project(v3);
		
		
		/*Vector2 pos = new Vector2(Gdx.graphics.getWidth()/2 + m_relative.x * (Gdx.graphics.getWidth()/MapManager.V_WIDTH),
				Gdx.graphics.getHeight()/2 - m_relative.y * MapManager.PIXELS_PER_METER);*/
		
		Vector2 pos = new Vector2(v3.x, v3.y);
		return pos;
	}
	
}
