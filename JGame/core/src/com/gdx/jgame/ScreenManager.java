package com.gdx.jgame;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;

public class ScreenManager extends Game{
	private HashMap<SCREEN_STATE, Screen> m_gameScreens;
	private RecordManager m_recordManager;
	private boolean m_debugMode;
	private boolean m_showLayout;
	
	public enum SCREEN_STATE {
		PAUSE_MENU,
		PLAY;
	}
	
	public boolean isDebugMode() {
		return m_debugMode;
	}
	
	@Override
	public void create() {
		setPreferences();
		m_recordManager = new RecordManager();
		m_gameScreens = new HashMap<SCREEN_STATE, Screen>();
		m_gameScreens.put(SCREEN_STATE.PAUSE_MENU, new PauseMenu(m_recordManager, this, m_showLayout));
		// and other
		
		setScreen(SCREEN_STATE.PAUSE_MENU);
	}
	
	public void setScreen(SCREEN_STATE screenName) {
		if(m_gameScreens.get(screenName) == null) {
			m_gameScreens.put(SCREEN_STATE.PLAY, new JGame(this, m_recordManager, (PauseMenu) m_gameScreens.get(SCREEN_STATE.PAUSE_MENU)));
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
	
	void createNewGame() {
		JGame tmp;
		tmp = (JGame) m_gameScreens.remove(SCREEN_STATE.PLAY);
		if(tmp != null) tmp.dispose();
		
		m_gameScreens.put(SCREEN_STATE.PLAY, new JGame(this, m_recordManager, (PauseMenu) m_gameScreens.get(SCREEN_STATE.PAUSE_MENU)));
		super.setScreen(m_gameScreens.get(SCREEN_STATE.PLAY));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		for (Screen screen : m_gameScreens.values()) {
			if(screen != null && getScreen() != screen) screen.dispose();
		}
	}
	
	@Override
	public void render() {
		super.render();
		
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
	
}
