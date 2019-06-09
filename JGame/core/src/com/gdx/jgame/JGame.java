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
import com.gdx.jgame.gameObjects.characters.BasicEnemy;
import com.gdx.jgame.gameObjects.characters.BasicEnemyDef;
import com.gdx.jgame.gameObjects.characters.CharacterPolygonDef;
import com.gdx.jgame.gameObjects.characters.Player;
import com.gdx.jgame.gameObjects.characters.PlayerDef;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;
import com.gdx.jgame.gameObjects.missiles.def.BouncingBulletDef;
import com.gdx.jgame.gameObjects.missiles.def.NormalBulletDef;
import com.gdx.jgame.hud.Hud;
import com.gdx.jgame.jBox2D.JBoxObjects;
import com.gdx.jgame.logic.ai.FollowerEntity;
import com.gdx.jgame.managers.CharactersManager;
import com.gdx.jgame.managers.MapManager;
import com.gdx.jgame.managers.SavesManager;
import com.gdx.jgame.managers.ScreenManager;
import com.gdx.jgame.managers.TextureManager;
import com.gdx.jgame.managers.ScreenManager.SCREEN_STATE;
import com.gdx.jgame.ui.PauseMenu;

public final class JGame implements Screen {
	public enum GAME_STATE{
		PAUSE,
		RUN;
	}
	
	public enum GAME_LEVEL_STATE{
		UNKNOW,
		WIN,
		LOSE;
	}
	
	// managers
	private ScreenManager m_screenManager;	
	private CharactersManager m_characters;
	private TextureManager m_charactersTextures;
	private TextureManager m_bulletsTextures;
	private SavesManager m_recordManager;
	private MapManager m_map;
	private MissilesManager m_missleManager;
	
	// in game classes
	private Camera m_mainCamera;
	private SpriteBatch m_batch;
	
	// these have debug mode
	private Hud m_hud;
	private PauseMenu m_pauseMenu;
	private JBoxObjects m_jBox;
	
	// these are used in debug mode
	private Semaphore m_stopRender;
	private DebugCommands m_debugBuffer;
	
	
	GAME_STATE gameState = GAME_STATE.PAUSE;
	private GAME_LEVEL_STATE gameLevelState = GAME_LEVEL_STATE.UNKNOW;
	private boolean gameCreated = false;
	private final boolean m_debugMode;
	private boolean m_showLayout;
	
	
	private int enemyID;
	
	public boolean isGameCreated() {
		return gameCreated;
	}
	
	public JGame(ScreenManager screenManager, SavesManager savesManager, PauseMenu pauseMenu, MapManager map) {
		m_debugMode = screenManager.isDebugMode();
		m_showLayout = screenManager.isShowLayout();
		m_recordManager = savesManager;
		m_screenManager = screenManager;
		m_pauseMenu = pauseMenu;
		m_pauseMenu.connectWithGame(this);
		m_map = map;
		create();
	}
	
	public void create () {		
		createAllReferences();
		
		if(!m_recordManager.isDataLoaded()) createNewGame();
		else m_recordManager.loadData(this);
		
		gameCreated = true;
		if(m_debugMode) m_debugBuffer.start();
	}	
	
	public void render () {
		if(m_debugMode) m_stopRender.acquireUninterruptibly();
		
		renderGame();
		
		if(gameLevelState == GAME_LEVEL_STATE.WIN) {
			m_screenManager.setM_gameState(GAME_LEVEL_STATE.WIN);
			m_screenManager.nextMap();
		}
		else if(gameLevelState == GAME_LEVEL_STATE.LOSE) {
			m_screenManager.setM_gameState(GAME_LEVEL_STATE.LOSE);
			System.out.println("You lose the game!");
			this.setMenu();
		}
		
		if(m_debugMode) m_stopRender.release();
	}
	
	@Override
	public void dispose () {
		m_jBox.dispose();
		m_batch.dispose();
		m_charactersTextures.dispose();
		m_bulletsTextures.dispose();
		m_hud.dispose();
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
		m_jBox.removeBodies();
		GameInputControl.update(this, m_characters.getPlayer());
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); // transparency of part of the image
		
