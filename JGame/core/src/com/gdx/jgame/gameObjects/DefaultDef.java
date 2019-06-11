package com.gdx.jgame.gameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.gameObjects.characters.BasicEnemyDef;
import com.gdx.jgame.gameObjects.characters.CharacterPolygonDef;
import com.gdx.jgame.gameObjects.characters.PlayerDef;
import com.gdx.jgame.gameObjects.missiles.def.BouncingBulletDef;
import com.gdx.jgame.gameObjects.missiles.def.MissileDef;
import com.gdx.jgame.gameObjects.missiles.def.NormalBulletDef;

public class DefaultDef {
	public static void defPalpableObjectPolygon(PalpableObjectPolygonDef def) {
		def.bodyDef.fixedRotation = false;
		def.fixtureDef.friction = 0.2f;
		def.fixtureDef.density = 1f;
		def.bodyDef.type = BodyType.DynamicBody;
	}
	
	public static void defMovingObject(MovingObjectDef def) {
		defPalpableObjectPolygon(def);
		def.acceleration = 0.5f;
		def.maxLinearSpeed = 1.5f;
		def.maxLinearAcceleration = 10f;
		def.maxAngularSpeed = 20f;
		def.maxAngularAcceleration = 10f;
		def.zeroThreshold = 1f;
		def.boundingRadious = 1f;
		def.boundingToActivateAI = 2f;
	}
	
	public static void defCharacterPolygon(CharacterPolygonDef def) {
		defMovingObject(def);
		def.maxHealth = 20;
		def.health = 20;
		def.armory.initialImpulseNormalBullet = 0.005f;
		def.armory.initialImpulseBouncingBullet = 0.005f;
		
		def.armory.maxAmmoNormalBullet = 10;
		def.armory.maxAmmoBouncingBullet = 5;
		def.armory.currentAmmoNormalBullet = 10;
		def.armory.currentAmmoBouncingBullet = 5;		
	}
	
	public static void defPlayer(PlayerDef def) {
		defCharacterPolygon(def);
	}
	
	public static void defBasicEnemy(BasicEnemyDef def) {
		defCharacterPolygon(def);
		def.spawnFrequency = 2f;
		def.space = new Vector2(0.15f, 0.15f);
	}
	
	public static void defMissile(MissileDef def) {
		defMovingObject(def);
		def.damage = -1;
	}

	public static void defBouncingBullet(BouncingBulletDef def) {
		defMissile(def);
		def.numberOfBounces = 3;
	}
	
	public static void defNormalBullet(NormalBulletDef def) {
		defMissile(def);
	}
	
	public static void def() {
		
	}
}
