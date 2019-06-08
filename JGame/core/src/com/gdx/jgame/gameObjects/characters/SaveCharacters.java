package com.gdx.jgame.gameObjects.characters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

import com.gdx.jgame.JGame;
import com.gdx.jgame.gameObjects.PalpableObject;
import com.gdx.jgame.gameObjects.PalpableObjectPolygonDef;
import com.gdx.jgame.gameObjects.characters.def.BasicEnemyDef;
import com.gdx.jgame.gameObjects.characters.def.PlayerDef;
import com.gdx.jgame.managers.CharactersManager;

public class SaveCharacters implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8904015122021194471L;
	
	private static long m_numberOfObjects = 0;
	public final long ID = m_numberOfObjects;
	
	private transient CharactersManager m_manager;
	private ArrayList<String> m_groupNames;
	private ArrayList<BasicEnemyDef> basicEnemy;
	private PlayerDef playerDef;
	
	public void save() {
		playerDef = m_manager.getPlayerDef();
		
		for(String name : m_manager.getGroupsNamesList()) {
			m_groupNames.add(name);
			for(PlainCharacter character : m_manager.getGroupSets(name)) {
				if(character instanceof BasicEnemy) {
					basicEnemy.add(new BasicEnemyDef( (BasicEnemy)character) );
				}
			}
		}
	}
	
	public void load(JGame jGame, TreeMap<Integer, Object> restoreOwner) {
		m_manager = jGame.getCharactersManager();
		
		playerDef.restore(jGame.getCharactersTextures(), jGame.getjBox().getWorld());
		restoreOwner.put(((PalpableObjectPolygonDef) playerDef).hashCode(), m_manager.addPlayer(playerDef, jGame.getBulletsTextures()));
		System.out.println(playerDef.hashCode());
		
		for(String name : m_groupNames) {
			m_manager.addGroup(name);
			for(BasicEnemyDef character : basicEnemy) {
				character.restore(jGame.getCharactersTextures(), jGame.getjBox().getWorld());
				restoreOwner.put(((PalpableObjectPolygonDef) character).hashCode(), m_manager.addEnemies(character).first());
			}
			// and additional loops for other types
		}
	}
	
	public SaveCharacters(CharactersManager manager) {
		++m_numberOfObjects;
		m_manager = manager;
		m_groupNames = new ArrayList<String>();
		basicEnemy = new ArrayList<BasicEnemyDef>();
	}
	
	public long numberOfObjects() {
		return m_numberOfObjects;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		result = prime * result + ((basicEnemy == null) ? 0 : basicEnemy.hashCode());
		result = prime * result + ((m_groupNames == null) ? 0 : m_groupNames.hashCode());
		result = prime * result + ((playerDef == null) ? 0 : playerDef.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaveCharacters other = (SaveCharacters) obj;
		if (ID != other.ID)
			return false;
		if (basicEnemy == null) {
			if (other.basicEnemy != null)
				return false;
		} else if (!basicEnemy.equals(other.basicEnemy))
			return false;
		if (m_groupNames == null) {
			if (other.m_groupNames != null)
				return false;
		} else if (!m_groupNames.equals(other.m_groupNames))
			return false;
		if (playerDef == null) {
			if (other.playerDef != null)
				return false;
		} else if (!playerDef.equals(other.playerDef))
			return false;
		return true;
	}
	
}
