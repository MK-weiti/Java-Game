package com.gdx.jgame.gameObjects.characters.def;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.gameObjects.characters.BasicEnemy;
import com.gdx.jgame.gameObjects.characters.PlainCharacter.CHAR_TYPE;
import com.gdx.jgame.managers.TextureManager;

public class BasicEnemyDef extends CharacterPolygonDef implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 982871793147669843L;

	public BasicEnemyDef(World world, TextureManager texManager, String texPath, 
			float texScale, String characterGroupName, Vector2[] b2vertices, CHAR_TYPE charType) {
		super(world,  texManager, texPath, texScale, characterGroupName, b2vertices, charType);
	}
	
	public BasicEnemyDef(BasicEnemyDef definition) {
		super(definition);
	}
	
	public BasicEnemyDef(BasicEnemy player) {
		super(player);
	}
}
