package com.gdx.jgame;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.gameObjects.characters.CharacterPolygonDef;
import com.gdx.jgame.gameObjects.characters.CharacterPolygonDef.CharType;
import com.gdx.jgame.hud.Hud;
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
		mapName = new String (m_game.getMaps().getActualMapName());
		cameraZoom = m_game.getWorldCamera().zoom;
		cameraShift = new Vector2(m_game.getWorldCamera().getShift());
		
		for (String name: m_game.getCharacters().getGroupsNamesList()) {
			charGroupsNames.add(name);
			for (CharacterSet set : m_game.getCharacters().getGroupSets(name)) {
				characters.add(new SaveCharacter(set));
			}
		}
		characters.add(new SaveCharacter(m_game.getCharacters().getPlayerSet()));	
	}
	
	public void load(JGame jGame) {
		
		m_game = jGame;
		for (String name : charGroupsNames) {
			m_game.getCharacters().addGroup(name);	
		}
		
		m_game.getWorldCamera().zoom = cameraZoom;
		m_game.getWorldCamera().setShift(cameraShift);
		
		//m_game.getMaps().setMap(mapName);
		m_game.setJBox(new JBoxManager(m_game, m_game.getMaps().getLayers(), m_game.getWorldCamera(), m_game.isDebugMode(), m_game.isShowLayout()));
		
		for (SaveCharacter character : characters) {
			CharacterPolygonDef charDef = character.getCharacterPolygonDef();
			character.restoreCharacter(jGame.getCharactersTextures(), jGame.getJBox().world);
			
			if(charDef.charType == CharType.Player) {
				m_game.getCharacters().addPlayer(character.getCharacterPolygonDef());
			} 
			else if(charDef.charType == CharType.Enemy) {
				m_game.getCharacters().addEnemies(charDef);
			}
		}
		
		m_game.setHud(new Hud(m_game.getBatch(), m_game.getCharacters().getPlayer(), m_game.isDebugMode(), m_game.isShowLayout()));
		m_game.getWorldCamera().follower = m_game.getCharacters().getPlayer();
	}
	
	public String getMapName() {
		return mapName;
	}
}
