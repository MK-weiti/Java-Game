package com.gdx.jgame.gameObjects.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.Camera;
import com.gdx.jgame.gameObjects.MovingObject;
import com.gdx.jgame.gameObjects.characters.def.CharacterPolygonDef;
import com.gdx.jgame.gameObjects.missiles.def.MissileDef;

public abstract class PlainCharacter extends MovingObject implements CharacterMethods{
	private int m_maxHealth;
	private int m_health;
	
	//private CHAR_TYPE m_type;
	private String m_groupName;
	
	/*public enum CHAR_TYPE {
		PLAYER,
		ENEMY;
	}*/
	
	public PlainCharacter(CharacterPolygonDef characterPolygonDef) {
		super(characterPolygonDef, characterPolygonDef.maxVelocity, characterPolygonDef.acceleration);
		m_maxHealth = characterPolygonDef.maxHealth;
		m_health = characterPolygonDef.m_health;
		m_groupName = characterPolygonDef.charGroupName;
		//m_type = characterPolygonDef.charType;
	}
	
	protected Vector2 initialImpulseAndPosition(Camera camera, Vector2 space, MissileDef missile, float initialImpulse) {
		Vector2 rad = new Vector2(0f, 1f);
		Vector2 bulletPos = new Vector2();
		Vector2 impulse = new Vector2();
		rad.setAngleRad( - this.rawAngleRadOnScreen(camera, new Vector2(Gdx.input.getX(), Gdx.input.getY())));
		
		
		bulletPos.x = this.getPosition().x + (this.getDefaultSprite().getWidth()/2 + space.x) * rad.x;
		bulletPos.y = this.getPosition().y + (this.getDefaultSprite().getHeight()/2 + space.y) * rad.y;
		missile.setPositionInGame(bulletPos);
		missile.setOwner(this);
		
		impulse.x = rad.x * initialImpulse;
		impulse.y = rad.y * initialImpulse;
		
		return impulse;
	}

	public int getMaxHealth() {
		return m_maxHealth;
	}
	
	public float getMaxHealthFloat() {
		return (float) m_maxHealth;
	}
	
	public int getHealth() {
		return m_health;
	}
	
	public float getHealthFloat() {
		return (float) m_health;
	}
	
	public void setMaxHealth(int health) {
		m_maxHealth = health;
	}
	
	public void setHealth(int health) {
		m_health = health;
	}
	
	public void changeHealth(int difference) {
		if(m_health + difference < 0) {
			m_health = 0;
		}
		else if(m_health + difference > m_maxHealth) {
			m_health = m_maxHealth;
		}
		else {
			m_health += difference;
		}
	}
	
	public void giveDamage(int damage) {
		if(m_health - damage < 0) {
			m_health = 0;
		}
		else {
			m_health -= damage;
		}
	}
	
	public void giveHealing(int heal) {
		if(m_health + heal > m_maxHealth) {
			m_health = m_maxHealth;
		}
		else {
			m_health += heal;
		}
	}

	/*public CHAR_TYPE getCharType() {
		return m_type;
	}

	void setCharType(CHAR_TYPE type) {
		this.m_type = type;
	}*/

	public String getGroupName() {
		return m_groupName;
	}	
	
}
