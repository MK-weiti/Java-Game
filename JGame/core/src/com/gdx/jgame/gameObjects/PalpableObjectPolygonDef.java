package com.gdx.jgame.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.jgame.world.MapManager;

public class PalpableObjectPolygonDef implements Disposable{
	public BodyDef bodyDef;
	public FixtureDef fixtureDef;
	public PolygonShape shape;
	
	public Texture texture;
	public World world;
	public float scale;
	
	public PalpableObjectPolygonDef(World world, Texture texture, Vector2[] vertices) {
		bodyDef = new BodyDef();
		fixtureDef = new FixtureDef();
		shape = new PolygonShape();
		
		this.world = world;
		this.texture = texture;
		
		bodyDef.type =  BodyType.StaticBody;
		bodyDef.fixedRotation = true;
		
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;	// 16 x 16 * density
		fixtureDef.friction = 0.5f;
		
		shape.set(vertices);
		
		scale = 1f;
	}

	@Override
	public void dispose() {
		shape.dispose();
	}
	
	public void setPosition(float posX, float posY) {
		bodyDef.position.set(posX / MapManager.PIXELS_PER_METER, 
				posY / MapManager.PIXELS_PER_METER);
	}
	
}
