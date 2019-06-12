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
import com.gdx.jgame.gameObjects.DefaultDef;
import com.gdx.jgame.gameObjects.characters.BasicEnemyDef;
import com.gdx.jgame.gameObjects.characters.PlayerDef;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;
import com.gdx.jgame.gameObjects.missiles.def.BouncingBulletDef;
import com.gdx.jgame.gameObjects.missiles.def.NormalBulletDef;
import com.gdx.jgame.hud.Hud;
import com.gdx.jgame.jBox2D.JBoxObjects;
import com.gdx.jgame.logic.ai.AIManager;
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
	private ScreenManager screenManager;	
	private CharactersManager characters;
	private TextureManager charactersTextures;
	private TextureManager bulletsTextures;
	private SavesManager recordManager;
	private MapManager map;
	private MissilesManager missleManager;
	private AIManager managerAI;
	
	// in game classes
	private Camera mainCamera;
	private SpriteBatch batch;
	
	// these have debug mode
	private Hud hud;
	private PauseMenu pauseMenu;
	private JBoxObjects jBox;
	
	// these are used in debug mode
	private Semaphore stopRender;
	private DebugCommands debugBuffer;
	
	
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
		this.m_debugMode = screenManager.isDebugMode();
		this.m_showLayout = screenManager.isShowLayout();
		this.recordManager = savesManager;
		this.screenManager = screenManager;
		this.pauseMenu = pauseMenu;
		this.pauseMenu.connectWithGame(this);
		this.map = map;
		create();
	}
	
	public void create () {		
		createAllIndependentReferences();
		
		if(!recordManager.isDataLoaded()) createNewGame();
		else recordManager.loadData(this);
		
		gameCreated = true;
		if(m_debugMode) debugBuffer.start();
	}	
	
	public void render () {
		if(m_debugMode) stopRender.acquireUninterruptibly();
		
		renderGame();
		
		if(gameLevelState == GAME_LEVEL_STATE.WIN) {
			screenManager.setGameState(GAME_LEVEL_STATE.WIN);
			screenManager.nextMap();
		}
		else if(gameLevelState == GAME_LEVEL_STATE.LOSE) {
			screenManager.setGameState(GAME_LEVEL_STATE.LOSE);
			System.out.println("You lose the game!");
			this.setMenu();
		}
		
		if(m_debugMode) stopRender.release();
	}
	

	private void renderGame() {
		jBox.removeBodies();
		GameInputControl.update(this, characters.getPlayer());
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); // transparency of part of the image
		
		map.render(mainCamera);		
		jBox.render(mainCamera);
		hud.update(batch);
		
		mainCamera.update(batch);	
		
		batch.begin();
			missleManager.render(batch);
			characters.renderAll(batch);
		batch.end();		
	}
	
	private void createAllIndependentReferences() {
		charactersTextures = new TextureManager("characters/player.png", "badlogic.jpg",
				"characters/honeybee.png", "characters/honeybee2.png");
		bulletsTextures = new TextureManager("bullets/normalBullet.png", "bullets/rocket.png");	
		missleManager = new MissilesManager();
		characters = new CharactersManager(missleManager);
		mainCamera = new Camera(characters);
		batch = new SpriteBatch();
		
		if(m_debugMode) {
			stopRender = new Semaphore(1);
			debugBuffer = new DebugCommands(this);
		}
	}

	private void createNewGame() {
		managerAI = new AIManager();
		
		characters.addGroup("test_group");		
		
		mainCamera.zoom = 0.7f;
		mainCamera.setShift(new Vector2(0, 0));
		
		jBox = new JBoxObjects(this, map.getLayers(), mainCamera, m_debugMode, m_showLayout);
		
		createPlayer();
		createEnemy();
		setAI();
		
		hud = new Hud(batch, characters.getPlayer(), m_debugMode, m_showLayout);
		characters.setCameraFollower(characters.getPlayer());
	}
	
	private void createEnemy() {		
		String groupName = "test_group";
		Texture texture = charactersTextures.get("honeybee.png");
		float scale = 0.05f;
		BasicEnemyDef objectDef = new BasicEnemyDef(jBox.getWorld(), this.getCharactersTextures(),
				this.getBulletsTextures(), this.getMisslesManager(), "honeybee.png", scale, groupName, 
				Utils.setVerticesToTexture(texture, scale));
		
		DefaultDef.defBasicEnemy(objectDef);
		objectDef.bodyDef.fixedRotation = false;
		objectDef.setPosition(400, 200);
		objectDef.textureScale = scale;
		
		objectDef.armory.normalBulletDef = createBulletDef(bulletsTextures);
		objectDef.armory.normalBulletDef = createBulletDef(bulletsTextures);
		
		enemyID = characters.addEnemies(objectDef).first();
		
		BasicEnemyDef objectDef2 = new BasicEnemyDef(jBox.getWorld(), this.getCharactersTextures(),
				this.getBulletsTextures(), this.getMisslesManager(),
				"honeybee.png", scale, groupName, Utils.setVerticesToTexture(texture, scale));
		
		
		DefaultDef.defBasicEnemy(objectDef2);
		objectDef2.acceleration = 0.5f;
		objectDef2.bodyDef.fixedRotation = false;
		objectDef2.setPosition(500, 250);
		objectDef2.textureScale = scale;
		
		
		characters.addEnemies(objectDef2);
		characters.addEnemies(objectDef2);
		
		BasicEnemyDef objectDef3 = new BasicEnemyDef(objectDef);
		
		//objectDef3.armory.normalBulletDef = null;
		DefaultDef.defBasicEnemy(objectDef3);
		objectDef3.bodyDef.fixedRotation = false;
		objectDef3.setPosition(400, 250);
		objectDef3.textureScale = scale;
		
		characters.addEnemies(objectDef3).first();
		
	}

	private void createPlayer() {
		Texture texture = charactersTextures.get("player.png");
		float scale = 1f;
		
		PlayerDef objectDef = new PlayerDef(jBox.getWorld(), charactersTextures, bulletsTextures, missleManager, 
				"player.png", scale, null, Utils.setVerticesToTexture(texture, scale));
		
		DefaultDef.defPlayer(objectDef);
		objectDef.bodyDef.fixedRotation = true;
		objectDef.setPosition(128, 192);
		objectDef.textureScale = scale;
		
		objectDef.armory.bouncingBulletDef = createmBouncingBulletDef(bulletsTextures);
		objectDef.armory.normalBulletDef = createBulletDef(bulletsTextures);
		objectDef.charGroupName = "";
		
		characters.addPlayer(objectDef, bulletsTextures);
	}
	
	private NormalBulletDef createBulletDef(TextureManager txBulletManager) {
		NormalBulletDef normalBulletDef;
		normalBulletDef = new NormalBulletDef(jBox.getWorld(), txBulletManager, "rocket.png", 
				0.05f, Utils.setVerticesToTexture(txBulletManager.get("rocket.png"), 0.01f));
		DefaultDef.defNormalBullet(normalBulletDef);
		normalBulletDef.damage = -2;
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
		bouncingBulletDef = new BouncingBulletDef(jBox.getWorld(), txBulletManager, "normalBullet.png", 
				0.05f, Utils.setVerticesToTexture(txBulletManager.get("normalBullet.png"), 0.01f));
		DefaultDef.defBouncingBullet(bouncingBulletDef);
		bouncingBulletDef.numberOfBounces = 3;
		bouncingBulletDef.damage = -2;
		bouncingBulletDef.acceleration = 5f;
		bouncingBulletDef.fixtureDef.density = 2f;
		bouncingBulletDef.bodyDef.type = BodyType.DynamicBody;
		bouncingBulletDef.bodyDef.fixedRotation = false;
		bouncingBulletDef.fixtureDef.friction = 0f;
		bouncingBulletDef.fixtureDef.restitution = 1f;
		
		return bouncingBulletDef;
	}
	
	private void setAI() {
		FollowerEntity tmp = managerAI.createFollowerEntity(characters.getEnemy("test_group", enemyID), characters.getPlayer());
		tmp.getArrive().setTimeToTarget(1f)
		.setArrivalTolerance(0.5f)
		.setDecelerationRadius(0.01f);
		
		tmp.getFace().setTimeToTarget(1f)
		.setDecelerationRadius(0.5f)
		.setAlignTolerance(0.01f);
		
		tmp.setBehavior();
		
		characters.getEnemy("test_group", enemyID).setMaxLinearSpeed(3f);
	}
	
	@Override
	public void dispose () {
		jBox.dispose();
		batch.dispose();
		charactersTextures.dispose();
		bulletsTextures.dispose();
		hud.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		mainCamera.resize(width, height);
		hud.resize(width, height);
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
		return mainCamera;
	}
	
	public void setMenu() {
		screenManager.setScreen(SCREEN_STATE.PAUSE_MENU);
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
		return batch;
	}

	Semaphore getRenderSemaphore() {
		return stopRender;
	}

	Camera getWorldCamera() {
		return mainCamera;
	}

	CharactersManager getCharacters() {
		return characters;
	}

	public TextureManager getCharactersTextures() {
		return charactersTextures;
	}

	MapManager getMaps() {
		return map;
	}

	Hud getHud() {
		return hud;
	}
	
	void setHud(Hud m_hud) {
		this.hud = m_hud;
	}	

	PauseMenu getPauseMenu() {
		return pauseMenu;
	}

	JBoxObjects getJBox() {
		return jBox;
	}

	void setJBox(JBoxObjects m_jBox) {
		this.jBox = m_jBox;
	}

	public GAME_LEVEL_STATE getGameLevelState() {
		return gameLevelState;
	}

	public void setGameLevelState(GAME_LEVEL_STATE gameLevelState) {
		this.gameLevelState = gameLevelState;
	}

	public TextureManager getBulletsTextures() {
		return bulletsTextures;
	}	
	
	public MissilesManager getMisslesManager() {
		return missleManager;
	}

	public CharactersManager getCharactersManager() {
		return characters;
	}

	public JBoxObjects getjBox() {
		return jBox;
	}

	public AIManager getManagerAI() {
		return managerAI;
	}	
	
	public void setManagerAI(AIManager aiManager) {
		this.managerAI = aiManager;
	}
}


