package com.gdx.jgame.gameObjects.characters;

import java.awt.IllegalComponentStateException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.gameObjects.missiles.Missile;

public class BasicEnemy extends PlainCharacter{

	private float spawnFrequency; // seconds
	private Vector2 space; // at what distance the projectile will spawn (ellipse)
	
	public BasicEnemy(BasicEnemyDef enemyDef) {
		super(enemyDef);
		if(enemyDef.space == null) throw new IllegalComponentStateException("No space.");
		if(enemyDef.spawnFrequency == 0) throw new IllegalComponentStateException("No spawnFrequency.");
		
		spawnFrequency = enemyDef.spawnFrequency;
		space = new Vector2(enemyDef.space);
	}	
	
	// do not copy, only for algorithm
	private float sum = 0;
	
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		
		sum += Gdx.graphics.getRawDeltaTime();
		
		if(sum > spawnFrequency) {
			this.spawnBullet(space, Missile.MissileType.NormalBullet);
			sum -= spawnFrequency;
		}
		
	}

	@Override
	public void updateObject(Object object) {
		if(object instanceof Missile) {
			changeHealth(((Missile) object).getDamage());
		}
	}

	@Override
	public boolean isRemovable() {
		if(getHealth() > 0) return false;
		return isDestructible();
	}

	@Override
	public void deleteObject() {
		destoryBody();
	}

	public float getSpawnFrequency() {
		return spawnFrequency;
	}

	public Vector2 getSpace() {
		return new Vector2(space);
	}
	
	

}
