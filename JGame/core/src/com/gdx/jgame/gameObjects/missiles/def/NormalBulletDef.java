package com.gdx.jgame.gameObjects.missiles.def;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.gameObjects.missiles.NormalBullet;
import com.gdx.jgame.managers.TextureManager;

public class NormalBulletDef extends MissileDef implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4566689807523351020L;

	public NormalBulletDef(World world, TextureManager txManager, String texPath, float texScale, Vector2[] vertices) {
		super(world, txManager, texPath, texScale, vertices);
	}
	
	public NormalBulletDef(NormalBulletDef definition) {
		super(definition);
	}
	
	public NormalBulletDef(NormalBullet normalBullet) {
		super(normalBullet);
	}
	
	
}
