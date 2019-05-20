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

import java.util.concurrent.Semaphore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.gameObjects.Methods;
import com.gdx.jgame.gameObjects.PalpableObjectPolygonDef;
import com.gdx.jgame.gameObjects.characters.*;
import com.gdx.jgame.hud.Hud;
import com.gdx.jgame.jBox2D.JBoxManager;
import com.gdx.jgame.managers.CharactersManager;
import com.gdx.jgame.managers.MapManager;
import com.gdx.jgame.managers.RecordManager;
import com.gdx.jgame.managers.ScreenManager;
import com.gdx.jgame.managers.TextureManager;
import com.gdx.jgame.managers.ScreenManager.SCREEN_STATE;
import com.gdx.jgame.ui.PauseMenu;

public final class JGame implements Screen {
	public enum GAME_STATE{
		PAUSE,
		RUN;
	}
	
	// managers
	private ScreenManager m_screenManager;	
	private CharactersManager m_characters;
	private TextureManager m_charactersTextures;
	private RecordManager m_recordManager;
	private MapManager m_map;
	
	// in game classes
	private Camera m_mainCamera;
	private SpriteBatch m_batch;
	
	// these have debug mode
	private Hud m_hud;
	private PauseMenu m_pauseMenu;
	private JBoxManager m_jBox;
	
	// these are used in debug mode
	private Semaphore m_sem;
	private DebugCommands m_debugBuffer;
	
	
	GAME_STATE gameState = GAME_STATE.PAUSE;
	private boolean gameCreated = false;
	private final boolean m_debugMode;
	private boolean m_showLayout;
	
	public boolean isGameCreated() {
		return gameCreated;
	}
	
	public JGame(ScreenManager screenManager, RecordManager recordManager, PauseMenu pauseMenu) {
		m_debugMode = screenManager.isDebugMode();
		m_recordManager = recordManager;
		m_screenManager = screenManager;
		m_pauseMenu = pauseMenu;
		m_pauseMenu.connectWithGame(this);
		create();
	}
	
	public void create () {		
		createAllReferences();
		
		if(!m_recordManager.isDataLoaded()) createNew();
		else m_recordManager.loadData(this);
		
		gameCreated = true;
		if(m_debugMode) m_debugBuffer.start();
	}	
	
	public void render () {
		if(m_debugMode) m_sem.acquireUninterruptibly();
		
		renderGame();
		
		if(m_debugMode) m_sem.release();
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
		// TODO Auto-generated method stub
	}
	
	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}
	
	public Camera getCamera() {
		return m_mainCamera;
	}
	
	public void setMenu() {
		m_screenManager.setScreen(SCREEN_STATE.PAUSE_MENU);
	}

	private void renderGame() {
		GameInputControl.update(this, m_characters.getPlayer());
		
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
	}

	@Override
	public void render(float delta) {
		render();
		
	}

	@Override
	public void hide() {
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

	SpriteBatch getBatch() {
		return m_batch;
	}

	Semaphore getRenderSemaphore() {
		return m_sem;
	}

	Camera getWorldCamera() {
		return m_mainCamera;
	}

	CharactersManager getCharacters() {
		return m_characters;
	}

	TextureManager getCharactersTextures() {
		return m_charactersTextures;
	}

	MapManager getMaps() {
		return m_map;
	}

	Hud getHud() {
		return m_hud;
	}
	
	void setHud(Hud m_hud) {
		this.m_hud = m_hud;
	}	

	PauseMenu getPauseMenu() {
		return m_pauseMenu;
	}

	JBoxManager getJBox() {
		return m_jBox;
	}

	void setJBox(JBoxManager m_jBox) {
		this.m_jBox = m_jBox;
	}	
	
	
}


