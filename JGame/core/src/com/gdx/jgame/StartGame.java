/*
 * cannot load badimage.jpg:
 * run -> run configuration -> arguments -> other -> assets in core
 * 
 * problem with tests file
 * Game-core -> properties -> source -> add file -> add needed files
 * 
 * warnings:
 * properties -> java build path -> libraries -> edit jre to java-oracle11
 * 
 * compile:
 * go to the root of JGame (where is gradlew) and in terminal run ./gradlew desktop:dist
 * the result is in desctop/build/libs
 * 
 */


package com.gdx.jgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.gdx.jgame.gameObjects.characters.*;
import com.gdx.jgame.jBox2D.JBoxManager;
import com.gdx.jgame.world.MapManager;

public class StartGame extends ApplicationAdapter {
	private JBoxManager m_jBox;
	
	private SpriteBatch m_batch;
	private Player m_mainPlayer, m_secondPlayer;
	private Camera m_mainCamera;
	private MapManager m_map;
	private TextureManager m_charactersTextures;
	private Hud m_hud;
	
	@Override
	public void create () {		
		m_charactersTextures = new TextureManager("characters/player.png", "badlogic.jpg");
		m_map = new MapManager("maps/map.tmx");	
		
		m_mainCamera = new Camera();
		m_mainCamera.zoom = 0.7f;
		
		m_batch = new SpriteBatch();
		m_map.setMap("map.tmx");
		m_jBox = new JBoxManager(m_map.getLayers(), m_mainCamera);
		m_hud = new Hud(m_batch);
		
		m_mainPlayer = new Player(m_charactersTextures.get("player.png"), m_jBox.world, 128, 192, 1, 0.5f, 1);
		m_secondPlayer = new Player(m_charactersTextures.get("badlogic.jpg"), m_jBox.world, 400, 50, 0.3f, 3, 50);
	}

	@Override
	public void render () {
		InputControl.update(m_mainPlayer);
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); // transparency of part of the image
		
		m_map.render(m_mainCamera);		
		m_jBox.render(m_mainCamera);
		m_mainCamera.update(m_batch, m_mainPlayer);	
		
		m_batch.begin();
			m_secondPlayer.render(m_batch);
			m_mainPlayer.render(m_batch);
		m_batch.end();
		
		m_batch.setProjectionMatrix(m_hud.stage.getCamera().combined);
		m_hud.stage.draw();
	}
	
	@Override
	public void dispose () {
		m_jBox.dispose();
		m_batch.dispose();
		m_charactersTextures.dispose();
		m_hud.dispose();
		m_map.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		m_mainCamera.resize(width, height);
		m_hud.resize(width, height);
	}	
	
	@Override
	public void pause() {
		 // TODO
	}
	
	@Override
	public void resume() {
		// TODO
	}
	
}