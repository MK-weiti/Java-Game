package com.gdx.jgame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.hud.Hud;
import com.gdx.jgame.gameObjects.characters.PlainCharacter;
import com.gdx.jgame.gameObjects.characters.SaveCharacters;
import com.gdx.jgame.gameObjects.characters.def.CharacterPolygonDef;
import com.gdx.jgame.gameObjects.missiles.SaveMisiles;
import com.gdx.jgame.jBox2D.JBoxObjects;

public class GameState implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3912629976721885987L;
	
	private transient JGame m_game;
	private SaveCharacters characters;
	private SaveMisiles missiles;
	private String mapName;
	private float cameraZoom;
	private Vector2 cameraShift;
	
	public GameState(JGame jGame) {
		characters = new SaveCharacters(jGame.getCharactersManager());
		missiles = new SaveMisiles(jGame.getMisslesManager());
		m_game = jGame;
	}
	
	public void save() {
		mapName = new String (m_game.getMaps().getActualMapName());
		cameraZoom = m_game.getWorldCamera().zoom;
		cameraShift = new Vector2(m_game.getWorldCamera().getShift());
		
		missiles.save();
		characters.save();
	}
	
	public void load(JGame jGame) {
		TreeMap<Integer, Object> restoreOwner = new TreeMap<Integer, Object>();
		m_game = jGame;
		
		m_game.getWorldCamera().zoom = cameraZoom;
		m_game.getWorldCamera().setShift(cameraShift);
		
		m_game.setJBox(new JBoxObjects(m_game, m_game.getMaps().getLayers(), m_game.getWorldCamera(), m_game.isDebugMode(), m_game.isShowLayout()));
		
		characters.load(jGame, restoreOwner);
		
		m_game.setHud(new Hud(m_game.getBatch(), m_game.getCharacters().getPlayer(), m_game.isDebugMode(), m_game.isShowLayout()));
		m_game.getCharacters().setCameraFollower(m_game.getCharacters().getPlayer());
		missiles.load(m_game, restoreOwner);
	}
	
	public String getMapName() {
		return mapName;
	}
}
