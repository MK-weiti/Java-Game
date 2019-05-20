package com.gdx.jgame.managers;

import java.util.ArrayList;
import java.util.TreeMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.jgame.gameObjects.characters.CharacterPolygonDef;
import com.gdx.jgame.gameObjects.characters.CharacterSet;
import com.gdx.jgame.gameObjects.characters.Enemy;
import com.gdx.jgame.gameObjects.characters.PlainCharacter;
import com.gdx.jgame.gameObjects.characters.Player;

public class CharactersManager{
	private TreeMap<String, TreeMap<Integer, CharacterSet>> m_enemyGroups;
	private CharacterSet playerSet = null;
	
	public CharactersManager(){
		m_enemyGroups = new TreeMap<String, TreeMap<Integer, CharacterSet>>();
	}
	
	public void addGroup(String ... names) {
		for (String name : names) {
			m_enemyGroups.put(name, new TreeMap<Integer, CharacterSet>());
		}
	}
	
	public void addPlayer(CharacterPolygonDef playerDef) {
		if(playerSet != null) {
			throw new IllegalArgumentException("Player already exist");
		}
		playerSet = new CharacterSet(playerDef, new Player(playerDef));
	}
	
	public void deletePlayer() {
		playerSet = null;
	}
	
	public void addEnemies(CharacterPolygonDef ... enemiesDefs) {
		TreeMap<Integer, CharacterSet> tree;
		//groupException(tmp, groupName);
		for (CharacterPolygonDef enemyDef : enemiesDefs) {
			String group = enemyDef.charGroupName;
			tree = m_enemyGroups.get(group);
			CharacterSet set = new CharacterSet(enemyDef, new Enemy(enemyDef));
			tree.put(set.hashCode(), set);
		}
	}
	
	public Player getPlayer() {
		return (Player) playerSet.character;
	}
	
	// A copy of Player`s CharacterPolygonDef
	public CharacterPolygonDef getPlayerDef() {
		return new CharacterPolygonDef(playerSet.characterDef);
	}
	
	public CharacterSet getPlayerSet() {
		return playerSet;
	}
	
	public PlainCharacter getEnemy(String groupName, Integer enemyId) {
		TreeMap<Integer, CharacterSet> tmp = m_enemyGroups.get(groupName);
		groupException(tmp, groupName);
		return tmp.get(enemyId).character;
	}
	
	public ArrayList<String> getGroupsNamesList(){
	   	ArrayList<String> list = new  ArrayList<String>(m_enemyGroups.keySet());
		return list;
	}
	
	public ArrayList<PlainCharacter> getGroupList(String groupName){
		TreeMap<Integer, CharacterSet> tmp = m_enemyGroups.get(groupName);
		ArrayList<PlainCharacter> list = new ArrayList<PlainCharacter>();
		for (CharacterSet set : tmp.values()) {
			list.add(set.character);
		}
		return list;
	}
	
	public ArrayList<CharacterSet> getGroupSets(String groupName){
		TreeMap<Integer, CharacterSet> tmp = m_enemyGroups.get(groupName);
		ArrayList<CharacterSet> list = new ArrayList<CharacterSet>(tmp.values());
		return list;
	}
	
	private void groupException(TreeMap<Integer, CharacterSet> group, String groupName) {
		if(group == null) {
			throw new IllegalArgumentException("This group of enemies do not exist: " + groupName);
		}
	}
	
	public void renderPlayer(SpriteBatch batch) {
		playerSet.character.render(batch);
	}
	
	public void renderEnemiesGroup(SpriteBatch batch, String groupName) {
		ArrayList<PlainCharacter> list = getGroupList(groupName);
		for (PlainCharacter character : list) {
			character.render(batch);
		}
	}
	
	public void renderAll(SpriteBatch batch) {
		renderPlayer(batch);
		ArrayList<String> list = getGroupsNamesList();
		for(String name : list) {
			renderEnemiesGroup(batch, name);
		}
	}
}
