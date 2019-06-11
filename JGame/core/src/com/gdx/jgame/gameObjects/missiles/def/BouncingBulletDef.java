package com.gdx.jgame.gameObjects.missiles.def;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.gameObjects.missiles.BouncingBullet;
import com.gdx.jgame.managers.TextureManager;

public class BouncingBulletDef extends MissileDef implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6255251998554385702L;
	
	public int numberOfBounces;
	
	public BouncingBulletDef(World world, TextureManager txManager, String texPath,
			float texScale, Vector2[] vertices) {
		super(world, txManager, texPath, texScale, vertices);
	}
	
	public BouncingBulletDef(BouncingBulletDef definition) {
		super(definition);
		numberOfBounces = definition.numberOfBounces;
	}
	
	public BouncingBulletDef(BouncingBullet definition) {
		super(definition);
		numberOfBounces = definition.getNumberOfBounces();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + numberOfBounces;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BouncingBulletDef other = (BouncingBulletDef) obj;
		if (numberOfBounces != other.numberOfBounces)
			return false;
		return true;
	}
	
}
