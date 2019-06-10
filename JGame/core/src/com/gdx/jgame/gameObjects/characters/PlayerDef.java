package com.gdx.jgame.gameObjects.characters;

import java.io.Serializable;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;
import com.gdx.jgame.logic.Armory;
import com.gdx.jgame.managers.TextureManager;

public class PlayerDef extends CharacterPolygonDef implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7507812240552005946L;
	
	public PlayerDef(World world, TextureManager charTexManager, TextureManager missileTexManager, MissilesManager missileManager,
			String texPath, float texScale, String characterGroupName, Vector2[] b2vertices) {
		super(world,  charTexManager, missileTexManager, missileManager, texPath, texScale, characterGroupName, b2vertices);
	}
	
	public PlayerDef(PlayerDef definition) {
		super(definition);
	}
	
	public PlayerDef(Player player) {
		super(player);
	}
}
