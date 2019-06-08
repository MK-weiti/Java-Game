package com.gdx.jgame.managers;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.jgame.gameObjects.characters.BasicEnemy;
import com.gdx.jgame.gameObjects.characters.PlainCharacter;
import com.gdx.jgame.gameObjects.characters.Player;
import com.gdx.jgame.gameObjects.characters.def.CharacterPolygonDef;
import com.gdx.jgame.gameObjects.characters.def.PlayerDef;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;

public class CharactersManager{
	private TreeMap<String, TreeMap<Integer, PlainCharacter>> m_enemyGroups;
	private Player m_player = null;
	private MissilesManager m_missleManager;
	private PlainCharacter m_cameraFollower = null;

	public CharactersManager(MissilesManager missleManager){
		m_enemyGroups = new TreeMap<String, TreeMap<Integer, PlainCharacter>>();
		m_missleManager = missleManager;
	}
	
	public void addGroup(String ... names) {
		for (String name : names) {
			m_enemyGroups.put(name, new TreeMap<Integer, PlainCharacter>());
		}
	}
	
	public Player addPlayer(PlayerDef playerDef, TextureManager txBulletManager) {
		if(m_player != null) {
			throw new IllegalArgumentException("Player already exist");
		}
		m_player = new Player(playerDef, txBulletManager, m_missleManager);
		return m_player;
	}
	
	public TreeSet<Integer> addEnemies(CharacterPolygonDef ... enemiesDefs) {
		TreeMap<Integer, PlainCharacter> tree;
		TreeSet<Integer> retVal = new TreeSet<Integer>();
		for (CharacterPolygonDef enemyDef : enemiesDefs) {
			String group = enemyDef.charGroupName;
			tree = m_enemyGroups.get(group);
			groupException(tree, group);
			BasicEnemy tmp = new BasicEnemy(enemyDef);
			tree.put(tmp.ID, tmp);
			retVal.add(tmp.ID);
		}
		return retVal;
	}
	
	public Player getPlayer() {
		return (Player) m_player;
	}
	
	public PlayerDef getPlayerDef() {
		return new PlayerDef(m_player);
	}
	
	public PlainCharacter getEnemy(String groupName, Integer enemyId) {
		TreeMap<Integer, PlainCharacter> tmp = m_enemyGroups.get(groupName);
		groupException(tmp, groupName);
		return tmp.get(enemyId);
	}
	
	public ArrayList<String> getGroupsNamesList(){
	   	ArrayList<String> list = new  ArrayList<String>(m_enemyGroups.keySet());
		return list;
	}
	
	public ArrayList<PlainCharacter> getGroupList(String groupName){
		TreeMap<Integer, PlainCharacter> tmp = m_enemyGroups.get(groupName);
		ArrayList<PlainCharacter> list = new ArrayList<PlainCharacter>();
		for (PlainCharacter set : tmp.values()) {
			list.add(set);
		}
		return list;
	}
	
	public ArrayList<PlainCharacter> getGroupSets(String groupName){
		TreeMap<Integer, PlainCharacter> tmp = m_enemyGroups.get(groupName);
		ArrayList<PlainCharacter> list = new ArrayList<PlainCharacter>(tmp.values());
		return list;
	}
	
	private void groupException(TreeMap<Integer, PlainCharacter> group, String groupName) {
		if(group == null) {
			throw new IllegalArgumentException("This group of enemies do not exist: " + groupName);
		}
	}
	
	public void renderPlayer(SpriteBatch batch) {
		if(m_player != null) m_player.render(batch);
	}
	
	public void renderEnemiesGroup(SpriteBatch batch, String groupName) {
		ArrayList<PlainCharacter> list = getGroupList(groupName);
		for (PlainCharacter character : list) {
			character.render(batch);
		}
	}
	
	public void renderAll(SpriteBatch batch) {
		ArrayList<String> list = getGroupsNamesList();
		for(String name : list) {
			renderEnemiesGroup(batch, name);
		}
		renderPlayer(batch);
	}
	
	public void remove(PlainCharacter character) {
		if(character.isRemovable()) {
			if(character.equals(m_player)) {
				if(m_cameraFollower == m_player) m_cameraFollower = null;
				m_player = null;
			}
			else {
				PlainCharacter tmp;
				for(TreeMap<Integer, PlainCharacter> tree : m_enemyGroups.values()) {
					tmp = tree.remove(character.ID);
					if(tmp != null && tmp.equals(m_player)) m_cameraFollower = null;
				}
			}
			character.deleteObject();
		}
	}
	
	public PlainCharacter getCameraFollower() {
		return m_cameraFollower;
	}

	public boolean setCameraFollower(PlainCharacter cameraFollower) {	
		if(cameraFollower == null) throw new IllegalArgumentException("Follower is null.");
		if(cameraFollower.equals(m_player) || isEnemyIn(cameraFollower)) {
			this.m_cameraFollower = cameraFollower;
			return false;
		}
		return true;
	}
	
	private boolean isEnemyIn(PlainCharacter cameraFollower) {
		for(String name : m_enemyGroups.keySet()) {
			TreeMap<Integer, PlainCharacter> tree = m_enemyGroups.get(name);
			for (PlainCharacter character : tree.values()) {
				if(character.equals(cameraFollower)) return true;
			}
		}
		return false;
	}
}
