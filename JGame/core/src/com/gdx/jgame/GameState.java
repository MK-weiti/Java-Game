package com.gdx.jgame;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.gameObjects.characters.CharacterPolygonDef;
import com.gdx.jgame.gameObjects.characters.CharacterPolygonDef.CharType;
import com.gdx.jgame.gameObjects.characters.CharacterSet;
import com.gdx.jgame.gameObjects.characters.SaveCharacter;
import com.gdx.jgame.jBox2D.JBoxManager;

public class GameState implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7114218668434763987L;
	
	private transient JGame m_game;
	private ArrayList<SaveCharacter> characters;
	private ArrayList<String> charGroupsNames;
	private String mapName;
	private float cameraZoom;
	private Vector2 cameraShift;
	
	public GameState(JGame jGame) {
		characters = new ArrayList<SaveCharacter>();
		charGroupsNames = new ArrayList<String>();
		m_game = jGame;
	}
	
	public void save() {
		mapName = new String (m_game.m_map.getActualMapName());
		cameraZoom = m_game.m_mainCamera.zoom;
		cameraShift = new Vector2(m_game.m_mainCamera.getShift());
		
		for (String name: m_game.m_characters.getGroupsNamesList()) {
			charGroupsNames.add(name);
			for (CharacterSet set : m_game.m_characters.getGroupSets(name)) {
				characters.add(new SaveCharacter(set));
			}
		}
		characters.add(new SaveCharacter(m_game.m_characters.getPlayerSet()));	
	}
	
	public void load(JGame jGame) {
		
		m_game = jGame;
		for (String name : charGroupsNames) {
			m_game.m_characters.addGroup(name);	
		}
		
		m_game.m_mainCamera.zoom = cameraZoom;
		m_game.m_mainCamera.setShift(cameraShift);
		
		m_game.m_map.setMap(mapName);
		m_game.m_jBox = new JBoxManager(m_game.m_map.getLayers(), m_game.m_mainCamera, m_game.isDebugMode(), m_game.isShowLayout());
		
		for (SaveCharacter character : characters) {
			CharacterPolygonDef charDef = character.getCharacterPolygonDef();
			character.restoreCharacter(jGame.m_charactersTextures, jGame.m_jBox.world);
			
			if(charDef.charType == CharType.Player) {
				m_game.m_characters.addPlayer(character.getCharacterPolygonDef());
			} 
			else if(charDef.charType == CharType.Enemy) {
				m_game.m_characters.addEnemies(charDef);
			}
		}
		
		m_game.m_hud = new Hud(m_game.m_batch, m_game.m_characters.getPlayer(), m_game.isDebugMode(), m_game.isShowLayout());
		m_game.m_mainCamera.follower = m_game.m_characters.getPlayer();
	}
}
