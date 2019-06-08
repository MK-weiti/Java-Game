package com.gdx.jgame.gameObjects.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.Camera;
import com.gdx.jgame.gameObjects.Methods;
import com.gdx.jgame.gameObjects.missiles.Missile;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;
import com.gdx.jgame.gameObjects.missiles.NormalBullet;
import com.gdx.jgame.gameObjects.missiles.BouncingBullet;
import com.gdx.jgame.gameObjects.missiles.def.NormalBulletDef;
import com.gdx.jgame.logic.Armory;
import com.gdx.jgame.logic.ArmoryMethods;
import com.gdx.jgame.gameObjects.missiles.def.BouncingBulletDef;
import com.gdx.jgame.managers.TextureManager;

public class Player extends PlainCharacter implements ArmoryMethods{
	
	public Player(PlayerDef playerDef, TextureManager txBulletManager, MissilesManager missileManager) {
		super(playerDef);
	}
	
	public void spawnBullet(Camera camera, Vector2 space, Missile.MissileType type) {
		m_armory.spawnBullet(camera, space, type);
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
	
}
