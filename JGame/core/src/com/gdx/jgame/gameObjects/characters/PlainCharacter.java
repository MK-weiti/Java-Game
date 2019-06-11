package com.gdx.jgame.gameObjects.characters;

import java.awt.IllegalComponentStateException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.Camera;
import com.gdx.jgame.gameObjects.MovingObject;
import com.gdx.jgame.gameObjects.missiles.Missile.MissileType;
import com.gdx.jgame.gameObjects.missiles.def.MissileDef;
import com.gdx.jgame.logic.Armory;
import com.gdx.jgame.logic.ArmoryMethods;

public abstract class PlainCharacter extends MovingObject implements CharacterMethods, ArmoryMethods{
	private int maxHealth;
	private int health;
	
	Armory armory;
	
	private String groupName = null;
	
	public PlainCharacter(CharacterPolygonDef characterPolygonDef) {
		super(characterPolygonDef, characterPolygonDef.acceleration);
		if(characterPolygonDef.maxHealth == 0) throw new IllegalComponentStateException("No maxHealth.");
		if(characterPolygonDef.health == 0) throw new IllegalComponentStateException("No health.");
		if(characterPolygonDef.charGroupName == null) throw new IllegalComponentStateException("No charGroupName.");
		if(characterPolygonDef.armory == null) throw new IllegalComponentStateException("No armory.");
		
		
		maxHealth = characterPolygonDef.maxHealth;
		health = characterPolygonDef.health;
		groupName = characterPolygonDef.charGroupName;
		
		characterPolygonDef.armory.setOwner(this);
		armory = new Armory(characterPolygonDef.armory);
	}
	
	public Vector2 initialImpulseAndPosition(Camera camera, Vector2 space, MissileDef missile, float initialImpulse, float shift) {
		Vector2 rad = new Vector2(1f, 0f);
		rad.setAngleRad(this.angleRadOnScreen(camera, new Vector2(Gdx.input.getX(), Gdx.input.getY()), ((float) Math.PI)) + shift);		
		return impulseAndPosition(space, missile, initialImpulse, rad);
	}

	private Vector2 impulseAndPosition(Vector2 space, MissileDef missile, float initialImpulse, Vector2 rad) {
		Vector2 bulletPos = new Vector2();
		Vector2 impulse = new Vector2();
		bulletPos.x = this.getPosition().x + (this.getDefaultSprite().getWidth()/2 + space.x) * rad.x;
		bulletPos.y = this.getPosition().y + (this.getDefaultSprite().getHeight()/2 + space.y) * rad.y;
		missile.setPositionInGame(bulletPos);
		missile.setOwner(this);
		
		impulse.x = rad.x * initialImpulse;
		impulse.y = rad.y * initialImpulse;
		
		return impulse;
	}
	
	// TODO need test
	public Vector2 initialImpulseAndPosition(Vector2 target, Vector2 space, MissileDef missile, float initialImpulse, float shift) {
		Vector2 rad = new Vector2(0f, 1f);
		rad.setAngleRad(this.angleToTarget(target, shift));
		return impulseAndPosition(space, missile, initialImpulse, rad);
	}
	
	public Vector2 initialImpulseAndPosition(Vector2 space, MissileDef missile, float initialImpulse, float shift) {
		Vector2 rad = new Vector2(0f, 1f);
		rad.setAngleRad(this.getBodyAngle(shift));		
		return impulseAndPosition(space, missile, initialImpulse, rad);
	}

	public int getMaxHealth() {
		return maxHealth;
	}
	
	public float getMaxHealthFloat() {
		return (float) maxHealth;
	}
	
	public int getHealth() {
		return health;
	}
	
	public float getHealthFloat() {
		return (float) health;
	}
	
	public void setMaxHealth(int health) {
		maxHealth = health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public void changeHealth(int difference) {
		if(health + difference < 0) {
			health = 0;
		}
		else if(health + difference > maxHealth) {
			health = maxHealth;
		}
		else {
			health += difference;
		}
	}
	
	public void giveDamage(int damage) {
		if(health - damage < 0) {
			health = 0;
		}
		else {
			health -= damage;
		}
	}
	
	public void giveHealing(int heal) {
		if(health + heal > maxHealth) {
			health = maxHealth;
		}
		else {
			health += heal;
		}
	}

	public String getGroupName() {
		return groupName;
	}	
	
	@Override
	public void spawnBullet(Vector2 target, Vector2 space, MissileType type) {
		armory.spawnBullet(target, space, type);
	}
	
	@Override
	public void spawnBullet(Camera camera, Vector2 space, MissileType type) {
		armory.spawnBullet(camera, space, type);
	}
	
	@Override
	public void spawnBullet(Vector2 space, MissileType type) {
		armory.spawnBullet(space, type);
	}
	
	@Override
	public float getInitialImpulseNormalBullet() {
		return armory.initialImpulseNormalBullet;
	}
	
	@Override
	public void setInitialImpulseNormalBullet(float initialImpulseNormalBullet) {
		armory.initialImpulseNormalBullet = initialImpulseNormalBullet;
	}

	@Override
	public float getInitialImpulseBouncingBullet() {
		return armory.initialImpulseBouncingBullet;
	}

	@Override
	public void setInitialImpulseBouncingBullet(float initialImpulseBouncingBullet) {
		armory.initialImpulseBouncingBullet = initialImpulseBouncingBullet;
	}
	
	@Override
	public int getMaxAmmoNormalBullet() {
		return armory.maxAmmoNormalBullet;
	}

	@Override
	public void setMaxAmmoNormalBullet(int maxAmmoNormalBullet) {
		armory.maxAmmoNormalBullet = maxAmmoNormalBullet;
	}

	@Override
	public int getMaxAmmoBouncingBullet() {
		return armory.maxAmmoBouncingBullet;
	}

	@Override
	public void setMaxAmmoBouncingBullet(int maxAmmoBouncingBullet) {
		armory.maxAmmoBouncingBullet = maxAmmoBouncingBullet;
	}

	@Override
	public int getCurrentAmmoNormalBullet() {
		return armory.currentAmmoNormalBullet;
	}

	@Override
	public void setCurrentAmmoNormalBullet(int currentAmmoNormalBullet) {
		armory.currentAmmoNormalBullet = currentAmmoNormalBullet;
	}

	@Override
	public int getCurrentAmmoBouncingBullet() {
		return armory.currentAmmoBouncingBullet;
	}

	@Override
	public void setCurrentAmmoBouncingBullet(int currentAmmoBouncingBullet) {
		armory.currentAmmoBouncingBullet = currentAmmoBouncingBullet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((armory == null) ? 0 : armory.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + health;
		result = prime * result + maxHealth;
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
		PlainCharacter other = (PlainCharacter) obj;
		if (armory == null) {
			if (other.armory != null)
				return false;
		} else if (!armory.equals(other.armory))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (health != other.health)
			return false;
		if (maxHealth != other.maxHealth)
			return false;
		return true;
	}
	
}
