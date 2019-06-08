package com.gdx.jgame.gameObjects.characters.def;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.gameObjects.characters.PlainCharacter.CHAR_TYPE;
import com.gdx.jgame.gameObjects.characters.Player;
import com.gdx.jgame.managers.TextureManager;

public class PlayerDef extends CharacterPolygonDef implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7507812240552005946L;
	
	public float initialImpulseNormalBullet = 0.005f;
	public float initialImpulseBouncingBullet = 0.005f;
	
	public PlayerDef(World world, TextureManager texManager, String texPath, 
			float texScale, String characterGroupName, Vector2[] b2vertices, CHAR_TYPE charType) {
		super(world,  texManager, texPath, texScale, characterGroupName, b2vertices, charType);
	}
	
	public PlayerDef(PlayerDef definition) {
		super(definition);
		initialImpulseNormalBullet = definition.initialImpulseNormalBullet;
		initialImpulseBouncingBullet = definition.initialImpulseBouncingBullet;
	}
	
	public PlayerDef(Player player) {
		super(player);
		initialImpulseNormalBullet = player.getInitialImpulseNormalBullet();
		initialImpulseBouncingBullet = player.getInitialImpulseBouncingBullet();
	}
}
