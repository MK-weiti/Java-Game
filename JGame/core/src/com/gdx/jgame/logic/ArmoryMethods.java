package com.gdx.jgame.logic;

import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.Camera;
import com.gdx.jgame.gameObjects.missiles.Missile;
import com.gdx.jgame.gameObjects.missiles.Missile.MissileType;

public interface ArmoryMethods {
	public void spawnBullet(Camera camera, Vector2 space, Missile.MissileType type);
	
	public void spawnBullet(Vector2 target, Vector2 space, Missile.MissileType type);
	
	public void spawnBullet(Vector2 space, MissileType type);
	
	public float getInitialImpulseNormalBullet();

	public void setInitialImpulseNormalBullet(float initialImpulseNormalBullet);

	public float getInitialImpulseBouncingBullet();

	public void setInitialImpulseBouncingBullet(float initialImpulseBouncingBullet);
	
	public int getMaxAmmoNormalBullet();

	public void setMaxAmmoNormalBullet(int maxAmmoNormalBullet);

	public int getMaxAmmoBouncingBullet();

	public void setMaxAmmoBouncingBullet(int maxAmmoBouncingBullet);

	public int getCurrentAmmoNormalBullet();

	public void setCurrentAmmoNormalBullet(int currentAmmoNormalBullet);

	public int getCurrentAmmoBouncingBullet();

	public void setCurrentAmmoBouncingBullet(int currentAmmoBouncingBullet);
	
}
