package com.gdx.jgame.gameObjects.characters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

import com.gdx.jgame.IDAdapter;
import com.gdx.jgame.JGame;
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
					BasicEnemyDef tmp = new BasicEnemyDef((BasicEnemy)character);
					basicEnemy.add(tmp);
				}
			}
		}
	}
	
	// put in Map
	public void load(JGame jGame, TreeMap<Integer, IDAdapter> restoreOwner) {
		m_manager = jGame.getCharactersManager();
		
		playerDef.restore(jGame.getjBox().getWorld(), restoreOwner, jGame.getBulletsTextures(), 
				jGame.getCharactersTextures(), jGame.getMisslesManager());
		
		IDAdapter object = m_manager.addPlayer(playerDef, jGame.getBulletsTextures());
		restoreOwner.put(playerDef.getOldIDToBody(), object);
		
		for(String name : m_groupNames) {
			m_manager.addGroup(name);
		}
		
		for(BasicEnemyDef character : basicEnemy) {
			character.restore(jGame.getjBox().getWorld(), restoreOwner, jGame.getBulletsTextures(), 
					jGame.getCharactersTextures(), jGame.getMisslesManager());

			object = m_manager.getEnemy(character.charGroupName, m_manager.addEnemies(character).first());
			restoreOwner.put(character.getOldIDToBody(), object);
		}
		// and additional loops for other types
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
