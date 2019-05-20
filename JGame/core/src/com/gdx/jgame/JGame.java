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

import java.util.Properties;
import java.util.concurrent.Semaphore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.ScreenManager.SCREEN_STATE;
import com.gdx.jgame.gameObjects.Methods;
import com.gdx.jgame.gameObjects.PalpableObjectPolygonDef;
import com.gdx.jgame.gameObjects.characters.*;
import com.gdx.jgame.jBox2D.JBoxManager;
import com.gdx.jgame.world.MapManager;

public final class JGame implements Screen {
	public enum GAME_STATE{
		PAUSE,
		RUN;
	}
	ScreenManager m_screenManager;
	
	Semaphore m_sem;
	DebugCommands m_debugBuffer;
	
	JBoxManager m_jBox;
	SpriteBatch m_batch;
	Camera m_mainCamera;
	Hud m_hud;
	CharactersManager m_characters;
	MapManager m_map;
	TextureManager m_charactersTextures;
	GameState m_gameState;
	PauseMenu m_pauseMenu;
	RecordManager m_recordManager;
	
	GAME_STATE gameState = GAME_STATE.PAUSE;
	private boolean gameCreated = false;
	private final boolean m_debugMode;
	private boolean m_showLayout;
	
	public boolean isGameCreated() {
		return gameCreated;
	}

	Properties plik;
	
	public JGame(ScreenManager screenManager, RecordManager recordManager, PauseMenu pauseMenu) {
		m_debugMode = screenManager.isDebugMode();
		m_recordManager = recordManager;
		m_screenManager = screenManager;
		m_pauseMenu = pauseMenu;
		m_pauseMenu.connectWithGame(this);
		create();
	}
	
	//@Override
	public void create () {		
		createAllReferences();
		
		if(!m_recordManager.isDataLoaded()) createNew();
		else m_recordManager.loadData(this);
		
		gameCreated = true;
		if(m_debugMode) m_debugBuffer.start();
	}	
	
	//@Override
	public void render () {
		if(m_debugMode) m_sem.acquireUninterruptibly();
		
		renderGame();
		/*if(state == State.Run) renderGame();
		else if(state == State.Pause) renderPauseMenu();*/
		
		if(m_debugMode) m_sem.release();
	}
	
	@Override
	public void dispose () {
		//m_pauseMenu.hide();
		m_jBox.dispose();
		m_batch.dispose();
		m_charactersTextures.dispose();
		m_hud.dispose();
		m_map.dispose();
		//m_pauseMenu.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		//m_pauseMenu.resize(width, height);
		m_mainCamera.resize(width, height);
		m_hud.resize(width, height);
		//m_pauseMenu.resize(width, height);
	}	
	
	@Override
	public void pause() {
		//m_pauseMenu.pause();
	}
	
	@Override
	public void resume() {
		//m_pauseMenu.resume();
	}
	
	public Camera getCamera() {
		return m_mainCamera;
	}
	
	public void setMenu() {
		m_screenManager.setScreen(SCREEN_STATE.PAUSE_MENU);
	}

	private void renderGame() {
		InputControl.update(this, m_characters.getPlayer());
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); // transparency of part of the image
		
		m_map.render(m_mainCamera);		
		m_jBox.render(m_mainCamera);
		m_hud.update(m_batch);
		
		m_mainCamera.update(m_batch, m_characters.getPlayer());	
		
		m_batch.begin();
			m_characters.renderAll(m_batch);
		m_batch.end();		
	}
	
	private void createAllReferences() {
		m_charactersTextures = new TextureManager("characters/player.png", "badlogic.jpg",
				"characters/honeybee.png", "characters/honeybee2.png");
		m_map = new MapManager("maps/map.tmx");	
		m_characters = new CharactersManager();
		m_mainCamera = new Camera();
		m_batch = new SpriteBatch();
		m_gameState = new GameState(this);
		//m_recordManager = new RecordManager();
		
		if(m_debugMode) {
			m_sem = new Semaphore(1);
			m_debugBuffer = new DebugCommands(this);
		}
	}

	private void createNew() {
		m_characters.addGroup("test_group");		
		
		m_mainCamera.zoom = 0.7f;
		m_mainCamera.setShift(new Vector2(0, 0));
		
		
		m_map.setMap("map.tmx");
		m_jBox = new JBoxManager(m_map.getLayers(), m_mainCamera, m_debugMode, m_showLayout);
		
		createPlayer();
		createEnemy();
		
		m_hud = new Hud(m_batch, m_characters.getPlayer(), m_debugMode, m_showLayout);
		m_mainCamera.follower = m_characters.getPlayer();
		
		//m_jBox.world.setContactListener(listener);
	}
	
	private void createEnemy() {
		/*
		 * if you copy the charDef without changing anything
		 * then it wont draw sprite
		 */
		
		String groupName = "test_group";
		Texture texture = m_charactersTextures.get("honeybee.png");
		float scale = 0.05f;
		CharacterPolygonDef objectDef = new CharacterPolygonDef(m_jBox.world,
				m_charactersTextures, "honeybee.png", scale, groupName, Methods.setVerticesToTexture(texture, scale), 
				CharacterPolygonDef.CharType.Enemy);
		
		playerObjectDef(scale, objectDef, 400, 200);
		objectDef.maxVelocity = 1f;
		objectDef.acceleration = 0.5f;
		
		m_characters.addEnemies(objectDef);
		
		CharacterPolygonDef objectDef2 = new CharacterPolygonDef(m_jBox.world,
				m_charactersTextures, "honeybee.png", scale, groupName, Methods.setVerticesToTexture(texture, scale), 
				CharacterPolygonDef.CharType.Enemy);
		
		playerObjectDef(scale, objectDef2, 500, 250);
		m_characters.addEnemies(objectDef2);
		
		CharacterPolygonDef objectDef3 = new CharacterPolygonDef(objectDef);
		
		
		playerObjectDef(scale, objectDef3, 400, 250);
		m_characters.addEnemies(objectDef3);
		
	}

	private void createPlayer() {
		Texture texture = m_charactersTextures.get("player.png");
		float scale = 1f;
		
		CharacterPolygonDef objectDef = new CharacterPolygonDef(m_jBox.world,
				m_charactersTextures, "player.png", scale, null, Methods.setVerticesToTexture(texture, scale),  CharacterPolygonDef.CharType.Player);
		
		playerObjectDef(scale, objectDef, 128, 192);
		objectDef.maxVelocity = 1f;
		objectDef.acceleration = 0.5f;
		objectDef.bodyDef.fixedRotation = true;
		m_characters.addPlayer(objectDef);
	}

	private void playerObjectDef(float textureScale, PalpableObjectPolygonDef objectDef, float posX, float posY) {
		objectDef.bodyDef.type = BodyType.DynamicBody;
		objectDef.bodyDef.active = true;
		objectDef.bodyDef.allowSleep = false;
		objectDef.bodyDef.fixedRotation = false;
		objectDef.fixtureDef.friction = 0.2f;
		objectDef.textureScale = textureScale;
		objectDef.setPosition(posX, posY);
	}

	
	@Override
	public void show() {
		gameState = GAME_STATE.RUN;
		// TODO Auto-generated method stub
	}

	@Override
	public void render(float delta) {
		render();
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		gameState = GAME_STATE.PAUSE;
	}

	public boolean isDebugMode() {
		return m_debugMode;
	}

	public boolean isShowLayout() {
		return m_showLayout;
	}

	public void setShowLayout(boolean m_showLayout) {
		this.m_showLayout = m_showLayout;
	}
	
	
}