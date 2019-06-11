package com.gdx.jgame.managers;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.gdx.jgame.JGame;
import com.gdx.jgame.JGame.GAME_LEVEL_STATE;
import com.gdx.jgame.ui.PauseMenu;

public class ScreenManager extends Game{
	private HashMap<SCREEN_STATE, Screen> gameScreens;
	private SavesManager savesManager;
	private MapManager map;
	private boolean debugMode;
	private boolean showLayout;
	private boolean isTest;
	private GAME_LEVEL_STATE m_gameState;
	
	public enum SCREEN_STATE {
		PAUSE_MENU,
		PLAY;
	}
	
	public enum GAME_STATE{
		WIN,
		LOSE;
	}
	
	public ScreenManager(boolean isTest) {
		this.isTest = isTest;
	}
	
	public boolean isDebugMode() {
		return debugMode;
	}
	
	@Override
	public void create() {
		setPreferences();
		savesManager = new SavesManager();
		map = new MapManager("maps/map.tmx", "maps/nextLevel.tmx");
		gameScreens = new HashMap<SCREEN_STATE, Screen>();
		gameScreens.put(SCREEN_STATE.PAUSE_MENU, new PauseMenu(savesManager, this, showLayout));
		
		setScreen(SCREEN_STATE.PAUSE_MENU);
	}
	
	public void setScreen(SCREEN_STATE screenName) {
		if(gameScreens.get(screenName) == null) {
			// create new Game
			map.setMap("map.tmx");
			gameScreens.put(SCREEN_STATE.PLAY, new JGame
					(this, savesManager, (PauseMenu) gameScreens.get(SCREEN_STATE.PAUSE_MENU), map));
		}
		super.setScreen(gameScreens.get(screenName));
	}
	
	private void setPreferences() {
		
		Preferences prefs = Gdx.app.getPreferences("com.gdx.jgame.Configuration");
		debugMode = prefs.getBoolean("Debug", false);
		showLayout = prefs.getBoolean("Debug layout", false);
		prefs.putBoolean("Debug", debugMode);
		prefs.putBoolean("Debug layout", showLayout);
		prefs.flush();
	}
	
	public void createNewGame() {
		JGame tmp;
		tmp = (JGame) gameScreens.remove(SCREEN_STATE.PLAY);
		if(tmp != null) tmp.dispose();
		map.setMap("map.tmx");
		gameScreens.put(SCREEN_STATE.PLAY, new JGame
				(this, savesManager, (PauseMenu) gameScreens.get(SCREEN_STATE.PAUSE_MENU), map));
		super.setScreen(gameScreens.get(SCREEN_STATE.PLAY));
	}
	
	public void nextMap() {
		if(map.getActualMapName() == "nextLevel.tmx") {
			System.out.println("You win the game!");
			setScreen(SCREEN_STATE.PAUSE_MENU);
			return;
		}
		JGame tmp;
		tmp = (JGame) gameScreens.remove(SCREEN_STATE.PLAY);
		if(tmp != null) tmp.dispose();
		map.setMap("nextLevel.tmx");
		gameScreens.put(SCREEN_STATE.PLAY, new JGame
				(this, savesManager, (PauseMenu) gameScreens.get(SCREEN_STATE.PAUSE_MENU), map));
		super.setScreen(gameScreens.get(SCREEN_STATE.PLAY));
	}
	
	public void loadGame() {
		JGame tmp;
		tmp = (JGame) gameScreens.remove(SCREEN_STATE.PLAY);
		if(tmp != null) tmp.dispose();
		map.setMap(savesManager.mapName());
		gameScreens.put(SCREEN_STATE.PLAY, new JGame
				(this, savesManager, (PauseMenu) gameScreens.get(SCREEN_STATE.PAUSE_MENU), map));
		super.setScreen(gameScreens.get(SCREEN_STATE.PLAY));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		for (Screen screen : gameScreens.values()) {
			if(screen != null && getScreen() != screen) screen.dispose();
		}
		map.dispose();
	}
	
	@Override
	public void render() {
		super.render();
		
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public GAME_LEVEL_STATE getGameState() {
		return m_gameState;
	}

	public void setGameState(GAME_LEVEL_STATE m_gameState) {
		this.m_gameState = m_gameState;
	}

	public boolean isShowLayout() {
		return showLayout;
	}
	
	public JGame testGame() {
		if(isTest) {
			SavesManager savesManager2 = new SavesManager();
			MapManager map2= new MapManager("maps/map.tmx", "maps/nextLevel.tmx");
			HashMap<SCREEN_STATE, Screen> gameScreens2 = new HashMap<SCREEN_STATE, Screen>();
			return new JGame(this, savesManager2, (PauseMenu) gameScreens2.get(SCREEN_STATE.PAUSE_MENU), map2);
		}
		return null;
	}
	
}