		m_map.render(m_mainCamera);		
		m_jBox.render(m_mainCamera);
		m_hud.update(m_batch);
		
		m_mainCamera.update(m_batch);	
		
		m_batch.begin();
			m_missleManager.render(m_batch);
			m_characters.renderAll(m_batch);
		m_batch.end();		
	}
	
	private void createAllReferences() {
		m_charactersTextures = new TextureManager("characters/player.png", "badlogic.jpg",
				"characters/honeybee.png", "characters/honeybee2.png");
		m_bulletsTextures = new TextureManager("bullets/normalBullet.png", "bullets/rocket.png");	
		m_missleManager = new MissilesManager();
		m_characters = new CharactersManager(m_missleManager);
		m_mainCamera = new Camera(m_characters);
		m_batch = new SpriteBatch();
		
		if(m_debugMode) {
			m_stopRender = new Semaphore(1);
			m_debugBuffer = new DebugCommands(this);
		}
	}

	private void createNewGame() {
		m_characters.addGroup("test_group");		
		
		m_mainCamera.zoom = 0.7f;
		m_mainCamera.setShift(new Vector2(0, 0));
		
		m_jBox = new JBoxObjects(this, m_map.getLayers(), m_mainCamera, m_debugMode, m_showLayout);
		
		createPlayer();
		createEnemy();
		setAI();
		
		m_hud = new Hud(m_batch, m_characters.getPlayer(), m_debugMode, m_showLayout);
		m_characters.setCameraFollower(m_characters.getPlayer());
	}
	
	private void createEnemy() {		
		String groupName = "test_group";
		Texture texture = m_charactersTextures.get("honeybee.png");
		float scale = 0.05f;
		BasicEnemyDef objectDef = new BasicEnemyDef(m_jBox.getWorld(), this.getCharactersTextures(),
				this.getBulletsTextures(), this.getMisslesManager(), "honeybee.png", scale, groupName, 
				Utils.setVerticesToTexture(texture, scale));
		
		playerObjectDef(scale, objectDef, 400, 200);
		objectDef.maxVelocity = 1f;
		objectDef.acceleration = 0.5f;
		objectDef.armory.normalBulletDef = createBulletDef(m_bulletsTextures);
		
		enemyID = m_characters.addEnemies(objectDef).first();
		
		BasicEnemyDef objectDef2 = new BasicEnemyDef(m_jBox.getWorld(), this.getCharactersTextures(),
				this.getBulletsTextures(), this.getMisslesManager(),
				"honeybee.png", scale, groupName, Utils.setVerticesToTexture(texture, scale));
		
		objectDef2.maxVelocity = 1f;
		objectDef2.acceleration = 0.5f;
		
		playerObjectDef(scale, objectDef2, 500, 250);
		m_characters.addEnemies(objectDef2);
		m_characters.addEnemies(objectDef2);
		
		BasicEnemyDef objectDef3 = new BasicEnemyDef(objectDef);
		
		
		playerObjectDef(scale, objectDef3, 400, 250);
		m_characters.addEnemies(objectDef3).first();
		
	}

	private void createPlayer() {
		Texture texture = m_charactersTextures.get("player.png");
		float scale = 1f;
		
		PlayerDef objectDef = new PlayerDef(m_jBox.getWorld(), m_charactersTextures, m_bulletsTextures, m_missleManager, 
				"player.png", scale, null, Utils.setVerticesToTexture(texture, scale));
		
		playerObjectDef(scale, objectDef, 128, 192);
		objectDef.maxVelocity = 1f;
		objectDef.acceleration = 0.5f;
		objectDef.bodyDef.fixedRotation = true;
		objectDef.armory.currentAmmoBouncingBullet = 5;
		objectDef.armory.currentAmmoNormalBullet = 10;
		
		objectDef.armory.bouncingBulletDef = createmBouncingBulletDef(m_bulletsTextures);
		objectDef.armory.normalBulletDef = createBulletDef(m_bulletsTextures);
		
		m_characters.addPlayer(objectDef, m_bulletsTextures);
	}

	private void playerObjectDef(float textureScale, CharacterPolygonDef objectDef, float posX, float posY) {
		objectDef.bodyDef.type = BodyType.DynamicBody;
		objectDef.bodyDef.active = true;
		objectDef.bodyDef.allowSleep = false;
		objectDef.bodyDef.fixedRotation = false;
		objectDef.fixtureDef.friction = 0.2f;
		objectDef.textureScale = textureScale;
		objectDef.setPosition(posX, posY);
	}
	
	private NormalBulletDef createBulletDef(TextureManager txBulletManager) {
		NormalBulletDef normalBulletDef;
		normalBulletDef = new NormalBulletDef(m_jBox.getWorld(), txBulletManager, "rocket.png", 
				0.05f, Utils.setVerticesToTexture(txBulletManager.get("rocket.png"), 0.01f));
		normalBulletDef.damage = -2;
		normalBulletDef.maxVelocity = 40f;
		normalBulletDef.acceleration = 5f;
		normalBulletDef.fixtureDef.density = 2f;
		normalBulletDef.bodyDef.type = BodyType.DynamicBody;
		normalBulletDef.bodyDef.fixedRotation = false;
		normalBulletDef.fixtureDef.friction = 0f;
		normalBulletDef.fixtureDef.restitution = 1f;
		return normalBulletDef;
	}
	
	private BouncingBulletDef createmBouncingBulletDef(TextureManager txBulletManager) {
		BouncingBulletDef bouncingBulletDef;
		bouncingBulletDef = new BouncingBulletDef(m_jBox.getWorld(), txBulletManager, "normalBullet.png", 
				0.05f, Utils.setVerticesToTexture(txBulletManager.get("normalBullet.png"), 0.01f));
		bouncingBulletDef.numberOfBounces = 3;
		bouncingBulletDef.damage = -2;
		bouncingBulletDef.maxVelocity = 40f;
		bouncingBulletDef.acceleration = 5f;
		bouncingBulletDef.fixtureDef.density = 2f;
		bouncingBulletDef.bodyDef.type = BodyType.DynamicBody;
		bouncingBulletDef.bodyDef.fixedRotation = false;
		bouncingBulletDef.fixtureDef.friction = 0f;
		bouncingBulletDef.fixtureDef.restitution = 1f;
		
		return bouncingBulletDef;
	}
	
	private void setAI() {
		FollowerEntity tmp = new FollowerEntity(m_characters.getEnemy("test_group", enemyID), m_characters.getPlayer());
		tmp.getArrive().setTimeToTarget(1f)
		.setArrivalTolerance(0.5f)
		.setDecelerationRadius(0.01f);
		
		tmp.getFace().setTimeToTarget(1f)
		.setDecelerationRadius(0.01f)
		.setAlignTolerance(0.01f);
		
		tmp.setBehavior();
		
		m_characters.getEnemy("test_group", enemyID).setMaxAngularSpeed(2f);
		m_characters.getEnemy("test_group", enemyID).setMaxAngularAcceleration(3f);
		m_characters.getEnemy("test_group", enemyID).setMaxLinearSpeed(1f);
		m_characters.getEnemy("test_group", enemyID).setMaxLinearAcceleration(3f);
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
		return m_stopRender;
	}

	Camera getWorldCamera() {
		return m_mainCamera;
	}

	CharactersManager getCharacters() {
		return m_characters;
	}

	public TextureManager getCharactersTextures() {
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

	JBoxObjects getJBox() {
		return m_jBox;
	}

	void setJBox(JBoxObjects m_jBox) {
		this.m_jBox = m_jBox;
	}

	public GAME_LEVEL_STATE getGameLevelState() {
		return gameLevelState;
	}

	public void setGameLevelState(GAME_LEVEL_STATE gameLevelState) {
		this.gameLevelState = gameLevelState;
	}

	public TextureManager getBulletsTextures() {
		return m_bulletsTextures;
	}	
	
	public MissilesManager getMisslesManager() {
		return m_missleManager;
	}

	public CharactersManager getCharactersManager() {
		return m_characters;
	}

	public MissilesManager getM_missleManager() {
		return m_missleManager;
	}

	public JBoxObjects getjBox() {
		return m_jBox;
	}	
	
}


