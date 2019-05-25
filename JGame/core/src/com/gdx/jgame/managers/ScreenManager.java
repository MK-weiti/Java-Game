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
	private HashMap<SCREEN_STATE, Screen> m_gameScreens;
	private SavesManager m_savesManager;
	private MapManager m_map;
	private boolean m_debugMode;
	private boolean m_showLayout;
	private GAME_LEVEL_STATE m_gameState;
	
	public enum SCREEN_STATE {
		PAUSE_MENU,
		PLAY;
	}
	
	public enum GAME_STATE{
		WIN,
		LOSE;
	}
	
	public boolean isDebugMode() {
		return m_debugMode;
	}
	
	@Override
	public void create() {
		setPreferences();
		m_savesManager = new SavesManager();
		m_map = new MapManager("maps/map.tmx", "maps/nextLevel.tmx");
		m_gameScreens = new HashMap<SCREEN_STATE, Screen>();
		m_gameScreens.put(SCREEN_STATE.PAUSE_MENU, new PauseMenu(m_savesManager, this, m_showLayout));
		
		setScreen(SCREEN_STATE.PAUSE_MENU);
	}
	
	public void setScreen(SCREEN_STATE screenName) {
		if(m_gameScreens.get(screenName) == null) {
			// create new Game
			m_map.setMap("map.tmx");
			m_gameScreens.put(SCREEN_STATE.PLAY, new JGame
					(this, m_savesManager, (PauseMenu) m_gameScreens.get(SCREEN_STATE.PAUSE_MENU), m_map));
		}
		super.setScreen(m_gameScreens.get(screenName));
	}
	
	private void setPreferences() {
		
		Preferences prefs = Gdx.app.getPreferences("com.gdx.jgame.Configuration");
		m_debugMode = prefs.getBoolean("Debug", false);
		m_showLayout = prefs.getBoolean("Debug layout", false);
		prefs.putBoolean("Debug", m_debugMode);
		prefs.putBoolean("Debug layout", m_showLayout);
		prefs.flush();
	}
	
	public void createNewGame() {
		JGame tmp;
		tmp = (JGame) m_gameScreens.remove(SCREEN_STATE.PLAY);
		if(tmp != null) tmp.dispose();
		m_map.setMap("map.tmx");
		m_gameScreens.put(SCREEN_STATE.PLAY, new JGame
				(this, m_savesManager, (PauseMenu) m_gameScreens.get(SCREEN_STATE.PAUSE_MENU), m_map));
		super.setScreen(m_gameScreens.get(SCREEN_STATE.PLAY));
	}
	
	public void nextMap() {
		if(m_map.getActualMapName() == "nextLevel.tmx") {
			System.out.println("You win the game!");
			setScreen(SCREEN_STATE.PAUSE_MENU);
			return;
		}
		JGame tmp;
		tmp = (JGame) m_gameScreens.remove(SCREEN_STATE.PLAY);
		if(tmp != null) tmp.dispose();
		m_map.setMap("nextLevel.tmx");
		m_gameScreens.put(SCREEN_STATE.PLAY, new JGame
				(this, m_savesManager, (PauseMenu) m_gameScreens.get(SCREEN_STATE.PAUSE_MENU), m_map));
		super.setScreen(m_gameScreens.get(SCREEN_STATE.PLAY));
	}
	
	public void loadGame() {
		JGame tmp;
		tmp = (JGame) m_gameScreens.remove(SCREEN_STATE.PLAY);
		if(tmp != null) tmp.dispose();
		m_map.setMap(m_savesManager.mapName());
		m_gameScreens.put(SCREEN_STATE.PLAY, new JGame
				(this, m_savesManager, (PauseMenu) m_gameScreens.get(SCREEN_STATE.PAUSE_MENU), m_map));
		super.setScreen(m_gameScreens.get(SCREEN_STATE.PLAY));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		for (Screen screen : m_gameScreens.values()) {
			if(screen != null && getScreen() != screen) screen.dispose();
		}
		m_map.dispose();
	}
	
	@Override
	public void render() {
		super.render();
		
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public GAME_LEVEL_STATE getM_gameState() {
		return m_gameState;
	}

	public void setM_gameState(GAME_LEVEL_STATE m_gameState) {
		this.m_gameState = m_gameState;
	}
	
	
	
}
