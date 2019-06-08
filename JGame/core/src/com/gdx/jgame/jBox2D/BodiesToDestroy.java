package com.gdx.jgame.jBox2D;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Body;
import com.gdx.jgame.JGame;
import com.gdx.jgame.gameObjects.characters.PlainCharacter;
import com.gdx.jgame.gameObjects.missiles.MissileAdapter;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;
import com.gdx.jgame.managers.CharactersManager;

public class BodiesToDestroy {
	private JGame m_jGame;
	private JBoxObjects m_jbox;
	private ArrayList<Body> m_array;
	private MissilesManager m_missleManager;
	private CharactersManager m_charManager;
	private int m_capacity = 50;
	
	public BodiesToDestroy(JGame m_jGame) {
		m_array = new ArrayList<Body>();
		m_array.ensureCapacity(m_capacity);
		m_jbox = m_jGame.getjBox();
		m_missleManager = m_jGame.getM_missleManager();
		m_charManager = m_jGame.getCharactersManager();
	}
	
	public void add(Body body) {
		m_array.add(body);
	}
	
	public void removeAll() {
		for(Body body : m_array) {
			Object data = body.getUserData();
			
			if(data instanceof MissileAdapter) {
				m_missleManager.remove((MissileAdapter) data);
			}
			else if(data instanceof PlainCharacter) {
				m_charManager.remove((PlainCharacter) data);
			}
		}
		m_array.clear();
	}
}
