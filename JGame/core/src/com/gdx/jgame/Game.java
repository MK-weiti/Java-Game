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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.gameObjects.Methods;
import com.gdx.jgame.gameObjects.PalpableObjectPolygonDef;
import com.gdx.jgame.gameObjects.characters.*;
import com.gdx.jgame.jBox2D.JBoxManager;
import com.gdx.jgame.world.MapManager;

public final class Game extends ApplicationAdapter {
	private JBoxManager m_jBox;
	
	private SpriteBatch m_batch;
	private Player m_mainPlayer;
	private Enemy m_enemy;
	private Camera m_mainCamera;
	private MapManager m_map;
	private TextureManager m_charactersTextures;
	private Hud m_hud;
	
	Properties plik;
	
	@Override
	public void create () {		
		m_charactersTextures = new TextureManager("characters/player.png", "badlogic.jpg");
		m_map = new MapManager("maps/map.tmx");	
		
		plik = new Properties();
		/*try {
			FileInputStream ip = new FileInputStream("config.properties");
		} catch (FileNotFoundException e) {
			System.out.println("file null");
			e.printStackTrace();
		}*/
		
		m_mainCamera = new Camera();
		m_mainCamera.zoom = 0.7f;
		m_mainCamera.setShift(new Vector2(0, 0));
		
		m_batch = new SpriteBatch();
		m_map.setMap("map.tmx");
		m_jBox = new JBoxManager(m_map.getLayers(), m_mainCamera);
		
		createPlayer();
		m_hud = new Hud(m_batch, m_mainPlayer);
		
		createEnemy();
		
		m_mainCamera.follower = m_mainPlayer;
		
		//m_jBox.world.setContactListener(listener);
	}
	
	private void createEnemy() {
		Texture texture = m_charactersTextures.get("badlogic.jpg");
		float scale = 0.15f;
		
		CharacterPolygonDef objectDef = new CharacterPolygonDef(m_jBox.world,
				texture, Methods.setVerticesToTexture(texture, scale));
		
		playerObjectDef(scale, objectDef);
		objectDef.maxVelocity = 1f;
		objectDef.acceleration = 0.5f;
		m_enemy = new Enemy(objectDef);
		
		objectDef.dispose();
	}

	// as a test there is 2 objects of player but in future there will be only one
	private void createPlayer() {
		Texture texture = m_charactersTextures.get("player.png");
		float scale = 1f;
		
		CharacterPolygonDef objectDef = new CharacterPolygonDef(m_jBox.world,
				texture, Methods.setVerticesToTexture(texture, scale));
		
		playerObjectDef(scale, objectDef);
		objectDef.maxVelocity = 1f;
		objectDef.acceleration = 0.5f;
		m_mainPlayer = new Player(objectDef);
		
		objectDef.dispose();
	}

	private void playerObjectDef(float scale, PalpableObjectPolygonDef objectDef) {
		objectDef.bodyDef.type = BodyType.DynamicBody;
		objectDef.bodyDef.active = true;
		objectDef.bodyDef.allowSleep = false;
		objectDef.fixtureDef.friction = 0.2f;
		objectDef.scale = scale;
		objectDef.setPosition(128, 192);
	}

	@Override
	public void render () {
		InputControl.update(m_mainCamera, m_mainPlayer);
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); // transparency of part of the image
		
		m_map.render(m_mainCamera);		
		m_jBox.render(m_mainCamera);
		m_mainCamera.update(m_batch, m_mainPlayer);	
		
		m_batch.begin();
			m_enemy.render(m_batch);
			m_mainPlayer.render(m_batch);
		m_batch.end();
		
		m_batch.setProjectionMatrix(m_hud.getStage().getCamera().combined);
		m_hud.getStage().draw();
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