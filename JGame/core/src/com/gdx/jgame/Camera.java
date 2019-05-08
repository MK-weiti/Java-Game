package com.gdx.jgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.jgame.gameObjects.PalpableObject;
import com.gdx.jgame.world.MapManager;

public class Camera extends OrthographicCamera{
	
	private Viewport virtualScreen;
	
	public Camera (){
		super(MapManager.V_WIDTH / MapManager.PIXELS_PER_METER, 
				MapManager.V_HEIGHT / MapManager.PIXELS_PER_METER);
		virtualScreen = new FitViewport(MapManager.V_WIDTH / MapManager.PIXELS_PER_METER, 
				MapManager.V_HEIGHT / MapManager.PIXELS_PER_METER, this);
	}	
	
	public void update(SpriteBatch batch, PalpableObject object, float relativeX, float relativeY) {	
		batch.setProjectionMatrix(this.combined);
		position.set(object.body.getPosition().x + relativeX, object.body.getPosition().y + relativeY, 0);
		update();
	}
	
	public void update(SpriteBatch batch, PalpableObject object) {
		update(batch, object, 0, 0);
	}
	
	public void resize(int width, int height) {
		// Application, when it starts, at first call this method, so
		// any invalid arguments in OrthographicCamera constructor
		// won`t mess up.
		virtualScreen.update(width, height);
	}
	
}
