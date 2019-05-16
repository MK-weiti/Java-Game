package com.gdx.jgame.gameObjects.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.jgame.gameObjects.PalpableObjectPolygonDef;

public class CharacterPolygonDef extends PalpableObjectPolygonDef{
	public float maxVelocity = 1f;
	public float acceleration = 0f;
	public int maxHealth = 5;
	public int m_health = maxHealth;
	
	public CharacterPolygonDef(World world, Texture texture, Vector2[] vertices) {
		super(world, texture, vertices);
	}
}
