package com.gdx.jgame.gameObjects.characters;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;
import com.gdx.jgame.managers.TextureManager;

public class BasicEnemyDef extends CharacterPolygonDef implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 982871793147669843L;

	public float spawnFrequency; // seconds
	public Vector2 space;
	
	public BasicEnemyDef(World world, TextureManager charTexManager, TextureManager missileTexManager, MissilesManager missileManager,
			String texPath, float texScale, String characterGroupName, Vector2[] b2vertices) {
		super(world,  charTexManager, missileTexManager, missileManager, texPath, texScale, characterGroupName, b2vertices);
	}
	
	public BasicEnemyDef(BasicEnemyDef definition) {
		super(definition);
		spawnFrequency = definition.spawnFrequency;
		space = new Vector2(definition.space);
	}
	
	public BasicEnemyDef(BasicEnemy player) {
		super(player);
		spawnFrequency = player.getSpawnFrequency();
		space = player.getSpace();
	}
}
