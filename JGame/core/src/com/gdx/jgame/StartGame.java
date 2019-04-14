package com.gdx.jgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.gdx.jgame.characters.*;

public class StartGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	private Player mainPlayer = new Player();
	
	@Override
	public void create () {		
		mainPlayer.createApperance("badlogic.jpg");
		mainPlayer.setPosition(Gdx.graphics.getWidth()/2-mainPlayer.getWidth()/2, Gdx.graphics.getHeight()/2-mainPlayer.getHeight()/2);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		InputControl.game.keyMap();
		InputControl.character.keyMap(mainPlayer, Gdx.graphics.getRawDeltaTime(), 250);
		
		mainPlayer.render();
	}
	
	
	
	@Override
	public void dispose () {
		mainPlayer.disposeApperance();
	}
	
	@Override
	public void resize(int width, int height) {
		mainPlayer.resize(width, height);
	}	
	
	@Override
	public void pause() {
		
	}
	
	@Override
	public void resume() {
		
	}	
	
}