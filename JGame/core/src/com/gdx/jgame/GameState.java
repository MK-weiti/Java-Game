package com.gdx.jgame;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.TreeMap;

import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.hud.Hud;
import com.gdx.jgame.gameObjects.PalpableObject;
import com.gdx.jgame.gameObjects.PalpableObjectPolygonDef;
import com.gdx.jgame.gameObjects.characters.SaveCharacters;
import com.gdx.jgame.gameObjects.missiles.SaveMisiles;
import com.gdx.jgame.jBox2D.JBoxObjects;
import com.gdx.jgame.logic.ai.AIManager;

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
	private AIManager managerAI;
	
	public GameState(JGame jGame) {
		characters = new SaveCharacters(jGame.getCharactersManager());
		missiles = new SaveMisiles(jGame.getMisslesManager());
		m_game = jGame;
		managerAI = jGame.getManagerAI();
	}
	
	public void save() {
		mapName = new String (m_game.getMaps().getActualMapName());
		cameraZoom = m_game.getWorldCamera().zoom;
		cameraShift = new Vector2(m_game.getWorldCamera().getShift());
		
		saveGameObjects();
	}
	
	public void load(JGame jGame) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		m_game = jGame;
		
		Field f1 = PalpableObject.class.getDeclaredField("m_numberOfObjects");
		Field f2 = PalpableObjectPolygonDef.class.getDeclaredField("m_numberOfObjects");
		
		f1.setAccessible(true);
		f2.setAccessible(true);
		f1.setInt(null, 0);
		f2.setInt(null, 0);

		m_game.getWorldCamera().zoom = cameraZoom;
		m_game.getWorldCamera().setShift(cameraShift);
		
		m_game.setJBox(new JBoxObjects(m_game, m_game.getMaps().getLayers(), m_game.getWorldCamera(), m_game.isDebugMode(), m_game.isShowLayout()));
		
		loadGameObjects();
		
		
		m_game.setHud(new Hud(m_game.getBatch(), m_game.getCharacters().getPlayer(), m_game.isDebugMode(), m_game.isShowLayout()));
		m_game.getCharacters().setCameraFollower(m_game.getCharacters().getPlayer());
		
		jGame.setManagerAI(managerAI);
	}
	
	public String getMapName() {
		return mapName;
	}
	
	private void saveGameObjects() {
		missiles.save();
		characters.save();
		managerAI.save();
	}
	
	private void loadGameObjects() {
		// the order matters
		TreeMap<Integer, IDAdapter> restoreOwner = new TreeMap<Integer, IDAdapter>();
		
		characters.load(m_game, restoreOwner);
		missiles.load(m_game, restoreOwner);
		managerAI.load(restoreOwner);
	}
}
